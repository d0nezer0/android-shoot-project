package org.ggxz.shoot.mvp.view.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.utils.TimeUtils;
import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.mode.ShootDataModel;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.utils.ToastUtils;
import com.example.net_module.mode.InitMode;
import com.google.gson.Gson;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.HistoryAdapter;
import org.ggxz.shoot.adapter.ItemClickListener;
import org.ggxz.shoot.adapter.TopAdapter;
import org.ggxz.shoot.mvp.presenter.impl.HistoryPresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.HistoryView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class HistoryActivity extends BaseMvpActivity<HistoryPresenterImpl> implements HistoryView<ShootDataModel> {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.backHomeTv)
    TextView backHomeTv;
    @BindView(R.id.startData)
    TextView startTime;
    @BindView(R.id.endData)
    TextView endTime;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.tvReset)
    TextView tvReset;

    private HistoryAdapter adapter;
    private List<ShootDataModel> adapterData = new ArrayList<>();
    private long start = new Date().getTime();
    private long end = new Date().getTime();

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new HistoryAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((holder, position) -> {
            Intent intent = new Intent(HistoryActivity.this, DetailsActivity.class);
            ShootDataModel shootDataModel = adapterData.get(position);
            long userId = shootDataModel.getUserId();
            UserModel user = DbDownUtil.getInstance().findUser(userId);
            List<ShootDataModel> shootData = user.getShootData();
            if (shootData==null||shootData.isEmpty()){
                ToastUtils.showToast("用户最新设计数据异常");
                return;
            }
            ArrayList<Integer> ids = new ArrayList<>();
            for (int i = 0; i < user.getShootData().size(); i++) {
                ids.add((int) user.getShootData().get(i).getBoutId());
            }
            intent.putExtra(DetailsActivity.INTENT_CLICK_POS_KEY, 0);
            intent.putExtra(DetailsActivity.INTENT_KEY_TYPE, 2);
            intent.putIntegerArrayListExtra(DetailsActivity.INTENT_KEY, ids);
            startActivity(intent);
        });

        /*========click start=======*/
        backHomeTv.setOnClickListener(v -> finish());
        startTime.setOnClickListener(v -> {
            String[] split = startTime.getText().toString().split("-");
            DatePickerDialog dialog = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String time = year + "-" + (month + 1) + "-" + dayOfMonth;
                    startTime.setText(time);
                    start = TimeUtils.string2Date(time + " 00:00:00").getTime();
                }
            }, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            dialog.show();
        });

        endTime.setOnClickListener(v -> {
            String[] split = endTime.getText().toString().split("-");

            DatePickerDialog dialog = new DatePickerDialog(HistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String time = year + "-" + (month + 1) + "-" + dayOfMonth;
                    endTime.setText(time);
                    end = TimeUtils.string2Date(time + " 23:59:59").getTime();


                }
            }, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            dialog.show();
        });

        tvSearch.setOnClickListener(v -> {
            String userName = etName.getText().toString();
            List<UserModel> dataModels;
            if (TextUtils.isEmpty(userName)) {
                dataModels = DbDownUtil.getInstance().findAllUserByTime(start, end);
            } else {
                dataModels = DbDownUtil.getInstance().findAllUserByTimeAndName(start, end, userName);
            }
            setResetSearchConditions(dataModels);

        });
        tvReset.setOnClickListener(v -> initDefUi());
        /*========click end=======*/

    }

    @Override
    protected void initData() {
        initDefUi();
    }

    private void initDefUi() {
        List<UserModel> allUser = DbDownUtil.getInstance().findAllUser();
        setResetSearchConditions(allUser);
        if (!adapterData.isEmpty()) { //  +- 500 为了00:00:00 23:59:59 处理
            if (adapterData.size() == 1) {
                end = adapterData.get(0).getCreateTime();
                start = adapterData.get(0).getCreateTime();
            } else {
                end = adapterData.get(0).getCreateTime();
                start = adapterData.get(adapterData.size() - 1).getCreateTime();
            }
        }


        startTime.setText(TimeUtils.millis2String(start, "yyyy-MM-dd"));
        endTime.setText(TimeUtils.millis2String(end, "yyyy-MM-dd"));
        etName.setText("");
    }

    //adapterData.size() start_time 和 end_time相等
    private void setResetSearchConditions(List<UserModel> users) {
        adapterData.clear();
        for (int i = 0; i < users.size(); i++) {
            List<ShootDataModel> shootData = users.get(i).getShootData();
            if (shootData != null && !shootData.isEmpty())
                adapterData.add(shootData.get(shootData.size() - 1));
        }
        adapter.setData(adapterData);

    }


    @Override
    protected HistoryPresenterImpl initInjector() {
        return new HistoryPresenterImpl();
    }

    @Override
    public void onHistoryDataSuccess(ShootDataModel shootDataModel) {

    }

    @Override
    public void onHistoryDataError() {

    }
}
