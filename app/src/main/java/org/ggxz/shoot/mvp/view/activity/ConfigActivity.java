package org.ggxz.shoot.mvp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.utils.AssetFileReader;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.utils.ToastUtils;
import com.example.common_module.utils.player.AudioPlayerHelper;
import com.example.net_module.Common;
import com.example.net_module.callback.NetCallBack;
import com.example.net_module.helper.InitHelper;
import com.example.net_module.mode.InitMode;
import com.example.net_module.mode.InitModeData;
import com.example.net_module.mode.TopUser;
import com.google.gson.Gson;

import org.ggxz.shoot.R;

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

    TextView start;
    EditText ip;
    EditText userNameEt;
    EditText num;
    EditText bootNumEt;//局数
    EditText shootNumEt;//发序
    LinearLayout ll_system;
    LinearLayout ll_single;
    LinearLayout ll_free;
    LinearLayout ll_gunNum;

    TextView start_free;
    TextView start_system;
    TextView start_single;
    SPUtils utils;
    private SerialHelper serialHelper;

    private String sendData;

    private String ipEdit = "";
    private String numEdit = "";


    private int shootType = 1;//1 单人模式 2 系统模式 3自由模式  1-2 进相同页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        setContentView(R.layout.activity_config);

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
                Toast.makeText(this, "串口打开成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "串口打开异常", Toast.LENGTH_SHORT).show();

            }
        }

    }

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
        Log.e("TAG", text);
        if (type.equalsIgnoreCase("7F")) {
            ToastUtils.showToast("枪配网成功");
            if (shootType == 2) {
                ToastUtils.showToast("系统射击");
                InitHelper.init(new NetCallBack<InitModeData>() {
                    @Override
                    public void onResponseData(InitModeData data) {
                        Log.e("TAG", new Gson().toJson(data.toString()));

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
                        if (data.mode == 1)
                            startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                        else
                            startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));

                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("TAG", msg);
                        ToastUtils.showToast("输入错误：" + msg);
//                        startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));
//                        finish();
                    }
                });

            } else if (shootType == 3) {
                startActivity(new Intent(ConfigActivity.this, MultipleActivity.class));
                finish();
            } else if (shootType == 1) {
                startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                finish();
            }


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

    @Override
    protected void onResume() {
        super.onResume();
        initPermission();
        initSerial();
        start = findViewById(R.id.start);
        start_free = findViewById(R.id.start_free);
        start_system = findViewById(R.id.start_system);
        start_single = findViewById(R.id.start_single);

        ip = findViewById(R.id.ipEt);
        num = findViewById(R.id.numEt);
        bootNumEt = findViewById(R.id.bootNumEt);
        shootNumEt = findViewById(R.id.shootNumEt);
        userNameEt = findViewById(R.id.userNameEt);

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
                String shootNum = shootNumEt.getText().toString();
                if (shootNum.isEmpty()) {
                    ToastUtils.showToast("发序不能为空");
                    return;
                }
                generateFakeData(userName, bootNum, shootNum);
            } else if (shootType == 2) {
                ipEdit = this.ip.getText().toString().trim();
                if (ipEdit.isEmpty()) {
                    ToastUtils.showToast("ip不能为空");
                    return;
                }
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


            Log.e("TAG", sendData);
            serialHelper.sendHex(sendData);

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


}
