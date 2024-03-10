package org.ggxz.shoot.mvp.view.activity;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * 这个类就不放在Presenter里了 专门为View服务的
 */
public class MainAvtivityHelper {

    private Context context;

    public MainAvtivityHelper(Context context) {
        this.context = context;
    }

    public MainAvtivityHelper() {
    }

    public void configLineChart(LineChart chart) {
        /*=====chart=====*/
        chart.setBackgroundColor(Color.TRANSPARENT);//背景透明
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);// 是否绘制背景
        chart.setDrawBorders(true);//是否绘制边框
        chart.setScaleEnabled(false);//缩放图表

        //隐藏高亮十字
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);


        chart.getAxisRight().setEnabled(false); //Y轴右侧显示
        chart.getAxisLeft().setEnabled(true);//Y轴左侧显示

        chart.setDragEnabled(false);

        /*图例设置*/
        chart.getLegend().setEnabled(false);

        /*X轴线*/
        XAxis xAxis = chart.getXAxis();

        xAxis.setEnabled(true);
        xAxis.setLabelCount(7, true);
        xAxis.setDrawAxisLine(true);// 是否显示x轴线
        xAxis.setDrawGridLines(true);//是绘制y方向网格线
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisLineWidth(2f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";//相当于隐藏
            }
        });

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setLabelCount(11, true);
        yAxis.setDrawAxisLine(true);// 是否显示x轴线
        yAxis.setDrawGridLines(true);//是绘制y方向网格线
        yAxis.setAxisLineColor(Color.WHITE);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisMinimum(1);
        yAxis.setGranularity(1);
        yAxis.setAxisMaximum(11);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";//相当于隐藏
            }
        });

        /*=====chart end=====*/

    }

    public void configLineChartFragment(LineChart chart) {
        /*=====chart=====*/
        chart.setBackgroundColor(Color.TRANSPARENT);//背景透明
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);// 是否绘制背景
        chart.setDrawBorders(true);//是否绘制边框
        chart.setScaleEnabled(false);//缩放图表

        //隐藏高亮十字
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);


        chart.getAxisRight().setEnabled(false); //Y轴右侧显示
        chart.getAxisLeft().setEnabled(true);//Y轴左侧显示

        chart.setDragEnabled(false);

        /*图例设置*/
        chart.getLegend().setEnabled(false);

        /*X轴线*/
        XAxis xAxis = chart.getXAxis();

        xAxis.setEnabled(true);
        xAxis.setLabelCount(10, true);
        xAxis.setDrawAxisLine(false);// 是否显示x轴线
        xAxis.setDrawGridLines(true);//是绘制y方向网格线
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisLineWidth(3f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";//相当于隐藏
            }
        });

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setLabelCount(12, true);
        yAxis.setDrawAxisLine(true);// 是否显示x轴线
        yAxis.setDrawGridLines(true);//是绘制y方向网格线
        yAxis.setAxisMaximum(6F);
        yAxis.setAxisMinimum(-6F);
        yAxis.setAxisLineColor(Color.WHITE);
        yAxis.setAxisLineWidth(3f);
        yAxis.setDrawZeroLine(true);
        yAxis.setZeroLineWidth(3f);
        yAxis.setZeroLineColor(Color.WHITE);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";//相当于隐藏
            }
        });

        /*=====chart end=====*/

    }
}
