package org.ggxz.shoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.common_module.common.Constant;
import com.example.common_module.db.DbDownUtil;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.utils.FileUtils;
import com.example.common_module.utils.ToastUtils;
import com.example.net_module.callback.NetCallBack;
import com.example.net_module.helper.InitHelper;
import com.example.net_module.mode.InitModeData;
import com.example.net_module.mode.TopUser;
import com.google.gson.Gson;

import org.ggxz.shoot.mvp.view.activity.ConfigActivity;
import org.ggxz.shoot.mvp.view.activity.MainActivity;
import org.ggxz.shoot.mvp.view.activity.MultipleActivity;

import java.util.Date;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        InitHelper.getTopTypeList(new NetCallBack<TopUser>() {
            @Override
            public void onResponseData(TopUser topUser) {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
