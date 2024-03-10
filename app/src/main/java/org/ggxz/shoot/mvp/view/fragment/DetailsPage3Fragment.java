package org.ggxz.shoot.mvp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.db.mode.ShootDataModel;
import com.example.common_module.db.mode.SingleShootDataModel;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.utils.ToastUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.DetailsRVAdapter;
import org.ggxz.shoot.mvp.view.activity.DetailsActivity;
import org.ggxz.shoot.mvp.view.activity.MainAvtivityHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsPage3Fragment extends Fragment {

    private ArrayList<Integer> ids;
    private int pos = 0;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.curNum)
    TextView curNum;
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
    @BindView(R.id.main_chart)
    LineChart chart;

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
    private MainAvtivityHelper helper;//主要辅助定义UI Chart
    private ArrayList<Entry> values = new ArrayList<>();//chart数据
    private DetailsActivity activity;


    public DetailsPage3Fragment() {
        // Required empty public constructor
    }

    public static DetailsPage3Fragment newInstance(ArrayList<Integer> ids, int pos, int type) {
        DetailsPage3Fragment fragment = new DetailsPage3Fragment();
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
        View view = inflater.inflate(R.layout.fragment_details_page3, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {
        activity = ((DetailsActivity) Objects.requireNonNull(getActivity()));
        helper = new MainAvtivityHelper();

        models = DbDownUtil.getInstance().searchListBout(ids);
        if (type == 1)
            activity.curBout = spUtils.getInt(Constant.CUR_BOUT);
//        else
//            activity.curBout = models.size();


        curShoot = models.get(activity.curBout - 1);
        List<SingleShootDataModel> singleShootDataModels = curShoot.getData();
        curSingle = singleShootDataModels.get(pos);
        adapter = new DetailsRVAdapter(requireContext(), curShoot.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            List<SingleShootDataModel> data = curShoot.getData();
            curSingle = data.get(position);
            adapter.setPos(position);
            pos = position;
            setChartData(curSingle.getCheck());
            targetView(curSingle);

        });
        adapter.setPos(pos);
        recyclerView.scrollToPosition(pos);

        initChart();
        initClick();
    }

    private void initData() {

    }

    /**
     * 曲线图配置初始化
     */
    private void initChart() {
        helper.configLineChart(chart);
        setChartData(curSingle.getCheck());
        targetView(curSingle);
    }

    /**
     * 曲线图构建模拟数据
     */
    /**
     * 曲线图构建模拟数据
     */
    private void setChartData(List<EntryModel> data) {
        if (!data.isEmpty()) {
            values.clear();
            for (int i = 0; i < data.size(); i++) {
                if (i >= 10)//够10个取是个 不够有几个取几个
                    break;
                EntryModel model = data.get(i);
                values.add(new Entry(i, model.getRing()
                ));
            }
        }
        LineDataSet set;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(values);
            set.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set = new LineDataSet(values, "DataSet 1");


            // black lines and points
            set.setColor(Color.YELLOW);
            // line thickness and point size
            set.setLineWidth(2f);

            set.setDrawFilled(false);//是否绘制线下阴影
            set.setDrawCircles(false);//是否绘制数据上的原点


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set); // add the single_rounds sets

            // create a single_rounds object with the single_rounds sets
            LineData lineData = new LineData(dataSets);

            lineData.setDrawValues(false);//关闭绘制字体
            // set single_rounds
            chart.setData(lineData);
        }

        chart.invalidate();
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

            setChartData(curSingle.getCheck());
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

            setChartData(curSingle.getCheck());
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
                ((DetailsActivity) Objects.requireNonNull(getActivity())).initUi(activity.curBout - 1);
                curSingle = curShoot.getData().get(0);
                pos = 0;
                adapter.setPos(pos);
                recyclerView.scrollToPosition(pos);

                setChartData(curSingle.getCheck());
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
                ((DetailsActivity) Objects.requireNonNull(getActivity())).initUi(activity.curBout - 1);
                curSingle = curShoot.getData().get(0);
                pos = 0;
                adapter.setPos(pos);
                recyclerView.scrollToPosition(pos);
                setChartData(curSingle.getCheck());
                targetView(curSingle);
            }

        });
    }

    private void targetView(SingleShootDataModel model) {

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
}
