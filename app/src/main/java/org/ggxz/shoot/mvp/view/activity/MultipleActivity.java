package org.ggxz.shoot.mvp.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.common.Constant;
import com.example.common_module.db.EnterInfo;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.utils.AssetFileReader;

import org.ggxz.shoot.utils.ResourceMonitor;

import com.example.common_module.utils.ToastUtils;
import com.example.common_module.utils.player.AudioPlayerHelper;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.decoration.StickHeaderDecoration;
import org.ggxz.shoot.mvp.presenter.impl.MultiplePresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.MultipleView;
import org.ggxz.shoot.utils.LogUtils;
import org.ggxz.shoot.utils.SettingUtil;
import org.ggxz.shoot.widget.TargetPointViewMultiple;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.stick.AbsStickPackageHelper;
import tp.xmaihh.serialport.utils.ByteUtil;

/**
 * 自由模式主体
 */
public class MultipleActivity extends BaseMvpActivity<MultiplePresenterImpl> implements MultipleView {
    @BindView(R.id.targetView)
    TargetPointViewMultiple targetView_rxbm;
    @BindView(R.id.recyclerLayout)
    LinearLayout recyclerLayout;

    /**
     * 胸环靶
     */
    @BindView(R.id.shootNum)
    TextView shootNum;

    private SerialHelper serialHelper;
    private static final int PORT_TYPE = 0x05;
    private int state;
    private byte[] res = new byte[11];//数据拼接
    private boolean isGun92 = true;
    private long shootNumber = 0L;  //发序



    private Map<Integer, List<EntryModel>> map = new HashMap<>();//key 枪号 value 没个枪 存储的开抢的数据

    private Map<Integer, Adapter> views = new HashMap<>();
    private DecimalFormat df = new DecimalFormat("#.#");
    private AudioPlayerHelper playerHelper;


    private Map<Integer, Boolean> groupWithGunType = new HashMap<>();//true 92

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_multiple);
    }

    @Override
    protected void initView() {
        playerHelper = new AudioPlayerHelper(this);
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
//        // 测试数据；
//        List<String> fileLines = new ArrayList<>();
//        AtomicInteger s = new AtomicInteger(0);
//        if (SettingUtil.openTestData) {
//            //todo 本地测试数据 读取 assets文件
//            fileLines = AssetFileReader.readAssetFile(getApplicationContext(), "file.txt");
//        }
//        List<String> finalFileLines = fileLines;
//        shootNum.setOnClickListener(v -> {
//            if (SettingUtil.openTestData) {
//                testData(finalFileLines, s);
//            }
//        });

        //初始化人型靶面
        targetView_rxbm.setRingCount(10); // 设置环数为10
        targetView_rxbm.setBackgroundColor(Color.TRANSPARENT); // 设置背景色为灰色

        targetView_rxbm.setXAxisRange(-10f, 10f);
        targetView_rxbm.setYAxisRange(-10f, 10f);
        targetView_rxbm.setLayerType(View.LAYER_TYPE_HARDWARE, null);

//        ResourceMonitor.printMemoryUsage(this, "MultipleActivity");
    }

    /**
     * 获取本地测试数据方法，需要的时候打开
     *
     * @param fileLines 本地file.txt
     * @param s         计数器
     */
    private void testData(List<String> fileLines, AtomicInteger s) {
        if (s.get() < fileLines.size()) {
            byte[] bytes = ByteUtil.HexToByteArr(fileLines.get(s.get()).trim().replace(" ", ""));
            ComBean bean = new ComBean("11", bytes, bytes.length);
            Message message = handler.obtainMessage();
            message.obj = bean;
            message.what = PORT_TYPE;
            handler.sendMessage(message);
            s.set(s.get() + 1);
        } else {
            ToastUtils.showToast("测试数据已跑完");
            s.set(0);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        // 当用户点击返回键时，重新启动固定页面
        Intent intent = new Intent(this, ConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // 结束当前活动
    }

    @Override
    protected MultiplePresenterImpl initInjector() {
        return new MultiplePresenterImpl();
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
                Message message = handler.obtainMessage();
                lastTime[0] = now;
                LogUtils.i("MultipleActivity Time ->", interval + "");
                LogUtils.i("MultipleActivity paramComBean.bRec.length = " + paramComBean.bRec.length + ", Time ->", interval);
                String t = paramComBean.sRecTime;
                String rxText = ByteUtil.ByteArrToHex(paramComBean.bRec);
                String text = "Rx-> " + t + ": " + rxText + "\r" + "\n";
                LogUtils.i("onDataReceived", text);
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

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == PORT_TYPE) {//串口通信相关
                    ComBean comBean = (ComBean) msg.obj;
                    String t = comBean.sRecTime;
                    String rxText = ByteUtil.ByteArrToHex(comBean.bRec);
                    String text = "MultipleActivity Rx-> " + t + ": " + rxText + "\r" + "\n";
                    LogUtils.i(TAG, text);

                    //state 表示当前 res[11]中已经存储到的字节有集合 note 这里的问题在于：读到新Head 后面A5不会再读 直到获取完整数据 或 检验失败清空之前存储的数据
                    //  A5 A5 0B 7E 01 01 01 FF 9C 01 9A 67  / A5 0B 7E 03 A5 A5 0B 7E 01 01 01 FF 9C 01 9A 67 A5 0B 7E 01 01 01 FF 9C 01 9A 67-> A5 0B 7E 03 01 01 FF 9C 01 9A 67 正确吗？
                    for (int i = 0; i < comBean.bRec.length; i++) {
                        byte b = comBean.bRec[i];
                        switch (state) {
                            case 0:
                                if (b == (byte) 0xA5) {//帧头
                                    state = 1;
                                    res[0] = b;
                                }
                                break;
                            case 1:
                                if ((b == (byte) 0x0B) || b == (byte) 0x06) {//0B xy坐标数据 06 开抢数据
                                    state = 2;
                                    res[1] = b;
                                } else {
                                    state = 0;
                                }
                                break;
                            case 2:
                                if (b == (byte) 0x7E) {//胸环靶
                                    state = 3;
                                    res[2] = b;
                                } else if (b == (byte) 0x01) {//92式手枪
                                    isGun92 = true;
//                                state = 3;
                                    res[2] = b;
                                    state = 0;
                                } else {
                                    isGun92 = false;
                                    state = 0;

                                }
                                break;
                            case 3://ID 靶/枪 此时出现A5 怎么办？
                                state = 4;
                                res[3] = b;
//                            if (res[1] == (byte) 0x06) {
//                                state = 0;
//                                groupWithGunType.put((int) b, res[2] == 0x01);
//                            }
                                break;
                            case 4://cmd
                                state = 5;
                                res[4] = b;
                                break;
                            case 5://
                                state = 6;
                                res[5] = b;

                                break;
                            //break 不能却掉 否则无法正确更新state以及保证res数据的顺序
                            case 6:
                                state = 7;
                                res[6] = b;
                                break;
                            case 7:
                                state = 8;
                                res[7] = b;
                                break;
                            case 8:
                                state = 9;
                                res[8] = b;
                                break;
                            case 9:
                                state = 10;
                                res[9] = b;
                                break;
                            case 10://走到这一定是 坐标数据

                                byte sum = 0;
                                for (byte re : res) {
                                    sum += re;
                                }
                                if (b == sum) {
//                              创建model 赋值res数据       EntryModel model = new EntryModel(); 存入数据库
                                    EntryModel model = mPresenter.makeData(res);
                                    if (model.getCmdType() != EnterInfo.CMD_TYPE.SHOOT)
                                        return;

                                    groupWithGunType.put(model.getDeviceID(), isGun92);

                                    if (playerHelper != null)
                                        playerHelper.play(String.valueOf(model.getRing()), isGun92);
                                    int gunId = model.getGunId();
                                    List<EntryModel> list = map.get(gunId);
                                    if (list == null) {
                                        list = new ArrayList<>();
                                    }
                                    model.setUserId(list.size() + 1); //把list大小作为发序放入userId当中，应为entrymodle没有发序属性，借用
                                    list.add(0, model); //向前插入，显示时最新一发始终排在第一位
                                    map.put(gunId, list);
                                    Adapter adapter = views.get(gunId);
                                    if (adapter == null) {
                                        RecyclerView recyclerView = createRecyclerView();
                                        recyclerLayout.addView(recyclerView);
                                        adapter = new Adapter();
                                        recyclerView.setAdapter(adapter);
                                        adapter.setData(list);
                                    } else {
                                        adapter.setData(list);
                                    }

                                    views.put(gunId, adapter);
                                    //先注释掉人形靶面的绘制 hangg 2024年5月30日
                                    targetView_rxbm.setValues(list);

                                } else {
                                    Arrays.fill(res, (byte) 0);
                                    LogUtils.i("MultipleActivity", "state10-2 finished");
                                }
                                state = 0;
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

    private RecyclerView createRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(this);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(params);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        return recyclerView;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> implements StickHeaderDecoration.StickHeaderInterface {

        List<EntryModel> data;
        boolean isClick = false;

        public Adapter() {
            data = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multiple_shoot_data, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.title_name.setOnClickListener(v -> {
                if (!data.isEmpty()) {
                    targetView_rxbm.setValues(data);
                    int id = data.get(0).getDeviceID();
                    for (int key : views.keySet()) {
                        if (key == id) {
                            isClick = true;
                            notifyDataSetChanged();
                        } else {
                            Adapter adapter = views.get(key);
                            if (adapter != null)
                                adapter.setClick(false);
                        }
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EntryModel model = data.get(position);
            // model.setUserId(position+1);  //用userid来保存发序
            if (position == 0) {
                holder.title_name.setVisibility(View.VISIBLE);
                holder.title_line.setVisibility(View.VISIBLE);
                holder.title_name.setText("枪 " + model.getGunId());
                if (isClick) {
                    holder.title_name.setBackground(getResources().getDrawable(R.drawable.button_round_trans_8));
                } else {
                    holder.title_name.setBackgroundColor(Color.TRANSPARENT);
                }
            } else {
                holder.title_name.setVisibility(View.GONE);
                holder.title_line.setVisibility(View.GONE);
            }
            holder.ring_num.setText(model.getRing() - position == 11F ? "10.10" : df.format(model.getRing()));
            holder.fa_xu.setText(String.valueOf(model.getUserId()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<EntryModel> data) {
            this.data = data;

            notifyDataSetChanged();
        }

        public void setClick(boolean click) {
            this.isClick = click;
            notifyDataSetChanged();
        }

        @Override
        public boolean isStick(int position) {
            return position == 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_name;
        View title_line;
        TextView ring_num;
        TextView fa_xu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_name = itemView.findViewById(R.id.title_name);
            title_line = itemView.findViewById(R.id.title_line);
            ring_num = itemView.findViewById(R.id.ring_num);
            fa_xu = itemView.findViewById(R.id.fa_xu);
        }
    }
}
