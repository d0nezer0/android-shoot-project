package org.ggxz.shoot.mvp.view.activity;


import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.utils.TimeUtils;
import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.base.mvp.BasePresenterImpl;
import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.mode.ShootDataModel;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.view.NoScrollerViewPager;
import com.example.net_module.mode.InitMode;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.DetailsFragmentAdapter;
import org.ggxz.shoot.mvp.presenter.impl.DetailsPresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.DetailsView;
import org.ggxz.shoot.mvp.view.fragment.DetailsPage1Fragment;
import org.ggxz.shoot.mvp.view.fragment.DetailsPage2Fragment;
import org.ggxz.shoot.mvp.view.fragment.DetailsPage3Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends BaseMvpActivity<BasePresenterImpl> implements DetailsView<ShootDataModel> {

    public static final String INTENT_KEY = "intent_data";
    public static final String INTENT_KEY_TYPE = "intent_key_type";
    public static final String INTENT_CLICK_POS_KEY = "intent_click_pos_key";
    private DetailsPage1Fragment page1Fragment;
    private DetailsPage2Fragment page2Fragment;
    private DetailsPage3Fragment page3Fragment;
    private DetailsFragmentAdapter fragmentAdapter;
    @BindView(R.id.viewPager)
    NoScrollerViewPager viewPager;
    @BindView(R.id.tvBtn01)
    TextView tvBtn01;
    @BindView(R.id.tvBtn02)
    TextView tvBtn02;
    @BindView(R.id.tvBtn03)
    TextView tvBtn03;
    @BindView(R.id.backHomeTv)
    TextView backHomeTv;

    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.tvCurBout)
    TextView tvCurBout;
    @BindView(R.id.boutTime)
    TextView boutTime;
    @BindView(R.id.totalRing)
    TextView totalRing;
    DecimalFormat df = new DecimalFormat("#.#");


    //1是Main  2是History
    private int type = 1;
    public int curBout = 1;
    List<ShootDataModel> data;
    ArrayList<Integer> ids;
    private int clickPos = 0;
    private SPUtils spUtils = SPUtils.getInstance(getApplication());

    @Override
    protected void initView() {
        ids = getIntent().getIntegerArrayListExtra(INTENT_KEY);
        clickPos = getIntent().getIntExtra(INTENT_CLICK_POS_KEY, 0);
        type = getIntent().getIntExtra(INTENT_KEY_TYPE, 1);
        data = DbDownUtil.getInstance().searchListBout(ids);
        if (type == 1)
            curBout = spUtils.getInt(Constant.CUR_BOUT);
        else
            curBout = data.size();
        List<Fragment> fragments = new ArrayList<>();
        page1Fragment = DetailsPage1Fragment.newInstance(ids, clickPos, type);
        page2Fragment = DetailsPage2Fragment.newInstance(ids, clickPos, type);
        page3Fragment = DetailsPage3Fragment.newInstance(ids, clickPos, type);
        fragments.add(page1Fragment);
        fragments.add(page2Fragment);
        fragments.add(page3Fragment);
        fragmentAdapter = new DetailsFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        tvBtn01.setOnClickListener(v -> {
            viewPager.setCurrentItem(0, false);
            page1Fragment.initView();
            clickChangeBg(0, true);
        });
        tvBtn02.setOnClickListener(v -> {
            viewPager.setCurrentItem(1, false);
            page2Fragment.initView();
            clickChangeBg(1, true);
        });
        tvBtn03.setOnClickListener(v -> {
            viewPager.setCurrentItem(2, false);
            page3Fragment.initView();
            clickChangeBg(2, true);
        });
        backHomeTv.setOnClickListener(v -> {
            startActivity(new Intent(DetailsActivity.this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });


        if (!data.isEmpty()) {
            if (type == 1) {
                initUi(spUtils.getInt(Constant.CUR_BOUT) - 1);
            } else {
                initUi(data.size() - 1);
            }
        }
    }

    public void initUi(int position) {
        if (position > data.size())
            return;
        ShootDataModel model = data.get(position);
        userName.setText(model.getUserName());
        tvCurBout.setText(String.valueOf(model.getBoutNum()));
        boutTime.setText(TimeUtils.millis2String(model.getCreateTime()));
        totalRing.setText(df.format(model.getTotalRing()));
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_details);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenterImpl initInjector() {
        return new DetailsPresenterImpl();
    }

    @Override
    public void getDetailsDataSuccess(ShootDataModel e) {

    }

    @Override
    public void onDetailsDataError(String msg) {

    }

    private void clickChangeBg(int type, boolean isClick) {
        switch (type) {
            case 0:
                if (isClick) {
                    tvBtn01.setBackground(ContextCompat.getDrawable(this, R.drawable.left_check_bg));
                    tvBtn02.setBackground(ContextCompat.getDrawable(this, R.drawable.left_uncheck_bg));
                    tvBtn03.setBackground(ContextCompat.getDrawable(this, R.drawable.right_uncheck_bg));
                }
                break;
            case 1:
                if (isClick) {
                    tvBtn01.setBackground(ContextCompat.getDrawable(this, R.drawable.left_uncheck_bg));
                    tvBtn02.setBackground(ContextCompat.getDrawable(this, R.drawable.left_check_bg));
                    tvBtn03.setBackground(ContextCompat.getDrawable(this, R.drawable.right_uncheck_bg));
                }
                break;
            case 2:
                if (isClick) {
                    tvBtn01.setBackground(ContextCompat.getDrawable(this, R.drawable.left_uncheck_bg));
                    tvBtn02.setBackground(ContextCompat.getDrawable(this, R.drawable.left_uncheck_bg));
                    tvBtn03.setBackground(ContextCompat.getDrawable(this, R.drawable.right_check_bg));
                }
                break;
        }
    }


}
