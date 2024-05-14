package org.ggxz.shoot.mvp.view.activity;

import static org.ggxz.shoot.mvp.view.activity.ConfigActivity.mPrinter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.utils.TimeUtils;
import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.EnterInfo;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.db.mode.ShootDataModel;
import com.example.common_module.db.mode.SingleShootDataModel;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.db.mode.UserShootReportData;
import com.example.common_module.utils.PopWindowUtil;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.utils.ToastUtils;
import com.example.common_module.utils.player.AudioPlayerHelper;
import com.example.net_module.helper.InitHelper;
import com.example.net_module.mode.InitMode;
import com.example.net_module.mode.InitModeData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.printsdk.PrintSerializable;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.MainAdapter;
import org.ggxz.shoot.adapter.OnItemClickListener;
import org.ggxz.shoot.bean.MainBean;
import org.ggxz.shoot.mvp.presenter.impl.MainPresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.MainView;
import org.ggxz.shoot.utils.LogUtils;
import org.ggxz.shoot.utils.RestartAPPTool;
import org.ggxz.shoot.widget.TargetPointView;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.stick.AbsStickPackageHelper;
import tp.xmaihh.serialport.utils.ByteUtil;

public class MainActivity extends BaseMvpActivity<MainPresenterImpl> implements MainView<MainBean> {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.main_chart)
    LineChart chart;
    @BindView(R.id.top_day)
    TextView top_day;
    @BindView(R.id.top_week)
    TextView top_week;
    @BindView(R.id.top_month)
    TextView top_month;
    @BindView(R.id.top_year)
    TextView top_year;

    @BindView((R.id.jumpConfigTv))
    TextView jumpConfigTv;

    @BindView(R.id.targetView)
    TargetPointView targetView;
    @BindView(R.id.topLayout)
    LinearLayout topLayout;
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
    @BindView(R.id.shootNum)
    TextView shootNum;
    @BindView(R.id.shootPerson)
    FrameLayout shootPersonLay;
    @BindView(R.id.topTitleBtn)
    TextView topTitleBtn;
    @BindView(R.id.jumpHistoryTv)
    TextView jumpHistoryTv;
    @BindView(R.id.setting)
    TextView setting;
    @BindView(R.id.userName)
    TextView tvUserName;
    @BindView(R.id.tvShootNum)
    TextView tvShootNum;
    @BindView(R.id.userCurBout)
    TextView userCurBout;
    @BindView(R.id.tvCurFaxu)
    TextView tvCurFaxu;
    @BindView(R.id.tvTotalRing)
    TextView tvTotalRing;
    @BindView(R.id.tvCurRing)
    TextView tvCurRing;
    DecimalFormat df = new DecimalFormat("#.#");
    private int totalBout = 1;//总局数
    private int curBout = 1;//当前局数
    private long curBoutId = -1;//当前局Id  云端返回
    private long curBoutKeyId = -1;//当前局Id 主键

    private final static Object object = new Object();

    private ShootDataModel curBoutMode;
    private int faxu = 1;//当前发序
    private long curFaxuId = -1;//当前发序Id
    private int totalFaxu = 10;//发序总数
    private long lastTime = 0;//每个发序的时间
    private float totalRing = 0.0F;
    private float curRing = 0.0F;
    private SingleShootDataModel curFaxu;//当前发序
    //这个计数目的是为了寻找开枪后的第五个点 来计算据枪的成绩的
    private int shootCount = Integer.MIN_VALUE;
    //    private EntryModel lastShootModel = null;//记录最近一次开枪点的信息
    private UserModel userModel;

    private SerialHelper serialHelper;
    private static final int PORT_TYPE = 0x05;

    private int state;
    private byte[] res = new byte[11];//数据拼接
    private float oldX = 1f, oldY = 1f;
    private PopupWindow popupWindow;
    private MainAdapter adapter;
    private List<EntryModel> targetData = new ArrayList<>();
    private List<Entry> chartData = new ArrayList<>();
    AtomicInteger index = new AtomicInteger(0);
    private SPUtils spUtils;
    private List<InitMode> initModes = new ArrayList<>();

    private MainAvtivityHelper helper;//主要辅助定义UI Chart
    private Runnable timeOutRunnable = null;
    //    EntryModel lastModel = null;//记录连续两次开抢
    AudioPlayerHelper audioPlayerHelper;
    private ComBean comBean;
    private boolean isGun92 = true;
    public static boolean isInitTargetSurface=false;
    private String lastHeartTime;

//    Timer timer = new Timer();
//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//            try {
//                if (lastHeartTime != null ){
//               isInitTargetSurface = !isTimeOut(lastHeartTime);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == PORT_TYPE) {//串口通信相关
                    comBean = (ComBean) msg.obj;
                    String t = comBean.sRecTime;
                    String rxText = ByteUtil.ByteArrToHex(comBean.bRec);
                    String text = "handleMessage Rx-> " + t + ": " + rxText + "\r" + "\n";
                    LogUtils.i(TAG, text);
                    if (rxText.contains("A504")){
                        lastHeartTime = t.substring(3,8);  //MM:ss
                        if (!isInitTargetSurface) ToastUtils.showToast("靶面连接成功！");
                        isInitTargetSurface = true;
                        // 除了靶面信息， 应该是心跳， 忽略。
                        return;
                    }


                    //state 表示当前 res[11]中已经存储到的字节有集合 note 这里的问题在于：读到新Head 后面A5不会再读 直到获取完整数据 或 检验失败清空之前存储的数据
                    //  A5 A5 0B 7E 01 01 01 FF 9C 01 9A 67
                    //  A5 0B 7E 03 A5 A5 0B 7E 01 01 01 FF 9C 01 9A 67 A5 0B 7E 01 01 01 FF 9C 01 9A 67-> A5 0B 7E 03 01 01 FF 9C 01 9A 67 正确吗？
                    for (int i = 0; i < comBean.bRec.length; i++) {
                        byte b = comBean.bRec[i];

                        switch (state) {
                            case 0:
                                if (b == (byte) 0xA5) {//帧头
                                    state = 1;
                                    res[0] = b;
                                    LogUtils.i(TAG, "state1");
                                }
                                LogUtils.i(TAG, "state1 finished");
                                break;
                            case 1:
                                if ((b == (byte) 0x0B) || (b == (byte) 0x06)) {//0B xy坐标数据 06 开抢数据
//                                isXyFlag = true;
                                    state = 2;
                                    res[1] = b;
                                    LogUtils.i(TAG, "state2");
                                } else {
//                                isXyFlag = false;
                                    state = 0;
                                    LogUtils.i(TAG, "state2-2, state=0");
                                }
                                LogUtils.i(TAG, "state2 finished");
                                break;
                            case 2:
                                if (b == (byte) 0x7E || b == (byte) 0xFE) {//胸环靶
                                    state = 3;
                                    res[2] = b;
                                    LogUtils.i(TAG, "state3-1");
                                } else if (b == (byte) 0x01) {//92式手枪
                                    isGun92 = true;
                                /*state = 3;
                                res[2] = b;*/
                                    state = 0;
                                    LogUtils.i(TAG, "state3-2");
                                } else {
                                    isGun92 = false;
                                    state = 0;
                                    LogUtils.i(TAG, "state3-3");
                                }
                                LogUtils.i(TAG, "state3 finished");
                                break;
                            case 3://ID 靶/枪 此时出现A5 怎么办？
                                state = 4;
                                res[3] = b;
                                LogUtils.i(TAG, "state4 finished");
                                break;
                            case 4://cmd
                                state = 5;
                                res[4] = b;
                                LogUtils.i(TAG, "state5 finished");
                                break;
                            case 5://
                                state = 6;
                                res[5] = b;
                                /*if (isXyFlag) {//坐标
                                    state = 6;
                                    res[5] = b;
                                } else {//开抢 如果是开抢 此时就应该要校验了
                                    state = 0;//重置初始状态 校验成功失败 都要重置 (难道要遍历re 从里面找有效Head 然后再嵌套边 swatch case？)
                                    byte sum = 0;
                                    for (int j = 0; j < 5; j++) {
                                        sum += res[j];
                                    }
                                    if (b == sum) {//校验成功
                                        long time = new Date().getTime();
                                        lastTime = time;
                                        EntryModel model = mPresenter.makeData(res);

                                        model.setTime(time);
                                        model.setUserId(spUtils.getLong(Constant.USER_ID));
                                        model.setUserStatus(0);
                                        model.setDeleteStatus(false);
                                        model.setSingleShootId(curFaxuId);
                                        DbDownUtil.getInstance().saveEntry(model);
                                        targetView(model);
                                        LogUtils.e(TAG, "Re-> success-开抢");
                                    } else {
                                        //校验失败 那么我该删除前面的数据嘛？
                                        Arrays.fill(res, (byte) 0);// 清空缓存数据 or 不用清空 state重置数据会覆盖掉
                                    }
                                }*/
                                LogUtils.i(TAG, "state6 finished");
                                break;
                            //break 不能却掉 否则无法正确更新state以及保证res数据的顺序
                            case 6:
                                state = 7;
                                res[6] = b;
                                LogUtils.i(TAG, "state7 finished");
                                break;
                            case 7:
                                state = 8;
                                res[7] = b;
                                LogUtils.i(TAG, "state8 finished");
                                break;
                            case 8:
                                state = 9;
                                res[8] = b;
                                LogUtils.i(TAG, "state9 finished");
                                break;
                            case 9:
                                state = 10;
                                res[9] = b;
                                LogUtils.i(TAG, "state10 finished");
                                break;
                            case 10:// 走到这一定是 坐标数据
                                byte sum = 0;
                                for (byte re : res) {
                                    sum += re;
                                }
                                if (b == sum) {
                                    LogUtils.i(TAG, "state10-1");
                                    long time = new Date().getTime();
//                              创建model 赋值res数据       EntryModel model = new EntryModel(); 存入数据库
                                    EntryModel model = mPresenter.makeData(res);
                                    model.setTime(time);
                                    model.setUserId(spUtils.getLong(Constant.USER_ID));
                                    model.setUserStatus(0);
                                    model.setDeleteStatus(false);
                                    model.setSingleShootId(curFaxuId);
                                    LogUtils.i("1", "计算环数完成， 环数 = " + model.getRing());
                                    if (lastTime == 0) {//当前发序的第一个点
                                        lastTime = time;
                                    }
                                    if (index.get() <= 5)
//                                    model.setStatus(Color.GREEN);//发序开始
                                        model.setStatus(Color.RED);//发序开始
                                    else {
                                        if (shootCount >= 0 && shootCount <= 5)
                                            model.setStatus(Color.BLUE);//开抢后
//                                        model.setStatus(Color.RED);//开抢后
                                        else
                                            model.setStatus(Color.RED);//正常轨迹
                                    }
                                    DbDownUtil.getInstance().saveEntry(model);
                                    LogUtils.i("2 saveEntry", "存 DB， model = " + model.toString());

                                    targetView(model);
                                    LogUtils.i("3 targetView", "model = " + model.toString());
                                    //hint 画折线
                                    chartData.add(new Entry(index.get(), Math.round(model.getRing() * 10F) / 10F));
                                    setChartData(chartData);
                                    LogUtils.i("4 setChartData", "model = " + model.toString());
                                    index.incrementAndGet();
                                    LogUtils.i("5 setChartData", "走到这一定是 坐标数据 Re-> success-点， index = " + index);
                                    LogUtils.i(TAG, "state10-1 结束");
                                } else {
                                    Arrays.fill(res, (byte) 0);
                                    LogUtils.e("Arrays.fill(res, (byte) 0);", "异常情况！！！");
                                    LogUtils.e(TAG, "准备设置为脱靶");
                                    LogUtils.i(TAG, "state10-2 结束");
                                }
                                state = 0;
                                LogUtils.i(TAG, "state all 结束");
                                break;
                        }
                    }
                    state = 0;
                }
            } catch (Exception e) {
                LogUtils.e("MultipleActivity- handleMessage error", "error = " + e.getMessage());
            }
        }
    };

    //todo 结束当前发序
    private void finishCurBout() {
        LogUtils.e("finishCurBout 结束当前发序", "TODO");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        spUtils = SPUtils.getInstance(getApplication());
        shootInit();
        //  开启串口  没有 sttys3 crash
        initSerialConfig();
        if (!serialHelper.isOpen()) {
            try {
                serialHelper.open();
                Toast.makeText(this, "串口打开成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "串口打开异常", Toast.LENGTH_SHORT).show();

            }
        }
        helper = new MainAvtivityHelper();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MainAdapter(this);
        recyclerView.setAdapter(adapter);


        setting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingDialogActivity.class);
            startActivity(intent);
        });
        jumpHistoryTv.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
        //jumpConfigTv.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ConfigActivity.class)));
        jumpConfigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestartAPPTool.restartAPP(getApplicationContext(),100);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                ArrayList<Integer> ids = new ArrayList<>();
                for (InitMode mode : initModes) {
                    ids.add((int) mode.bout_id);
                }
                intent.putExtra(DetailsActivity.INTENT_CLICK_POS_KEY, position);
                intent.putIntegerArrayListExtra(DetailsActivity.INTENT_KEY, ids);
                startActivity(intent);
            }
        });

        //todo 本地测试数据 读取 assets文件
//        List<String> fileLines = AssetFileReader.readAssetFile(getApplicationContext(), "file.txt");
//        AtomicInteger s = new AtomicInteger(0);
        shootNum.setOnClickListener(v -> {
//            byte[] bytes = ByteUtil.HexToByteArr(fileLines.get(s.get()).trim().replace(" ", ""));
//            ComBean bean = new ComBean("11", bytes, bytes.length);
//            Message message = handler.obtainMessage();
//            message.obj = bean;
//            message.what = PORT_TYPE;
//            handler.sendMessage(message);
//            s.set(s.get() + 1);
        });
        topTitleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopActivity.class);
            startActivity(intent);
        });

        initChart();

        //初始化人型靶面
        targetView.setRingCount(10); // 设置环数为10
        targetView.setBackgroundColor(Color.TRANSPARENT); // 设置背景色为灰色

        targetView.setXAxisRange(-10f, 10f);
        targetView.setYAxisRange(-10f, 10f);
        targetView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        //初始化打印机
        //mPrinter = new PrintSerializable();
        //mPrinter.open(EXTRA_SERIAL_NAME,EXTRA_SERIAL_PORT);

    }

    private void shootInit() {
        initModes = spUtils.getStringList(Constant.INIT_MODE_DATA, InitMode.class);

        LogUtils.i("------", initModes.toString());
        totalBout = spUtils.getInt(Constant.TOTAL_BOUT);
        curBout = spUtils.getInt(Constant.CUR_BOUT);
        curBoutId = initModes.get(curBout - 1).bout_id;
        userModel = DbDownUtil.getInstance().findUser(initModes.get(curBout - 1).user_id);


        faxu = spUtils.getInt(Constant.CUR_FAXU);
        totalFaxu = spUtils.getInt(Constant.BULLET_COUNT);


        userCurBout.setText(String.valueOf(initModes.get(curBout - 1).bout_num));
//        tvShootNum.setText(String.valueOf(initModes.get(curBout - 1).shoot_num));
        tvShootNum.setText(String.valueOf(initModes.get(curBout - 1).bout_num));
        tvUserName.setText(spUtils.getString(Constant.USER_NAME));

        tvCurFaxu.setText(String.valueOf(faxu));
        tvTotalRing.setText(df.format(totalRing));
        tvCurRing.setText(String.valueOf(curRing));
    }

    @Override
    protected void initData() {
        /*List<String> fileLines = AssetFileReader.readAssetFile(getApplicationContext(), "file.txt");

        for (int i = 0; i < fileLines.size(); i++) {
            byte[] bytes = ByteUtil.HexToByteArr(fileLines.get(i).trim().replace(" ", ""));
            ComBean bean = new ComBean("11", bytes, bytes.length);
            Message message = handler.obtainMessage();
            message.obj = bean;
            message.what = PORT_TYPE;
            handler.sendMessageDelayed(message, (i + 1) * 100);
        }*/

        top_year.setText(getUserName("year"));
        top_month.setText(getUserName("month"));
        top_week.setText(getUserName("week"));
        top_day.setText(getUserName("day"));

//        InitHelper.getTopTypeList(new NetCallBack<TopUser>() {
//            @Override
//            public void onResponseData(TopUser topUser) {
//                if (!topUser.day.isEmpty()) {
//                    top_day.setText(topUser.day.get(0).user_name);
//                }
//
//                if (!topUser.week.isEmpty()) {
//                    top_week.setText(topUser.week.get(0).user_name);
//                }
//
//                if (!topUser.month.isEmpty()) {
//                    top_month.setText(topUser.month.get(0).user_name);
//                }
//
//                if (!topUser.year.isEmpty()) {
//                    top_year.setText(topUser.year.get(0).user_name);
//                }
//            }
//
//            @Override
//            public void onError(String msg) {
//            }
//        });

    }

    private String getUserName(String dateType) {
        try {
            Date date = new Date();
            String dateStr = TimeUtils.date2String(date, "yyyy-MM-dd");
            long startMillions = 0;
            long endMillions = 0;
            //年时间计算
            if ("year".equals(dateType)) {
                String yearPrefix = dateStr.substring(0, dateStr.indexOf("-"));
                String yearStartDate = yearPrefix + "-01-01 00:00:00";
                String yearEndDate = yearPrefix + "-12-31 23:59:59";
                startMillions = TimeUtils.string2Millis(yearStartDate);
                endMillions = TimeUtils.string2Millis(yearEndDate);
            } else if ("month".equals(dateType)) {
                String mothPrefix = dateStr.substring(0, dateStr.lastIndexOf("-"));
                String mothStartDate = mothPrefix + "-01 00:00:00";
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(TimeUtils.string2Date(mothStartDate));
                calendar.add(Calendar.MONTH, 1);
                startMillions = TimeUtils.string2Millis(mothStartDate);
                endMillions = calendar.getTimeInMillis();
            } else if ("day".equals(dateType)) {
                String dayPrefix = dateStr;
                String dayStartDate = dayPrefix + " 00:00:00";
                String dayEndDate = dayPrefix + " 23:59:59";
                startMillions = TimeUtils.string2Millis(dayStartDate);
                endMillions = TimeUtils.string2Millis(dayEndDate);
            } else if ("week".equals(dateType)) {
                String dayPrefix = dateStr;
                String dayStartDate = dayPrefix + " 00:00:00";
                Date mondayDate = TimeUtils.string2Date(dayStartDate);
                // 获取指定日期所在周的第一天(周一)
                Calendar calendar = Calendar.getInstance();
                // 设置周一是第一天, getFirstDayOfWeek获取的默认是周日
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                // 设置指定日期
                calendar.setTime(mondayDate);
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                Date date1 = calendar.getTime();
                startMillions = date1.getTime();
                String dayEndDate = dayPrefix + " 23:59:59";
                endMillions = TimeUtils.string2Millis(dayEndDate);
            }
            //List<UserModel> userModelList = DbDownUtil.getInstance().findAllUserByTime(startMillions, endMillions);
            //List<UserShootReportData> userShootReportDataList = new ArrayList<>();
            List<ShootDataModel> shootDataModelList = DbDownUtil.getInstance().findAllShootDataModelByTime(startMillions, endMillions);
            Map<String, UserShootReportData> hashMap = new HashMap();
            for (int i = 0; i < shootDataModelList.size(); i++) {
                UserShootReportData userShootReportData = new UserShootReportData();

                //List<ShootDataModel> shootData = userModelList.get(i).getShootData();
                //long userId=userModelList.get(i).getUserId();
                ShootDataModel shootDataModel = shootDataModelList.get(i);
                String userName = shootDataModel.getUserName();
                if (hashMap.containsKey(userName)) {
                    userShootReportData = hashMap.get(userName);
                    Integer totalGrade;
                    Integer boutNum;
                    totalGrade = userShootReportData.getTotalGrade();
                    boutNum = userShootReportData.getTotalBout();
                    userShootReportData.setTotalBout(boutNum + 1);
                    //userShootReportData.setTotalGrade(totalGrade + shootDataModel.getTotalGrade());
                    userShootReportData.setTotalGrade(totalGrade + getTotalGradeEachShoot(shootDataModel));  //总体成绩累加，by ned
                    userShootReportData.setUserName(shootDataModel.getUserName());
                    userShootReportData.setUserId(shootDataModel.getUserId());
                    //userShootReportDataList.add(userShootReportData);

                } else {
                    Integer boutNum = 0;
                    //ToastUtils.showToast("####userId：" + shootDataModel.getUserId() + "名字：" + shootDataModel.getUserName() + "局数：" + boutNum);
                    userShootReportData.setTotalBout(boutNum + 1);
                    //userShootReportData.setTotalGrade(shootDataModel.getTotalGrade());
                    userShootReportData.setTotalGrade(getTotalGradeEachShoot(shootDataModel));
                    userShootReportData.setUserName(shootDataModel.getUserName());
                    userShootReportData.setUserId(shootDataModel.getUserId());
                    hashMap.put(userName, userShootReportData);
                }
            }
            //ToastUtils.showToast("名字："+userModel.getName()+"--局数："+boutNum);

            float averageGrade = 0;
            String userName = "未知用户";
            for (Map.Entry<String, UserShootReportData> entry : hashMap.entrySet()) {
                System.out.println("key=" + entry.getKey() + "  value=" + entry.getValue());
                UserShootReportData userShootReportData = entry.getValue();
                //ToastUtils.showToast("####userId：" + userShootReportData.getUserId() + "名字：" + userShootReportData.getUserName() + "局数：" + userShootReportData.getTotalBout());
                float nowAverageGrade = userShootReportData.getTotalBout() == 0 ? 0 : userShootReportData.getTotalGrade() / userShootReportData.getTotalBout();
                if (nowAverageGrade > averageGrade) {
                    averageGrade = nowAverageGrade;
                    userName = userShootReportData.getUserName();
                }
            }
//
//            for (UserShootReportData userShootReportData : userShootReportDataList) {
//                float nowAverageGrade = userShootReportData.getTotalBout() == 0 ? 0 : userShootReportData.getTotalGrade() / userShootReportData.getTotalBout();
//                if (nowAverageGrade > averageGrade) {
//                    averageGrade = nowAverageGrade;
//                    userName = userShootReportData.getUserName();
//                }
//            }
            return userName;
        } catch (Exception e) {
            ToastUtils.showToast(e.toString());
        }
        return null;
    }

    public void targetView(EntryModel model) {

        /*填充局内容 之前是进来就创建局信息 现在是第一个激光 开始去检查当前局是否存在*/
        if (curBoutMode == null) {
            curBoutKeyId = combineShootData();
            curFaxu = new SingleShootDataModel();
            curFaxu.setBoutId(curBoutId);
            curFaxu.setShootDataId(curBoutKeyId);
            curFaxuId = DbDownUtil.getInstance().insert(curFaxu);
        }

        if (curFaxu.getGrade() == 0) {
            gradeProgressBar.setProgress(0);
            gradeCount.setText("0");
            collimationProgressBar.setProgress(0);
            collimationCount.setText("0");
            totalProgressBar.setProgress(0);
            totalCount.setText("0");
            gunProgressBar.setProgress(0);
            gunCount.setText("0");
            sendProgressBar.setProgress(0);
            sendCount.setText("0");

        }

        shootCount++;
        targetData.add(model);
        targetView.setValues(targetData);

        //note 2.据枪逻辑
        if (shootCount > 0 && shootCount <= 5) {
//            shootCount = Integer.MIN_VALUE; 兼容点不够的情况
            model.setStatus(Color.BLUE);
            DbDownUtil.getInstance().updateEntry(model);
            // 前循环 shootCount等于5保证了一定能向前找到5个数据
            // note 前循环其实从开枪后开始计算的 所以这个前循环其实是从开枪开始向后找5个数据
            List<Entry> rifleList = new ArrayList<>();
            rifleList.add(new Entry(model.getX(), model.getY()));
            //取后面一个点的效果
//            EntryModel entryModel = list.get(list.size() - 5);
//            rifleList.add(new Entry(entryModel.getX(), entryModel.getY()));
            if ((model.getCmdType() == EnterInfo.CMD_TYPE.SHOOT || shootCount == 5)) {
                if (timeOutRunnable != null) {
                    handler.removeCallbacks(timeOutRunnable);
                }
//                EntryModel tempModel = null;
//                if (lastModel != null && lastModel.getCmdType() == EnterInfo.CMD_TYPE.SHOOT && shootCount == 1) {//note 连续2次开抢 就自己构建一个数据 在发送一遍
//                    tempModel = model;
//                    model = lastModel;//构建上一个model 当前model 则会在最后递归一次
//                }
                boolean isMiss = model.getMiss();
                float difference = mPresenter.getMaxDistance(new Entry(model.getX(), model.getY()), new Entry(model.getX(), model.getY()));
                int gun = isMiss ? Math.round(100F - difference * 100F) : 0;
                gunProgressBar.setProgress(gun < 0 ? 0 : gun);
                gunCount.setText(String.valueOf(gun < 0 ? 0 : gun));

                //4.击发
                Entry entry = rifleList.get(rifleList.size() - 1);
                float sendDifference = mPresenter.getDistance(new Entry(model.getX(), model.getY()), entry);
                int collimation = isMiss ? Math.round(100F - sendDifference * 100F) : 0;
                sendProgressBar.setProgress(collimation < 0 ? 0 : collimation);
                sendCount.setText(String.valueOf(collimation < 0 ? 0 : collimation));
                //5.note  总分 所有的评分 应在上面都结算后计算 所以放在这里
                //float total = (gradeProgressBar.getProgress() + gunProgressBar.getProgress()
                        //+ collimationProgressBar.getProgress() + sendProgressBar.getProgress()) / 4F;
                float total = (float) (gradeProgressBar.getProgress() * 0.6 + gunProgressBar.getProgress() * 0.1
                                        + collimationProgressBar.getProgress() * 0.15+ sendProgressBar.getProgress() * 0.15);
                int avgTotal = isMiss ? Math.round(total) : 0;
                totalProgressBar.setProgress(avgTotal);
                totalCount.setText(String.valueOf(avgTotal));

                if (faxu == 1) adapter.setData(new ArrayList<>()); //第一发则清空recycleview
                /*构建当前发序*/
                SingleShootDataModel shootDataModel = curFaxu;
                shootDataModel.setShootPreface(faxu++);
                //用时保留一位小树
                shootDataModel.setTimeSecond(lastTime == 0 ? 0 : ((model.getTime() - lastTime) / 1000F));
                shootDataModel.setTimeMill(model.getTime());
                shootDataModel.setShoot(gun < 0 ? 0 : gun);
                shootDataModel.setSend(collimation < 0 ? 0 : collimation);
                shootDataModel.setAllGrade(avgTotal);
                shootDataModel.update();
                shootDataModel.getCheck();
                InitHelper.postShootData(shootDataModel);
                lastTime = 0;//时间还原
                /*构建当前发序*/


                //更新射击列表
                List<SingleShootDataModel> data = adapter.getData();
                data.add(shootDataModel);

                adapter.setData(data);
                recyclerView.scrollToPosition(data.size() - 1);
                //创建下一个发序


                ShootDataModel boutMode = this.curBoutMode;
                boutMode.setCurrentSeq(faxu);
                boutMode.setCurrentRing(curRing);
                boutMode.setTotalRing(totalRing);
                boutMode.setTotalGrade(curFaxu.getGrade());
                boutMode.setTotalShoot(gun < 0 ? 0 : gun);
                boutMode.setTotalCollimation(curFaxu.getCollimation());
                boutMode.setTotalSend(curFaxu.getSend());
                boutMode.setTotalAll(avgTotal);
                boutMode.update();
                /*填充局内容*/

                if (faxu > totalFaxu) {//发序相等说明下一局 开始 没有下一局 则关闭接受数据
                    if (curBout < totalBout) {//如果还存在下一局
                        //adapter.setData(new ArrayList<>());
                        curBout += 1;
                        InitMode mode = initModes.get(curBout - 1);
                        curBoutId = mode.bout_id;

                        curBoutKeyId = combineShootData();

                        spUtils.put(Constant.CUR_BOUT, curBout);
                        userCurBout.setText(String.valueOf(curBout));
                        tvShootNum.setText(String.valueOf(initModes.get(curBout - 1).shoot_num));
                        faxu = 1;
                        spUtils.put(Constant.CUR_FAXU, faxu);
                        curFaxu = new SingleShootDataModel();
                        curFaxu.setShootDataId(curBoutKeyId);
                        curFaxu.setBoutId(curBoutId);
                        curFaxuId = DbDownUtil.getInstance().insert(curFaxu);
                        tvCurFaxu.setText(String.valueOf(faxu));

                        curRing = 0.0F;
                        tvCurRing.setText(String.valueOf(curRing));
                        totalRing = 0.0F;
                        tvTotalRing.setText(df.format(totalRing));
                        finishCurBout();
                        print(boutMode);
                    } else {//显示结束按钮 回到初始页面
                        handler.removeCallbacksAndMessages(null);
                        //射击结束
                        jumpConfigTv.setVisibility(View.VISIBLE);
                        ToastUtils.showToast("射击结束");
                        print(boutMode);
                        initData();
                        if (serialHelper != null) {
                            serialHelper.close();
                            serialHelper = null;
                        }
                        return;
                    }
                } else {//下一个发序
                    spUtils.put(Constant.CUR_FAXU, faxu);
                    curFaxu = new SingleShootDataModel();
                    curFaxu.setShootDataId(curBoutKeyId);
                    curFaxu.setBoutId(curBoutId);
                    curFaxuId = DbDownUtil.getInstance().insert(curFaxu);
                    tvCurFaxu.setText(String.valueOf(faxu));
                    tvTotalRing.setText(df.format(totalRing));

                }

                index.set(0);//还原
                //清空环靶图
                targetData.clear();
                chartData.clear();
                setChartData(chartData);
//                if (lastModel != null && tempModel != null && lastModel.getCmdType() == EnterInfo.CMD_TYPE.SHOOT) {//note 连续2次开抢 就自己构建一个数据 在发送一遍
//                    shootCount = 4;
//                    targetView(tempModel);//这里存在问题 就是 上一个数据构建利用 的是当前的model 当是 同时在坐标点 并且 连续开抢 如果存在中级 则小于50ms 影响不打
//                    lastModel = null;
//                }
            }


        }
        if (model.getCmdType() == EnterInfo.CMD_TYPE.SHOOT) {
            boolean isMiss = model.getMiss();
//            model.setStatus(Color.YELLOW);
//            DbDownUtil.getInstance().updateEntry(model);

            // note 1. 成绩  根据板子的逻辑成绩不可能少于3.6环  所以评分少于3.6等于0分
            int grade = isMiss ? (Math.round(model.getRing() * 10F)) : 0;
            gradeProgressBar.setProgress(grade < 36 ? 0 : grade - 10);
            gradeCount.setText(String.valueOf(grade < 36 ? 0 : (grade - 10)));
            curRing = (grade == 0 ? 0 : grade / 10F);

            totalRing = totalRing + curRing;
            tvCurRing.setText(String.valueOf(curRing));
            tvTotalRing.setText(df.format(totalRing));
            shootCount = 0;
            curFaxu.setGrade(grade < 36 ? 0 : grade -10 );
            //note 3.瞄准 确保向前找第五个点不会越界
            if (!targetData.isEmpty()) {
                int count = 0;
                int collimation = 0;
                for (int j = targetData.size() - 2; j > 0; j--) {//-2 才是下标
                    if (count >= 5)
                        break;
                    EntryModel entryModel = targetData.get(j);
                    entryModel.setStatus(Color.YELLOW);
                    DbDownUtil.getInstance().updateEntry(entryModel);
                    float difference = mPresenter.getMaxDistance(new Entry(model.getX(), model.getY()), new Entry(entryModel.getX(), entryModel.getY()));
                    collimation = isMiss ? Math.round(100F - difference * 100F) : 0;
                    count++;
                }
                DbDownUtil.getInstance().updateEntry(model);
                collimationProgressBar.setProgress(collimation < 0 ? 0 : collimation);
                collimationCount.setText(String.valueOf(collimation < 0 ? 0 : collimation));
                curFaxu.setCollimation(collimation < 0 ? 0 : collimation);
//            } else {
//                // TODO 无点的时候 处理 置为脱靶；
//                int collimation = 0;
//                EntryModel entryModel = new EntryModel();
//                DbDownUtil.getInstance().updateEntry(entryModel);
//                DbDownUtil.getInstance().updateEntry(model);
//                collimationProgressBar.setProgress(0);
//                collimationCount.setText(String.valueOf(0));
//                curFaxu.setCollimation(collimation);
            }

            curFaxu.setDirection(model.getRing() != 0 ? mPresenter.getEightWay(model.getX(), model.getY()) : "脱靶");
            if (audioPlayerHelper != null)
                audioPlayerHelper.play(String.valueOf(curRing), curFaxu.getDirection(), isGun92);
            curFaxu.setUserName(spUtils.getString(Constant.USER_NAME));
            curFaxu.setUserId(spUtils.getLong(Constant.USER_ID));
            curFaxu.setBoutId(curBoutId);
            curFaxu.setBoutNum(curBoutMode.getBoutNum());
            curFaxu.setRing(curRing);
            curFaxu.update();

            if (!isMiss) {
                model.setGunInAndMiss(0);//todo model相同对象 导致无法画出弹孔 不提先不改了
                shootCount = 4;
                targetView(model);
            } else {
                //hint 走到这就说明是有环数 非脱靶的逻辑 就要开启定时了 如果走上面的递归是不需要定时的 已经存在处理了
                timeOutRunnable = new TimeOutRunnable();
                handler.postDelayed(timeOutRunnable, 1000);//
//                lastModel = new EntryModel(model.getId(), model.getHead(), model.getLen(), model.getType(),
//                        model.getDeviceID(), model.getCmd(), model.getTime(), model.getX(), model.getY(),
//                        model.getGunId(), model.getRing(), model.getCheckVerify(), model.getSingleShootId(),
//                        model.getUserId(), model.getUserStatus(), model.getDeleteStatus(),
//                        model.getGunInAndMiss(), model.getMiss(), model.getStatus()
//                );//记录连续两次开抢

//                lastModel = model;

            }
        }

    }

    public long combineShootData() {

        curBoutMode = new ShootDataModel();
        curBoutMode.setShootNum(initModes.get(curBout - 1).shoot_num);
        curBoutMode.setBoutId(curBoutId);
        curBoutMode.setCreateTime(new Date().getTime());
        curBoutMode.setCurrentSeq(1);
        curBoutMode.setCurrentRing(0);
        curBoutMode.setTotalRing(0);
        curBoutMode.setTotalGrade(0);
        curBoutMode.setTotalShoot(0);
        curBoutMode.setTotalCollimation(0);
        curBoutMode.setTotalAll(0);
        curBoutMode.setUserId(initModes.get(curBout - 1).user_id);
        curBoutMode.setUserName(initModes.get(curBout - 1).user_name);
        curBoutMode.setBoutNum(initModes.get(curBout - 1).bout_num);
        curBoutMode.setUserIdKey(userModel.getId());
        return DbDownUtil.getInstance().insert(curBoutMode);

    }

    /**
     * 曲线图配置初始化
     */
    private void initChart() {
        helper.configLineChart(chart);
        chartData.add(new Entry(0, 0));//初始化图标
        setChartData(chartData);
        chartData.clear();
    }

    /**
     * 曲线图构建模拟数据
     */
    private void setChartData(List<Entry> values) {
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
            LineData data = new LineData(dataSets);

            data.setDrawValues(false);//关闭绘制字体
            // set single_rounds
            chart.setData(data);
        }

        chart.invalidate();
    }

    @Override
    protected MainPresenterImpl initInjector() {
        return new MainPresenterImpl();
    }

    /**
     * 端口通信
     */
    private void initSerialConfig() {
        //初始化SerialHelper对象，设定串口名称和波特率
        final long[] lastTime = {System.currentTimeMillis()};
        serialHelper = new SerialHelper(Constant.SPORT_NAME, Constant.BAUD_RATE) {
            @Override
            protected void onDataReceived(ComBean paramComBean) {

                long now = System.currentTimeMillis();
                long interval = now - lastTime[0];
                lastTime[0] = now;
                if (paramComBean.bRec.length == 11)
                    LogUtils.i("paramComBean.bRec.length, Time ->", interval + "");
                    String t = paramComBean.sRecTime;
                    String rxText = ByteUtil.ByteArrToHex(paramComBean.bRec);
                    String text = "onDataReceived Rx-> " + t + ": " + rxText + "\r" + "\n";
                    LogUtils.i("onDataReceived", text);
                Message message = handler.obtainMessage();
                message.obj = paramComBean;
                message.what = PORT_TYPE;
                handler.sendMessage(message);
            }
        };

        /*
         * 默认的BaseStickPackageHelper将接收的数据扩展成64位，一般用不到这么多位
         * 我这里重新设定一个自适应数据位数的
         */

        serialHelper.setStickPackageHelper(new AbsStickPackageHelper() {
            @Override
            public byte[] execute(InputStream is) {
                try {
                    int available = is.available();
                    if (available > 0) {
                        byte[] buffer = new byte[available];
                        int size = is.read(buffer);
                        if (size > 0) {
                            return buffer;
                        }
                    } else {
                        SystemClock.sleep(50);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }


    @Deprecated//展示取消缩放
    public void initPopupWindow() {
        //PopupWindow
        View popup_view = getLayoutInflater().inflate(R.layout.popupwindow_layout, null);
        TextView mile100 = popup_view.findViewById(R.id.shootNum_100);
        TextView mile50 = popup_view.findViewById(R.id.shootNum_50);
        TextView mile25 = popup_view.findViewById(R.id.shootNum_25);
        mile100.setOnClickListener(v -> {
            shootNum.setText("  1 0 0 米 胸 环 靶");
            ScaleAnimation animation = new ScaleAnimation(oldX, 0.5f, oldY, 0.5f, 200f, 200f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            shootPersonLay.startAnimation(animation);
            popupWindow.dismiss();
            oldX = 0.5f;
            oldY = 0.5f;

        });
        mile25.setOnClickListener(v -> {
            shootNum.setText("  2 5 米 胸 环 靶");
            ScaleAnimation animation = new ScaleAnimation(oldX, 1f, oldY, 1f, 200f, 200f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            shootPersonLay.startAnimation(animation);
            popupWindow.dismiss();
            oldX = 1f;
            oldY = 1f;

        });
        mile50.setOnClickListener(v -> {
            shootNum.setText("  5 0 米 胸 环 靶");
            ScaleAnimation animation = new ScaleAnimation(oldX, 0.75f, oldY, 0.75f, 200f, 200f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            shootPersonLay.startAnimation(animation);
            popupWindow.dismiss();
            oldX = 0.75f;
            oldY = 0.75f;
        });
        shootNum.setOnClickListener(v -> popupWindow = PopWindowUtil.getInstance()
                .makePopupWindow(MainActivity.this, shootNum, popup_view, R.color.main_style_blue)
                .showLocationWithAnimation(MainActivity.this, shootNum, 0, 0, -1));
        //PopupWindow end

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serialHelper != null) {
            serialHelper.close();
            serialHelper = null;
        }

        //数据和超时回调移除  避免泄漏
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onUserInformationDataSuccess(InitModeData initModeData) {

    }

    @Override
    public void onStart() {
        super.onStart();
       // if (timer != null ) timer.schedule(task,0,40000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioPlayerHelper = new AudioPlayerHelper(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
       // if (timer != null) timer.cancel();
        if (audioPlayerHelper != null)
            audioPlayerHelper.release();
    }

    class TimeOutRunnable implements Runnable {

        public TimeOutRunnable() {
        }

        @Override
        public void run() {
//            res[5] = (byte) (res[5] & 0x0F);
//            int sum = 0;
//            for (byte re : res) {
//                sum += re;
//            }
//            res[10] = (byte) sum;
//
//            LogUtils.e("--->", ByteUtil.ByteArrToHex(res));
//            ComBean bean = new ComBean(serialHelper.getPort(), res, res.length);
//
//            Message message = handler.obtainMessage();
//            message.obj = bean;
//            message.what = PORT_TYPE;
//            handler.sendMessage(message);
            shootCount = 4;
            try {
                targetView(targetData.get(targetData.size() - 1));
                if (targetData.size() < 5) {
                    LogUtils.i("targetData size < 5, size = ", targetData.size());
                    LogUtils.i("targetData：", targetData.toString());
                }
            } catch (Exception e){
                LogUtils.e("e TimeOutRunnable 出错：", e.getMessage());
                LogUtils.e("e targetData：", targetData.toString());
                LogUtils.e("e targetData size：", targetData.size());
                try {
                    LogUtils.e("e entryModel String: ", targetData.toString());
                    LogUtils.e("e targetData size：", targetData.size());
                    LogUtils.e("e entryModel String: ", targetData.get(targetData.size()).toString());
                    targetView(targetData.get(targetData.size()));
                } catch (Exception e2){
                    LogUtils.e("e2 TimeOutRunnable 出错：", e2.getMessage());
                    LogUtils.e("e2 targetData：", targetData.toString());
                    LogUtils.e("e2 targetData size：", targetData.size());
                    LogUtils.e("e2 targetView(null) ", "targetView(null)");
                    // TODO 分析原因， 写入真实射击信息；
                    EntryModel entryModel = new EntryModel();
                    entryModel.setRing(6.0f);
                    targetView(entryModel);
                }
            }
            handler.removeCallbacks(this);
        }
    }

    //打印每局,每局中每发的数
    public void print(ShootDataModel model) {
        if (!spUtils.getBoolean("auto_print", false)) return;
        if (mPrinter.getState() != PrintSerializable.CONN_SUCCESS) {
            ToastUtils.showToast("打印机状态有问题，请重新初始化");
            return;
        }
        mPrinter.init();
        // 设置字体
        // mWidth       -- 字体宽度    0-7
        // mHeight      -- 字体高度    0-7
        // mBold        -- 是否粗体    0,1
        // mUnderline   -- 是否下划线  0,1
//        Bitmap bitmap1 = null;
//        try
//        {
//            //从资源中打开图片 android.png。公司商标
//            bitmap1 = BitmapFactory.decodeStream(this.getResources().getAssets().open("android.png"));
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//            return;
//        }
        if (model != null) {
            List<SingleShootDataModel> singleShootDataModels = model.getData();
            Integer shootNum = 0;
            Integer notShootNum = 0;
            Integer shootTotalNum = 0;
            float timeSeconds = 0;
            int totalAllGrade = 0;
            if (singleShootDataModels.size() > 0) {
                shootTotalNum = singleShootDataModels.size();
            }
            for (SingleShootDataModel singleShootDataModel : singleShootDataModels) {
                //mPrinter.printText("  "+singleShootDataModel.getShootPreface() + "          " + singleShootDataModel.getRing() + "         " + singleShootDataModel.getTimeSecond());
                if (singleShootDataModel.getRing() > 0) {
                    shootNum++;
                } else {
                    notShootNum++;
                }
                timeSeconds = timeSeconds + singleShootDataModel.getTimeSecond();
                totalAllGrade = totalAllGrade + singleShootDataModel.getAllGrade();
            }

            mPrinter.setFont(0, 0, 1, 0);
            //mPrinter.printImage(bitmap1);              //打印图片
            mPrinter.setAlign(PrintSerializable.ALIGN_CENTER);
            mPrinter.printText("晟  达");
            mPrinter.wrapLines(1);
            mPrinter.setFont(0, 0, 1, 0);
            mPrinter.setAlign(PrintSerializable.ALIGN_CENTER);
            mPrinter.printText("S H E N G D A");
            mPrinter.wrapLines(1);
            mPrinter.wrapLines(1);
            mPrinter.setFont(0, 0, 0, 0);
            mPrinter.setAlign(PrintSerializable.ALIGN_CENTER);
            mPrinter.printText("模拟射击系统成绩单");
            mPrinter.wrapLines(1);
            mPrinter.setAlign(PrintSerializable.ALIGN_CENTER);
            mPrinter.printText("------------------------");
            mPrinter.wrapLines(1);
            mPrinter.setAlign(PrintSerializable.ALIGN_LEFT);
            mPrinter.printText("姓名：" + model.getUserName());
            mPrinter.wrapLines(1);
            mPrinter.printText("靶型：胸环靶");
            mPrinter.wrapLines(1);
            mPrinter.setAlign(PrintSerializable.ALIGN_LEFT);
            mPrinter.printText("打印时间：" + TimeUtils.millis2String(System.currentTimeMillis()));
            mPrinter.wrapLines(1);
            mPrinter.setAlign(PrintSerializable.ALIGN_LEFT);
            mPrinter.printText("总发数：" + shootTotalNum);
            mPrinter.wrapLines(1);
            mPrinter.printText("中靶数：" + shootNum);
            mPrinter.wrapLines(1);
            mPrinter.printText("脱靶数：" + notShootNum);
            mPrinter.wrapLines(1);
            mPrinter.printText("总环数：" + df.format(model.getTotalRing()));
            mPrinter.wrapLines(1);
            mPrinter.printText("均环数：" + df.format(model.getTotalRing() / shootTotalNum));
            mPrinter.wrapLines(1);
            mPrinter.printText("总用时：" + getTimeStr(timeSeconds));
            mPrinter.wrapLines(1);
            mPrinter.printText("平均分：" + Math.round(totalAllGrade / (float)shootTotalNum));
            mPrinter.wrapLines(1);
            mPrinter.printText("-------------------------------");
            mPrinter.wrapLines(1);
            mPrinter.printText("签字区：");
            mPrinter.wrapLines(2);
            mPrinter.printText("-------------------------------");
            mPrinter.wrapLines(1);
            mPrinter.setAlign(PrintSerializable.ALIGN_CENTER);
            mPrinter.printText(" 感谢您的使用！");
            mPrinter.wrapLines(1);
            mPrinter.setAlign(PrintSerializable.ALIGN_CENTER);
            mPrinter.printText("晟达启航-提供技术支持");
            mPrinter.wrapLines(3);
            mPrinter.printText("");
        } else {
            ToastUtils.showToast("打印没有射击数据");
        }
    }

    private String getTimeStr(float timeSecond) {
        String timeStr;
        long hours = TimeUnit.SECONDS.toHours((long) timeSecond);
        long minutes = TimeUnit.SECONDS.toMinutes((long) timeSecond) % 60;
        long remainingSeconds = (long) (timeSecond % 60);
        timeStr = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (remainingSeconds < 10 ? "0" + remainingSeconds : remainingSeconds);
        return timeStr;
    }

    private boolean isTimeOut(String lastHeartCheck){
        try{
            DateFormat dateFormat = new SimpleDateFormat("2024-01-01 00:mm:ss");
            String sNow  = dateFormat.format(new Date());
            Date mNow = dateFormat.parse(sNow);
            String yesterday = "2024-01-01 00:"+ lastHeartCheck;  //串口返回12小时格式，所以只取分和秒
            Date yesterdayDate = dateFormat.parse(yesterday);
            long yesterdayDateTime = yesterdayDate.getTime();
            long nowDateTime = mNow.getTime();//当前时间戳
            int result = (int) (nowDateTime - yesterdayDateTime);//毫秒
            int diffSecond = result / 1000; //1000毫秒等于1秒
            if(isInitTargetSurface && diffSecond>30 ) ToastUtils.showToast("靶面断开！");
            //ToastUtils.showToast(sNow+"-"+yesterday+"-"+String.valueOf(diffSecond)+"&&&&&&&&&&"+String.valueOf(nowDateTime)+"-"+String.valueOf(yesterdayDateTime));
            return diffSecond > 30;
        }catch (Exception e){
            return false;
        }
    }

    private int getTotalGradeEachShoot(ShootDataModel shootDataModel){
        int totalShootAllGarde = 0;
        List<SingleShootDataModel> singleShootDataModelList = shootDataModel.getData();
        if (singleShootDataModelList!= null && singleShootDataModelList.size()>0){
            for(SingleShootDataModel singleShootDataModel: singleShootDataModelList){
                totalShootAllGarde = totalShootAllGarde + singleShootDataModel.getAllGrade();
            }
            return  Math.round(totalShootAllGarde / (float)singleShootDataModelList.size());
        } else {
            return totalShootAllGarde;
        }

    }
}