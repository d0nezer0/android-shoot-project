package org.ggxz.shoot.mvp.view.activity;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common_module.base.mvp.BaseMvpActivity;
import com.example.common_module.common.Constant;
import com.example.common_module.db.EnterInfo;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.utils.player.AudioPlayerHelper;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.decoration.StickHeaderDecoration;
import org.ggxz.shoot.mvp.presenter.impl.MultiplePresenterImpl;
import org.ggxz.shoot.mvp.view.activity_view.MultipleView;
import org.ggxz.shoot.utils.LogUtils;
import org.ggxz.shoot.widget.TargetPointViewMultiple;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.stick.AbsStickPackageHelper;
import tp.xmaihh.serialport.utils.ByteUtil;

public class MultipleActivity extends BaseMvpActivity<MultiplePresenterImpl> implements MultipleView {
    @BindView(R.id.targetView)
    TargetPointViewMultiple targetView;
    @BindView(R.id.recyclerLayout)
    LinearLayout recyclerLayout;

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

        //初始化人型靶面
        targetView.setRingCount(10); // 设置环数为10
        targetView.setBackgroundColor(Color.TRANSPARENT); // 设置背景色为灰色

        targetView.setXAxisRange(-10f, 10f);
        targetView.setYAxisRange(-10f, 10f);
        targetView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void initData() {

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
                LogUtils.i("Time ->", interval + "");
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
            if (msg.what == PORT_TYPE) {//串口通信相关
                ComBean comBean = (ComBean) msg.obj;
                String t = comBean.sRecTime;
                String rxText = ByteUtil.ByteArrToHex(comBean.bRec);
                String text = "Rx-> " + t + ": " + rxText + "\r" + "\n";
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
                                model.setUserId(list.size()+1); //把list大小作为发序放入userId当中，应为entrymodle没有发序属性，借用
                                list.add(0,model); //向前插入，显示时最新一发始终排在第一位
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
                                targetView.setValues(list);

                                LogUtils.i(TAG, "Re-> success-点");
                            } else {
                                Arrays.fill(res, (byte) 0);
                            }
                            state = 0;
                            break;
                    }
                }
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
                    targetView.setValues(data);
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
