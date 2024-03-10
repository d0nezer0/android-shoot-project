package org.ggxz.shoot.mvp.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.blankj.utilcode.utils.HandlerUtils;
import com.example.common_module.App;
import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.EnterInfo;
import com.example.common_module.db.mode.DaoMaster;
import com.example.common_module.db.mode.DaoSession;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.db.mode.ShootDataModel;
import com.example.common_module.db.mode.SingleShootDataModel;
import com.example.common_module.utils.BinaryUtils;
import com.example.common_module.utils.FileUtils;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.utils.ToastUtils;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.DetailsRVAdapter;
import org.ggxz.shoot.mvp.view.activity.DetailsActivity;
import org.ggxz.shoot.mvp.view.activity.MainActivity;
import org.ggxz.shoot.widget.TargetPointView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsPage1Fragment extends Fragment {
    private static final String TAG = "DetailsPage1Fragment";


    private ArrayList<Integer> ids;
    private int pos = 0;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.curNum)
    TextView curNum;
    @BindView(R.id.targetView)
    TargetPointView targetView;
    @BindView(R.id.grade_progressBar)
    ProgressBar gradeProgressBar;
    @BindView(R.id.grade_count)
    TextView gradeCount;
    @BindView(R.id.gun_progressBar)
    ProgressBar gunProgressBar;
    @BindView(R.id.gun_count)
    TextView gunCount;
    @BindView(R.id.collimation_progressBar)
    ProgressBar collimationProgressBar;
    @BindView(R.id.collimation_count)
    TextView collimationCount;
    @BindView(R.id.send_progressBar)
    ProgressBar sendProgressBar;
    @BindView(R.id.send_count)
    TextView sendCount;
    @BindView(R.id.total_progressBar)
    ProgressBar totalProgressBar;
    @BindView(R.id.total_count)
    TextView totalCount;
    @BindView(R.id.tvUp01)
    TextView tvUp01;
    @BindView(R.id.tvDown01)
    TextView tvDown01;
    @BindView(R.id.tvUp02)
    TextView tvUp02;
    @BindView(R.id.tvDown02)
    TextView tvDown02;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private List<ShootDataModel> models;
    private DetailsRVAdapter adapter;

    private SPUtils spUtils;
    private ShootDataModel curShoot;
    private SingleShootDataModel curSingle;
    //1是Main  2是History
    private int type = 1;
    private DetailsActivity activity;

    public static DetailsPage1Fragment newInstance(ArrayList<Integer> ids, int pos, int type) {
        DetailsPage1Fragment fragment = new DetailsPage1Fragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PARAM1, ids);
        args.putInt(ARG_PARAM2, pos);
        args.putInt(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ids = getArguments().getIntegerArrayList(ARG_PARAM1);
            pos = getArguments().getInt(ARG_PARAM2);
            type = getArguments().getInt(ARG_PARAM3);
            spUtils = SPUtils.getInstance(Objects.requireNonNull(getActivity()).getApplication());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_page1, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }


    @SuppressLint("NewApi")
    public void initView() {
        activity = ((DetailsActivity) Objects.requireNonNull(getActivity()));
        models = DbDownUtil.getInstance().searchListBout(ids);
        if (type == 1)
            activity.curBout = spUtils.getInt(Constant.CUR_BOUT);
//        else
//            activity.curBout = models.size();


        curShoot = models.get(activity.curBout - 1);
        List<SingleShootDataModel> singleShootDataModels = curShoot.getData();
        curSingle = singleShootDataModels.get(pos);

        adapter = new DetailsRVAdapter(requireContext(), singleShootDataModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            targetView.setValues(new ArrayList<EntryModel>());
            List<SingleShootDataModel> data = curShoot.getData();
            curSingle = data.get(position);
            pos = position;
            adapter.setPos(pos);
            targetView(curSingle);

        });

        adapter.setPos(pos);
        recyclerView.scrollToPosition(pos);
        targetView(curSingle);

        initClick();
    }

    private void initData() {

    }

    private void targetView(SingleShootDataModel model) {
        targetView.setRingCount(10); // 设置环数为10
        targetView.setBackgroundColor(Color.TRANSPARENT); // 设置背景色为灰色

        targetView.setXAxisRange(-10f, 10f);
        targetView.setYAxisRange(-10f, 10f);
        List<EntryModel> check = model.getCheck();
        targetView.setAnimationXAxisMills(check.size() * 100, check);
        targetView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        //1 成绩
        gradeProgressBar.setProgress(model.getGrade());
        gradeCount.setText(String.valueOf(model.getGrade()));
        // 2.据枪逻辑
        gunProgressBar.setProgress(model.getShoot());
        gunCount.setText(String.valueOf(model.getShoot()));
        //3 瞄准
        collimationProgressBar.setProgress(model.getCollimation());
        collimationCount.setText(String.valueOf(model.getCollimation()));
        //4.击发
        sendProgressBar.setProgress(model.getSend());
        sendCount.setText(String.valueOf(model.getSend()));
        //总分
        totalProgressBar.setProgress(model.getAllGrade());
        totalCount.setText(String.valueOf(model.getAllGrade()));

    }

    private void initClick() {
        tvDown01.setOnClickListener(v -> {
            if (pos == curShoot.getData().size() - 1) {
                ToastUtils.showToast("当前是最后发序");
                return;
            }
            pos++;
            List<SingleShootDataModel> singleShootDataModels = curShoot.getData();
            curSingle = singleShootDataModels.get(pos);
            adapter.setPos(pos);
            recyclerView.scrollToPosition(pos);
            targetView(curSingle);
        });
        tvUp01.setOnClickListener(v -> {
            if (pos == 0) {
                ToastUtils.showToast("当前是开始发序");
                return;
            }
            pos--;
            List<SingleShootDataModel> singleShootDataModels = curShoot.getData();
            curSingle = singleShootDataModels.get(pos);
            adapter.setPos(pos);
            recyclerView.scrollToPosition(pos);
            targetView(curSingle);
        });

        tvUp02.setOnClickListener(v -> {
            if (activity.curBout == 1) {
                ToastUtils.showToast("当前是最新一局");
                return;
            }
            activity.curBout -= 1;
            curShoot = models.get(activity.curBout - 1);
            if (!curShoot.getData().isEmpty()) {
                activity.initUi(activity.curBout - 1);
                curSingle = curShoot.getData().get(0);
                pos = 0;
                adapter.setPos(pos);
                adapter.setData(curShoot.getData());
                recyclerView.scrollToPosition(pos);
                targetView(curSingle);
            }

        });

        tvDown02.setOnClickListener(v -> {
            if (activity.curBout == models.size()) {
                ToastUtils.showToast("当前是最后一局");
                return;
            }
            activity.curBout += 1;
            curShoot = models.get(activity.curBout - 1);
            if (!curShoot.getData().isEmpty()) {
                activity.initUi(activity.curBout - 1);
                curSingle = curShoot.getData().get(0);
                pos = 0;
                adapter.setPos(pos);
                adapter.setData(curShoot.getData());
                recyclerView.scrollToPosition(pos);
                targetView(curSingle);
            }

        });
    }
}
