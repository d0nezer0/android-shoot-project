package org.ggxz.shoot.mvp.view.fragment;

import static org.ggxz.shoot.mvp.view.activity.ConfigActivity.mPrinter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.blankj.utilcode.utils.HandlerUtils;
import com.blankj.utilcode.utils.TimeUtils;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.printsdk.PrintSerializable;

import org.ggxz.shoot.R;
import org.ggxz.shoot.adapter.DetailsRVAdapter;
import org.ggxz.shoot.mvp.view.activity.DetailsActivity;
import org.ggxz.shoot.mvp.view.activity.MainActivity;
import org.ggxz.shoot.widget.TargetPointView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
    DecimalFormat df = new DecimalFormat("#.#");

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
    public  void onResume(){
        super.onResume();
        try {
            create_QR_code();
        } catch (WriterException e) {
           e.printStackTrace();}
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


    /**
    生成成绩二维码图片
    */
    private void create_QR_code() throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
       // String text = "成绩：1000 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三 \n姓名：张三";
          String text = buildQrText();
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                bitmap.setPixel(x, y, matrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.cardview_light_background));
            }
        }
        // 通过像素数组生成bitmap,具体参考api
        //bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        /*byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
        String utf8BytesString = new String(utf8Bytes, StandardCharsets.UTF_8);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(utf8BytesString, BarcodeFormat.QR_CODE, 150, 150);
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
        for (int x = 0; x < 150; x++) {
            for (int y = 0; y < 150; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.colorAccent));
            }
        }*/
/*        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //定义二维码的纠错级别，为L
        hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置字符编码为utf-8
        hashMap.put(EncodeHintType.MARGIN, 2);
        //设置margin属性为2,也可以不设置
        String contents = "最简单的Demo"; //定义二维码的内容
        BitMatrix bitMatrix = null;   //这个类是用来描述二维码的,可以看做是个布尔类型的数组
        try {
            bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, 150, 150, hashMap);
            //调用encode()方法,第一次参数是二维码的内容，第二个参数是生二维码的类型，第三个参数是width，第四个参数是height，最后一个参数是hints属性
        } catch (WriterException e) {
            e.printStackTrace();
        }

        int width = bitMatrix.getWidth();//获取width
        int height = bitMatrix.getHeight();//获取height
        int[] pixels = new int[width * height]; //创建一个新的数组,大小是width*height
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //通过两层循环,为二维码设置颜色
                if (bitMatrix.get(i, j)) {
                    pixels[i * width + j] = Color.BLACK;  //设置为黑色

                } else {
                    pixels[i * width + j] = Color.WHITE; //设置为白色
                }
            }
        }
        //调用Bitmap的createBitmap()，第一个参数是width,第二个参数是height,最后一个是config配置，可以设置成RGB_565
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //调用setPixels(),第一个参数就是上面的那个数组，偏移为0，x,y也都可为0，根据实际需求来,最后是width ,和height
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);*/
        //调用setImageBitmap()方法，将二维码设置到imageview控件里

            activity.changeImage(bitmap);

    }

    /**
     * 构建二维码的文本
     *
     * @return qrtext
     */

    private String buildQrText(){
        String s="";
        ShootDataModel model = models.get(activity.curBout - 1);
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

            s="-------------------------------------------\n"+
                    "                   晟  达\n"+
                    "              S H E N G D A\n"+
                    "          模拟射击系统成绩单\n"+
                    "         -----------------------------\n"+
                    "姓名：" + model.getUserName()+"\n"+
                    "靶型：胸环靶 \n"+
                    "打印时间：" + TimeUtils.millis2String(System.currentTimeMillis())+"\n"+
                    "总发数：" + shootTotalNum+"\n"+
                    "中靶数：" + shootNum+"\n"+
                    "脱靶数：" + notShootNum+"\n"+
                    "总环数：" + df.format(model.getTotalRing())+"\n"+
                    "均环数：" + df.format(model.getTotalRing() / shootTotalNum)+"\n"+
                    "总用时：" + getTimeStr(timeSeconds)+"\n"+
                    "平均分：" + Math.round(totalAllGrade / (float)shootTotalNum)+"\n"+
                    "-------------------------------------------\n"+
                    "                  感谢您的使用！\n"+
                    "             晟达启航-提供技术支持";
        } else {
            ToastUtils.showToast("打印没有射击数据");

        }
        return s;
    }
    //打印每局,每局中每发的数
    public void print() {
        if (mPrinter.getState() != PrintSerializable.CONN_SUCCESS) {
            ToastUtils.showToast("打印机状态有问题，请重新初始化");
            return;
        }
        ShootDataModel model = models.get(activity.curBout - 1);
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
            try {
                create_QR_code();
            } catch (WriterException e) {
                e.printStackTrace();
            }
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
            try {
                create_QR_code();
            } catch (WriterException e) {
                e.printStackTrace();
            }
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
