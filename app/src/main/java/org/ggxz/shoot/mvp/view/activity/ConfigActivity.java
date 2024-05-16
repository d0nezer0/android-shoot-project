package org.ggxz.shoot.mvp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.mode.ConfigDataModel;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.utils.ToastUtils;
import com.example.net_module.Common;
import com.example.net_module.callback.NetCallBack;
import com.example.net_module.helper.InitHelper;
import com.example.net_module.mode.InitMode;
import com.example.net_module.mode.InitModeData;
import com.google.gson.Gson;
import com.printsdk.PrintSerializable;

import org.ggxz.shoot.R;
import org.ggxz.shoot.handler.CrashHandler;
import org.ggxz.shoot.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.utils.ByteUtil;

/**
 * 本地启动的时候需要注释下面的一些初始化方法。
 */
public class ConfigActivity extends AppCompatActivity {
    ArrayAdapter<Integer> arrayAdapter;
    private Integer[] spinnerArray = new Integer[]{5, 10, 15, 20, 25, 30}; //发数数组
    TextView test;
    ImageView vBattery;
    ImageView vMainBattery;

    Spinner spinner; //发数下拉控件
    TextView start;
    EditText ip;
    EditText userNameEt;
    EditText num;
    EditText numEt;
    EditText bootNumEt;//局数
    LinearLayout ll_system;
    LinearLayout ll_single;
    LinearLayout ll_free;
    LinearLayout ll_gunNum;

    TextView start_free;
    TextView start_system;
    TextView start_single;
    SPUtils utils;
    public static SerialHelper serialHelper;

    private String sendData;

    private String ipEdit = "";
    private String numEdit = "";


    private int shootType = 1;//1 单人模式 2 系统模式 3自由模式  1-2 进相同页面
    //打印机
    public static PrintSerializable mPrinter;
    public static String EXTRA_SERIAL_PORT = "9600";
    public static String EXTRA_SERIAL_NAME = "/dev/ttyS2";
    public static boolean isGunInit=false;
    public static boolean isInitSerialPort=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        setContentView(R.layout.activity_config);
        CrashHandler.getInstance().init(this);
    }



    /**
     * 刷新状态栏电量控件
     */
//    private void refreshStatus(){
//        BatteryManager batteryManager=(BatteryManager)getSystemService(BATTERY_SERVICE); //获取电量百分比
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //判断安卓版本
//            int batteryCapacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//            //Random rand = new Random();
//            //int batteryCapacity = rand.nextInt(100) + 1;
//            if (batteryCapacity>75)  {
//                vBattery.setImageResource(R.drawable.dianliang4);
//                vMainBattery.setImageResource(R.drawable.dianliang4);
//            }
//            if (batteryCapacity>50 && batteryCapacity<= 75)  {
//                vBattery.setImageResource(R.drawable.dianliang3);
//                vMainBattery.setImageResource(R.drawable.dianliang3);
//            }
//            if (batteryCapacity>25 && batteryCapacity<= 50)  {
//                vBattery.setImageResource(R.drawable.dianliang2);
//                vMainBattery.setImageResource(R.drawable.dianliang2);
//            }
//            if (batteryCapacity>3 && batteryCapacity<=25)  {
//                vBattery.setImageResource(R.drawable.dianliang1);
//                vMainBattery.setImageResource(R.drawable.dianliang1);
//            }
//            if (batteryCapacity<=3)  {
//                vBattery.setImageResource(R.drawable.dianliang0);
//                vMainBattery.setImageResource(R.drawable.dianliang0);
//            }
//        }
//    }
    /**
     * 从数据库读取上一次配置
     */
    private void initConfigData(){
        numEt = findViewById(R.id.numEt); //组号
        userNameEt = findViewById(R.id.userNameEt); //用户名
        bootNumEt = findViewById(R.id.bootNumEt); //局数
        spinner = findViewById(R.id.spinner_shootNumber); //发数
        arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.custom_spinner_item, spinnerArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_stytle);
        spinner.setAdapter(arrayAdapter);

        List<ConfigDataModel> configDataModelList = new ArrayList<>();
        configDataModelList = DbDownUtil.getInstance().findAllConfigData();
        if (configDataModelList != null && configDataModelList.size()>0){
            numEt.setText(configDataModelList.get(0).getGroup());
            userNameEt.setText(configDataModelList.get(0).getName());
            bootNumEt.setText(configDataModelList.get(0).getTotalBout());
            try {
                spinner.setSelection(getIndex(spinnerArray,Integer.parseInt(configDataModelList.get(0).getShootNum())),true);
            } catch (Exception e) {
                LogUtils.e(String.format("configDataMode.size() = %s", configDataModelList.size()), "分析错误内容");
                for (int i=0; i < configDataModelList.size(); i++) {
                    LogUtils.e(String.format("configDataMode %s", i), configDataModelList.get(i).toString());
                }
                LogUtils.e(String.format("init configDataModelList error, configDataModelList = %s", configDataModelList), e.getMessage());
            }
        }else {
            spinner.setSelection(1, true);
        }
    }
    private void initPermission() {
        //手动申请权限,视频音频权限为同一个
        if (ContextCompat.checkSelfPermission(ConfigActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConfigActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//权限返回码为1
        } else {
            //权限通过
        }

    }

    private void initSerial() {
        serialHelper = new SerialHelper(Constant.SPORT_NAME, Constant.BAUD_RATE) {
            @Override
            protected void onDataReceived(ComBean paramComBean) {
                Message message = handler.obtainMessage();
                message.obj = paramComBean;
                handler.sendMessage(message);
            }
        };

        if (!serialHelper.isOpen()) {
            try {
                serialHelper.open();
                isInitSerialPort=true;
                Toast.makeText(this, "串口打开成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                isInitSerialPort=false;
                e.printStackTrace();
                Toast.makeText(this, "串口打开异常", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void initPrinter(){
        mPrinter = new PrintSerializable();
        mPrinter.open(EXTRA_SERIAL_NAME, EXTRA_SERIAL_PORT);
        mHandler.obtainMessage(mPrinter.getState()).sendToTarget();
    }

    //用于接受连接状态消息的 Handler
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrintSerializable.CONN_SUCCESS:
                    ToastUtils.showToast("打印机连接成功");
                    break;
                case PrintSerializable.CONN_FAILED:
                    ToastUtils.showToast("打印机连接失败");
                    break;
                case PrintSerializable.CONN_CLOSED:
                    ToastUtils.showToast("打印机设备关闭");
                default:
                    break;
            }
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dealMsg(msg);
        }
    };

    /*
    系统上的几种射击方式选择；
     */
    private void dealMsg(Message msg) {
        ComBean comBean = (ComBean) msg.obj;
        String rxText = ByteUtil.ByteArrToHex(comBean.bRec);
        String type = rxText.substring(4, 6);
        String t = comBean.sRecTime;
        String a = ByteUtil.ByteArrToHex(comBean.bRec);
        String text = "Rx-> " + t + ": " + a + "\r" + "\n";
        LogUtils.i("TAG", text);
        if (type.equalsIgnoreCase("7F")) {
            isGunInit=true;
            ToastUtils.showToast("枪配网成功");
            if (shootType == 2) {
                ToastUtils.showToast("系统射击");
                InitHelper.init(new NetCallBack<InitModeData>() {
                    @Override
                    public void onResponseData(InitModeData data) {
                        LogUtils.i("TAG", new Gson().toJson(data.toString()));

                        if (data.single_rounds.size() == 0)
                            return;
                        ToastUtils.showToast("接口请求成功");
                        UserModel userModel = DbDownUtil.getInstance().findUser(data.single_rounds.get(0).user_id);
                        if (userModel == null) {
                            userModel = new UserModel();
                            userModel.setUserId(data.single_rounds.get(0).user_id);
                            userModel.setCreateTime(new Date().getTime());
                        }

                        userModel.setTotalBout(data.single_rounds.size());
                        userModel.setName(data.single_rounds.get(0).user_name);
                        DbDownUtil.getInstance().insertUser(userModel);

                        utils.put(Constant.HOST, ipEdit);
                        utils.put(Constant.PORT, 9999);
                        utils.put(Constant.DEVICE_NUM, numEdit);
                        utils.put(Constant.CUR_BOUT, 1);
                        utils.put(Constant.CUR_FAXU, 1);
                        utils.put(Constant.TOTAL_BOUT, data.single_rounds.size());
                        utils.put(Constant.IS_FINISH, false);
                        utils.put(Constant.INIT_MODE_DATA, data.single_rounds);
                        utils.put(Constant.USER_NAME, data.single_rounds.get(0).user_name);
                        utils.put(Constant.USER_ID, data.single_rounds.get(0).user_id);
                        utils.put(Constant.BULLET_COUNT, data.single_rounds.get(0).bullet_count);
                        utils.put(Constant.IS_NEED_WAY, false);

                        //todo 模拟 自由和单枪  记得还原
                        if (data.mode == 1) {
                            startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                        } else {
                            // 一枪多靶
                            startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));
                        }
                        LogUtils.i("dealMsg", "shootType == 2 主动抛异常！！！" + "finish()");
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        LogUtils.e("dealMsg onError", msg);
                        ToastUtils.showToast("输入错误：" + msg);
//                        startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));
//                        finish();
                    }
                });

            } else if (shootType == 3) {
                startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));
                LogUtils.i("dealMsg", "shootType == 3 主动抛异常！！！" + "finish()");
                finish();
            } else if (shootType == 1) {
                startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                LogUtils.i("dealMsg", "shootType == 1 主动抛异常！！！" + "finish()");
                finish();
            }


        }else{
            isGunInit=false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serialHelper != null) {
            serialHelper.close();
            serialHelper = null;
        }
        handler.removeCallbacksAndMessages(null);
    }


    /**
     * 依旧是申请权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限申请成功
                    Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "您禁止了语音权限，将无法播报任何音频~", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void generateFakeData(String userName, String bootNum, String shootNum) {
        InitModeData single_rounds = new InitModeData();
        List<InitMode> list = new ArrayList<>();

        long userid = utils.getLong(Constant.CUSTOM_USER_ID, 10000L);
        int boutId = utils.getInt(Constant.CUSTOM_BOUT_ID, 1);
        userid++;
        for (int i = 0; i < Integer.parseInt(bootNum); i++) {
            InitMode mode = new InitMode();
            mode.bout_num = i + 1;
            mode.shoot_num = i + 1;
            mode.bout_id = ++boutId;
            mode.user_id = userid;
            mode.user_name = userName;
            mode.bullet_count = Integer.parseInt(shootNum);
            list.add(mode);
        }
        utils.put(Constant.CUSTOM_USER_ID, userid);
        utils.put(Constant.CUSTOM_BOUT_ID, boutId);

        single_rounds.single_rounds = list;
        single_rounds.mode = 1;

        UserModel userModel = DbDownUtil.getInstance().findUser(single_rounds.single_rounds.get(0).user_id);
        if (userModel == null) {
            userModel = new UserModel();
            userModel.setUserId(single_rounds.single_rounds.get(0).user_id);
            userModel.setCreateTime(new Date().getTime());
        }

        userModel.setTotalBout(single_rounds.single_rounds.size());
        userModel.setName(single_rounds.single_rounds.get(0).user_name);
        DbDownUtil.getInstance().insertUser(userModel);

        utils.put(Constant.HOST, ipEdit);
        utils.put(Constant.PORT, "http://127.0.0.1:9999/api/");
        utils.put(Constant.DEVICE_NUM, numEdit);
        utils.put(Constant.CUR_BOUT, 1);
        utils.put(Constant.CUR_FAXU, 1);
        utils.put(Constant.TOTAL_BOUT, single_rounds.single_rounds.size());
        utils.put(Constant.IS_FINISH, false);
        utils.put(Constant.INIT_MODE_DATA, single_rounds.single_rounds);
        utils.put(Constant.USER_NAME, single_rounds.single_rounds.get(0).user_name);
        utils.put(Constant.USER_ID, single_rounds.single_rounds.get(0).user_id);
        utils.put(Constant.BULLET_COUNT, single_rounds.single_rounds.get(0).bullet_count);
        utils.put(Constant.IS_NEED_WAY, true);

    }

    private void generateConfigData(
            String numEdit, Integer shootType, String userName, String bootNum, String shootNum
    ) {
        ConfigDataModel configDataModel = new ConfigDataModel();
        configDataModel.setGroup(numEdit);
        configDataModel.setShootType(shootType);
        //单人模式
        if (shootType == 1) {
            configDataModel.setName(userName);
            configDataModel.setCreateTime(new Date().getTime());
            configDataModel.setShootNum(shootNum);
            configDataModel.setTotalBout(bootNum);
            //系统模式， 从服务端获取配置；
        } else if (shootType == 2) {
//            configDataModel.setShootType(shootType);
//            configDataModel.setName(userName);
//            configDataModel.setCreateTime(new Date().getTime());
//            configDataModel.setShootNum(shootNum);
//            configDataModel.setTotalBout(bootNum);
            //自由模式
        } else if (shootType == 3) {
            configDataModel.setCreateTime(new Date().getTime());
        }
        long id = DbDownUtil.getInstance().insertConfigDataModel(configDataModel);
        LogUtils.i("generateConfigData",
        "插入完成 shootType = " + shootType + ", configDataModel = " + configDataModel);

    }


    @Override
    protected void onResume() {
        LogUtils.i("init ", "onResume");
        // 保留最近 30天日志；
        LogUtils.delFile();
        super.onResume();
        initPermission();
        initSerial();
        initConfigData();
        initPrinter();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        start = findViewById(R.id.start);
        start_free = findViewById(R.id.start_free);
        start_system = findViewById(R.id.start_system);
        start_single = findViewById(R.id.start_single);

        ip = findViewById(R.id.ipEt);
        num = findViewById(R.id.numEt);
        bootNumEt = findViewById(R.id.bootNumEt);
        bootNumEt.setFilters(new InputFilter[]{new number100Filter()});
        userNameEt = findViewById(R.id.userNameEt);
        numEt = findViewById(R.id.numEt);
        numEt.setFilters(new InputFilter[]{new number99Filter()});

        ll_single = findViewById(R.id.ll_single);
        ll_system = findViewById(R.id.ll_system);
        ll_free = findViewById(R.id.ll_free);
        ll_gunNum = findViewById(R.id.ll_gunNum);
        utils = SPUtils.getInstance(getApplication());


        start_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shootType = 2;
                ll_gunNum.setVisibility(View.VISIBLE);
                ll_system.setVisibility(View.VISIBLE);
                ll_single.setVisibility(View.GONE);

            }
        });

        start_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shootType = 3;
                ll_gunNum.setVisibility(View.VISIBLE);
                ll_system.setVisibility(View.GONE);
                ll_single.setVisibility(View.GONE);
            }
        });

        start_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shootType = 1;
                ll_gunNum.setVisibility(View.VISIBLE);
                ll_system.setVisibility(View.GONE);
                ll_single.setVisibility(View.VISIBLE);
            }
        });
        start.setOnClickListener(v -> {
            numEdit = this.num.getText().toString().trim();

            if (numEdit.isEmpty()) {
                ToastUtils.showToast("枪配置不能为空");
                return;
            }

            if (shootType == 1) {
                String userName = userNameEt.getText().toString();
                if (userName.isEmpty()) {
                    ToastUtils.showToast("用户名不能为空");
                    return;
                }
                String bootNum = bootNumEt.getText().toString();
                if (bootNum.isEmpty()) {
                    ToastUtils.showToast("局数不能为空");
                    return;
                }
                String shootNum = spinner.getSelectedItem().toString();
                if (shootNum.isEmpty()) {
                    ToastUtils.showToast("发数不能为空");
                    return;
                }
                generateFakeData(userName, bootNum, shootNum);
                generateConfigData(numEdit, shootType, userName, bootNum, shootNum); //参数写入库
            } else if (shootType == 2) {
                ipEdit = this.ip.getText().toString().trim();
                if (ipEdit.isEmpty()) {
                    ToastUtils.showToast("ip不能为空");
                    return;
                }
                generateConfigData(numEdit, shootType, null, null, null);//参数写入库
            } else if (shootType == 3) {
                generateConfigData(numEdit, shootType, null, null, null);//参数写入库
            }

            if (!ipEdit.isEmpty())
                Common.API_URL = "http://" + ipEdit + ":" + 9999 + "/api/";
            else
                ipEdit = "http://127.0.0.1:9999/api/";

            String deviceNum = Integer.toHexString(Integer.parseInt(numEdit));
            if (deviceNum.length() == 1) {
                deviceNum = "0" + deviceNum;
            }

            sendData = "A5087F0101" + deviceNum + "01";
            byte[] hex = ByteUtil.HexToByteArr(sendData);
            byte sum = 0;
            for (byte b : hex) {
                sum += b;
            }

            if (ByteUtil.Byte2Hex(sum).length() == 1) {
                sendData = sendData + "0" + ByteUtil.Byte2Hex(sum);
            } else {
                sendData = sendData + ByteUtil.Byte2Hex(sum);
            }


            LogUtils.i("TAG", sendData);
            try {
                serialHelper.sendHex(sendData);
            } catch (Exception e) {
                LogUtils.e("serialHelper.sendHex, sendData = ", sendData);
            }

            //todo 本地测试数据
//            ComBean bean = new ComBean("", ByteUtil.HexToByteArr(sendData), 8);
//            Message message = handler.obtainMessage();
//            message.obj = bean;
//            handler.sendMessage(message);

//            InitModeData single_rounds = new InitModeData();
//            List<InitMode> list = new ArrayList<>();
//
//            InitMode mode1 = new InitMode();
//            mode1.bout_num = 1;
//            mode1.shoot_num = 1;
//            mode1.bout_id = 1001;
//            mode1.user_id = 1011;
//            mode1.user_name = "张三";
//            mode1.bullet_count = 10;
//
//            list.add(mode1);
//
//            InitMode mode2 = new InitMode();
//            mode2.bout_num = 2;
//            mode2.shoot_num = 2;
//            mode2.bout_id = 1002;
//            mode2.user_id = 1011;
//            mode2.user_name = "张三";
//            mode2.bullet_count = 10;
//
//            list.add(mode2);
//            single_rounds.single_rounds = list;
//
//            UserModel userModel = DbDownUtil.getInstance().findUser(single_rounds.single_rounds.get(0).user_id);
//            if (userModel == null) {
//                userModel = new UserModel();
//                userModel.setUserId(single_rounds.single_rounds.get(0).user_id);
//                userModel.setCreateTime(new Date().getTime());
//            }
//
//            userModel.setTotalBout(single_rounds.single_rounds.size());
//            userModel.setName(single_rounds.single_rounds.get(0).user_name);
//            DbDownUtil.getInstance().insertUser(userModel);
//
//            utils.put(Constant.HOST, ipEdit);
//            utils.put(Constant.PORT, portEdit);
//            utils.put(Constant.DEVICE_NUM, numEdit);
//            utils.put(Constant.CUR_BOUT, 1);
//            utils.put(Constant.CUR_FAXU, 1);
//            utils.put(Constant.TOTAL_BOUT, single_rounds.single_rounds.size());
//            utils.put(Constant.IS_FINISH, false);
//            utils.put(Constant.INIT_MODE_DATA, single_rounds.single_rounds);
//            utils.put(Constant.USER_NAME, single_rounds.single_rounds.get(0).user_name);
//            utils.put(Constant.USER_ID, single_rounds.single_rounds.get(0).user_id);
//            utils.put(Constant.BULLET_COUNT, single_rounds.single_rounds.get(0).bullet_count);
//            utils.put(Constant.IS_NEED_WAY,true);
//            startActivity(new Intent(ConfigActivity.this, MainActivity.class));
////            startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));
//
//            finish();

//            InitHelper.init(new NetCallBack<InitModeData>() {
//                @Override
//                public void onResponseData(InitModeData data) {
//
//                    if (data.single_rounds.size() == 0)
//                        return;
//
//                    UserModel userModel = DbDownUtil.getInstance().findUser(data.single_rounds.get(0).user_id);
//                    if (userModel == null) {
//                        userModel = new UserModel();
//                        userModel.setUserId(data.single_rounds.get(0).user_id);
//                        userModel.setCreateTime(new Date().getTime());
//                    }
//
//                    userModel.setTotalBout(data.single_rounds.size());
//                    userModel.setName(data.single_rounds.get(0).user_name);
//                    DbDownUtil.getInstance().insertUser(userModel);
//
//                    utils.put(Constant.HOST, ipEdit);
//                    utils.put(Constant.PORT, portEdit);
//                    utils.put(Constant.DEVICE_NUM, numEdit);
//                    utils.put(Constant.CUR_BOUT, 1);
//                    utils.put(Constant.CUR_FAXU, 1);
//                    utils.put(Constant.TOTAL_BOUT, data.single_rounds.size());
//                    utils.put(Constant.IS_FINISH, false);
//                    utils.put(Constant.INIT_MODE_DATA, data.single_rounds);
//                    utils.put(Constant.USER_NAME, data.single_rounds.get(0).user_name);
//                    utils.put(Constant.USER_ID, data.single_rounds.get(0).user_id);
//                    utils.put(Constant.BULLET_COUNT, data.single_rounds.get(0).bullet_count);
//                    startActivity(new Intent(ConfigActivity.this, MainActivity.class));
//                    finish();
//
//                }
//
//                @Override
//                public void onError(String msg) {
//                    ToastUtils.showToast(msg);
//                }
//            });

        });
    }

    public class number99Filter implements InputFilter {
        /**
         * @param source 新输入的字符串
         * @param start  新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart 新输入的字符在原字符串中的位置
         * @param dend   原内容终点坐标，
         * @return 输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText = dest.toString();
            if (dstart == 0 && "0".equals(source)) {
                //如果输入是0 且位置在第一位，取消输入
                return "";
            }

            StringBuilder totalText = new StringBuilder();
            totalText.append(destText.substring(0, dstart))
                    .append(sourceText)
                    .append(destText.substring(dstart, destText.length()));


            try {
                if (Integer.parseInt(totalText.toString()) > 99) {
                    return "";
                } else if (Integer.parseInt(totalText.toString()) == 0) {
                    //如果输入是0，取消输入
                    return "";
                }
            } catch (Exception e) {
                return "";
            }

            if ("".equals(source.toString())) {
                return "";
            }
            return "" + Integer.parseInt(source.toString());
        }
    }

    public class number100Filter implements InputFilter {
        /**
         * @param source 新输入的字符串
         * @param start  新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart 新输入的字符在原字符串中的位置
         * @param dend   原内容终点坐标，
         * @return 输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText = dest.toString();
            if (dstart == 0 && "0".equals(source)) {
                //如果输入是0 且位置在第一位，取消输入
                return "";
            }

            StringBuilder totalText = new StringBuilder();
            totalText.append(destText.substring(0, dstart))
                    .append(sourceText)
                    .append(destText.substring(dstart, destText.length()));


            try {
                if (Integer.parseInt(totalText.toString()) > 100) {
                    return "";
                } else if (Integer.parseInt(totalText.toString()) == 0) {
                    //如果输入是0，取消输入
                    return "";
                }
            } catch (Exception e) {
                return "";
            }

            if ("".equals(source.toString())) {
                return "";
            }
            return "" + Integer.parseInt(source.toString());
        }
    }

    /**
     * 获取数组下标
    */
    public static int getIndex(Integer[] array,int value){
        for(int i = 0;i<array.length;i++){
            if(array[i]==value){
                return i;
            }
        }
        return -1;//当if条件不成立时，默认返回一个负数值-1
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false; // 没有SIM卡
                break;
        }
        return result;
    }
    /**
     * 获取信号强度
     */
    private void getMobileNetworkSignal() { //没有sim卡功能以后再添加
        if (hasSimCard(getApplicationContext())) {
            ToastUtils.showToast("getMobileNetworkSignal: no sim card");
            return;
        }
        TelephonyManager mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(new PhoneStateListener() {

                @Override
                public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                    super.onSignalStrengthsChanged(signalStrength);
                    int asu = signalStrength.getGsmSignalStrength();
                    int lastSignal = -113 + 2 * asu;
                    if (lastSignal > 0) {
                        String mobileNetworkSignal = lastSignal + "dBm";
                    }
                    ToastUtils.showToast("Current mobileNetworkSignal：" + lastSignal + " dBm");
                }
            }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
    }

}
