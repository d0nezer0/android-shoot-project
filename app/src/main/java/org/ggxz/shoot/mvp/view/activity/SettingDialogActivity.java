package org.ggxz.shoot.mvp.view.activity;


import static org.ggxz.shoot.mvp.view.activity.ConfigActivity.isGunInit;
import static org.ggxz.shoot.mvp.view.activity.ConfigActivity.isInitSerialPort;
import static org.ggxz.shoot.mvp.view.activity.ConfigActivity.mPrinter;
import static org.ggxz.shoot.mvp.view.activity.ConfigActivity.serialHelper;
import static org.ggxz.shoot.mvp.view.activity.MainActivity.isInitTargetSurface;

import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.common.Constant;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.utils.SPUtils;
import com.example.common_module.utils.ToastUtils;
import com.example.common_module.utils.UIUtils;
import com.printsdk.PrintSerializable;

import org.ggxz.shoot.R;
import org.ggxz.shoot.mvp.presenter.SettingPresenter;
import org.ggxz.shoot.mvp.presenter.impl.SettingPresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.SettingView;

import butterknife.BindView;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SettingDialogActivity extends BaseMvpActivity<SettingPresenter> implements SettingView<UserModel> {

    @BindView((R.id.check_auto_print))
    CheckBox checkBoxAutoPrint;
    @BindView(R.id.face)
    TextView face;
    @BindView(R.id.system)
    TextView system;
    @BindView(R.id.print)
    TextView print;
    @BindView(R.id.switchGun)
    ImageView switchGun;
    @BindView(R.id.switchScreen)
    ImageView switchScreen;
    @BindView(R.id.shootType1)
    TextView shootType1;
    @BindView(R.id.shootType2)
    TextView shootType2;
    @BindView(R.id.faceLayout)
    ViewGroup faceLayout;
    @BindView(R.id.systemLayout)
    ViewGroup systemLayout;
    @BindView(R.id.printLayout)
    ViewGroup printLayout;
    @BindView(R.id.close)
    ImageView close;
    private boolean switchGunFg = false;
    private boolean switchScreenFg = true;

    @BindView(R.id.serialPort)
    TextView serialPort;
    @BindView(R.id.gunStatus)
    TextView gunStatus;
    @BindView(R.id.printStatus)
    TextView printStatus;
    @BindView(R.id.targetStatus)
    TextView targetStatus;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_setting_dialog);
        //设置内容占据全屏
        getWindow().setLayout(UIUtils.dip2px(900), UIUtils.dip2px(600));
        getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    protected void initView() {

        face.setOnClickListener(v -> clickChangeBg(0, true));
        print.setOnClickListener(v -> clickChangeBg(2, true));
        system.setOnClickListener(v -> clickChangeBg(1, true));
        switchGun.setOnClickListener(v -> {
            switchGunFg = !switchGunFg;
            if (switchGunFg)
                switchGun.setImageDrawable(getDrawable(R.drawable.switch_on));
            else
                switchGun.setImageDrawable(getDrawable(R.drawable.swich_off));
        });

        switchScreen.setOnClickListener(v -> {
            switchScreenFg = !switchScreenFg;
            if (switchScreenFg)
                switchScreen.setImageDrawable(getDrawable(R.drawable.switch_on));
            else
                switchScreen.setImageDrawable(getDrawable(R.drawable.swich_off));
        });


        shootType1.setOnClickListener(v -> {
            SPUtils.getInstance(getApplication()).put(Constant.IS_NEED_WAY, false);

            shootType1.setBackground(getDrawable(R.drawable.button_round_0));
            shootType2.setBackgroundColor(Color.TRANSPARENT);
        });

        shootType2.setOnClickListener(v -> {
            SPUtils.getInstance(getApplication()).put(Constant.IS_NEED_WAY, true);

            shootType2.setBackground(getDrawable(R.drawable.button_round_0));
            shootType1.setBackgroundColor(Color.TRANSPARENT);
        });

        close.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected SettingPresenter initInjector() {
        return new SettingPresenterImpl();
    }

    private void clickChangeBg(int type, boolean isClick) {
        switch (type) {
            case 0:
                if (isClick) {
                    face.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_select));
                    system.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_unselect));
                    print.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_unselect));
                    faceLayout.setVisibility(View.VISIBLE);
                    systemLayout.setVisibility(View.GONE);
                    printLayout.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (isClick) {
                    face.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_unselect));
                    system.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_select));
                    print.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_unselect));
                    faceLayout.setVisibility(View.GONE);
                    systemLayout.setVisibility(View.VISIBLE);
                    printLayout.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (isClick) {
                    face.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_unselect));
                    system.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_unselect));
                    print.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_setting_left_select));
                    faceLayout.setVisibility(View.GONE);
                    systemLayout.setVisibility(View.GONE);
                    printLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.getInstance(this.getApplication()).getBoolean(Constant.IS_NEED_WAY, false)) {
            shootType2.setBackground(getDrawable(R.drawable.button_round_0));
            shootType1.setBackgroundColor(Color.TRANSPARENT);
        } else {
            shootType1.setBackground(getDrawable(R.drawable.button_round_0));
            shootType2.setBackgroundColor(Color.TRANSPARENT);

        }
        //串口

        if (isInitSerialPort) {
            serialPort.setText("串口打开成功");
        } else {
            serialPort.setText("串口打开失败");
        }

        //配置成功
        if (isGunInit) {
            gunStatus.setText("枪配网成功");
        } else {
            gunStatus.setText("枪配网失败");
        }

        //打印机状态
        if (mPrinter != null && mPrinter.getState() == PrintSerializable.CONN_SUCCESS) {
            printStatus.setText("打印机初始化成功");
        } else {
            printStatus.setText("打印机初始化失败");
        }
        //靶面
        if (isInitTargetSurface) {
            targetStatus.setText("靶面配网成功");
        } else {
            targetStatus.setText("靶面配网失败");
        }

        //是否自动打印checkbox
        checkBoxAutoPrint.setChecked(SPUtils.getInstance(getApplication()).getBoolean("auto_print",false));
        checkBoxAutoPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.getInstance(getApplication()).put("auto_print", isChecked);
            }
        });

    }
}
