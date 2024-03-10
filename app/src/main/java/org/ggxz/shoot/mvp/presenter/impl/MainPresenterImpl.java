package org.ggxz.shoot.mvp.presenter.impl;

import android.util.Log;

import com.example.common_module.App;
import com.example.common_module.base.mvp.BasePresenterImpl;
import com.example.common_module.common.Constant;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.utils.BinaryUtils;
import com.example.common_module.utils.FileUtils;
import com.example.net_module.callback.NetCallBack;
import com.example.net_module.helper.InitHelper;
import com.example.net_module.mode.InitModeData;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import org.ggxz.shoot.mvp.presenter.MainPresenter;
import org.ggxz.shoot.mvp.view.activity_view.MainView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

    @Override
    public void getTopAllData() {

    }

    @Override
    public void getUserInformationData() {


    }

    @Override
    public void getShootPointData() {

    }

    @Override
    public void getShootRoundData() {

    }

    @Override
    public void getShootStatistcsData() {

    }

    @Override
    public void getShootChartData() {

    }


    public EntryModel makeData(byte[] group) {
        EntryModel model = new EntryModel();
        for (int i = 0; i < group.length; i++) {
            byte b = group[i];
            switch (i) {
                case 0:
                    model.setHead(b);
                    break;
                case 1:
                    model.setLen(b);
                    break;
                case 2:
                    model.setType(b);
                    break;
                case 3:
                    model.setDeviceID(b);
                    break;
                case 4:
                    model.setCmd(b);
                    break;
                case 5:
                    model.setGunId(b);
                    model.setGunInAndMiss(b);
                    break;
                case 9://5-8 4个byte才有

                    byte[] symbolX = Arrays.copyOfRange(group, 6, 8);
                    int toIntX = BinaryUtils.bytesToSignedInt(symbolX);
                    byte[] symbolY = Arrays.copyOfRange(group, 8, 10);
                    int toIntY = BinaryUtils.bytesToSignedInt(symbolY);

//                    model.setX(getMockValue(toIntX / Constant.RADIUS));
//                    model.setY(getMockValue(toIntY / Constant.RADIUS));
                    model.setX(toIntX / Constant.RADIUS);
                    model.setY(toIntY / Constant.RADIUS);
                    Log.e("XXXXX = ", String.valueOf(model.getX()));
                    Log.e("YYYYY = ", String.valueOf(model.getY()));


                    double sumOfSquares = Math.pow(toIntX, 2) + Math.pow(toIntY, 2);
                    float radius = (float) (11F - Math.sqrt(sumOfSquares) / Constant.RADIUS);
                    if (BinaryUtils.isPointInTriangle(toIntX, toIntY)) {
                        //11还等于10.10 强制修改 并保留一位小数
                        model.setRing((radius * 10F) / 10F);
                        model.setMiss(true);
                    } else {
                        model.setRing(0);
                        model.setMiss(false);
                    }

                    break;
                case 10:
                    break;
            }
        }
        return model;
    }

    /**
     * 根据原始数据， 获取随机 正负 1% 的值
     *
     * @param originValue
     * @return
     */
    public float getMockValue(float originValue) {
        // 创建一个Random对象
        Random random = new Random();

        // 生成一个0到0.01之间的随机数
        double randomValue = random.nextDouble() * 0.05;

        // 随机决定正负号
        if (random.nextBoolean()) {
            randomValue *= -1; // 使随机数为负数
        }

        return (float) (originValue * (1 + randomValue));
    }

    /**
     * 计算据枪 最大差值距离算法
     *
     * @param givenEntry 开枪的点
     * @param entry      向后找的5个数据
     * @return 距离
     */
    public float getMaxDistance(Entry givenEntry, Entry entry) {
        float maxDistance = 0.0f;

        float distance = getDistance(givenEntry, entry);
        if (distance > maxDistance) {
            maxDistance = distance;
        }

        return maxDistance;
    }

    public float getDistance(Entry entry1, Entry entry2) {
        float xDiff = entry2.getX() - entry1.getX();
        float yDiff = entry2.getY() - entry1.getY();
        return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public String getEightWay(float x, float y) {
        if (x > 0 && y > 0)
            return "右上";
        else if (x < 0 && y > 0)
            return "左上";
        else if (x < 0 && y < 0)
            return "左下";
        else if (x > 0 && y < 0)
            return "右下";
        else if (x == 0 && y > 0)
            return "正上";
        else if (x == 0 && y < 0)
            return "正下";
        else if (x > 0 && y == 0)
            return "正右";
        else if (x < 0 && y == 0)
            return "正左";
        else if (x == 0 && y == 0) {
            return "正中";
        }  else {
            return "脱靶";
        }
    }

}
