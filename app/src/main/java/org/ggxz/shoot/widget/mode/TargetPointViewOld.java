package org.ggxz.shoot.widget.mode;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.common_module.db.mode.EntryModel;

import org.ggxz.shoot.R;
import org.ggxz.shoot.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TargetPointViewOld extends View {
    private static final int MAX_RING_COUNT = 10; // 最大环数
    private static final float RING_WIDTH_DP = 20; // 环的宽度，单位dp
    private static final float CENTER_RADIUS_DP = 10; // 中心点半径，单位dp
    private static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE; // 默认背景色

    private Paint mPaint;
    private int mRingCount; // 环数
    private float mRingWidth; // 环宽度
    private float mCenterRadius; // 中心点半径
    private int mBackgroundColor; // 背景色
    private int[] mRingColors = new int[]{
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
    }; // 环的颜色数组

    private int[] mLineColors = new int[]{
            Color.GRAY,
            Color.GREEN,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    }; // 环的颜色数组
    // 定义环数文本的画笔
    private Paint mTextPaint;
    /*=======CoordinateView=========*/
    private float xMin, xMax, yMin, yMax;
    //    private List<Float> xValues, yValues;
    private List<EntryModel> entries = new ArrayList<>();
    private List<EntryModel> changeEntries = new ArrayList<>();

    private Paint axisPaint, pointPaint, textPaint, linePaint;

    private int cur = 0;
    private final HashSet<Integer> set = new HashSet<>();
    private ValueAnimator mAnimator;
    private Path path;
    private Path pathPeo = new Path();
    private Bitmap bitmap;

    /*=======CoordinateView=========*/
    public TargetPointViewOld(Context context) {
        this(context, null);
    }

    public TargetPointViewOld(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceType")
    public TargetPointViewOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initCoordinate();
    }

    private void initCoordinate() {
        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setStrokeWidth(10f);

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(5f);


        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1F);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setAntiAlias(true);
        PathEffect pathEffect = new CornerPathEffect(10); // radius 是圆角半径
        linePaint.setPathEffect(pathEffect);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setStrokeWidth(4f);

        path = new Path();

    }

    private void init() {
        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(dp2px(1));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        // 环数文本的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dp2px(14)); // 设置文本的字体大小
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        // 初始化默认值
        mRingCount = MAX_RING_COUNT;
        mRingWidth = dp2px(RING_WIDTH_DP);
        mCenterRadius = dp2px(CENTER_RADIUS_DP);
        mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        bitmap = getBitmap(getContext(), R.drawable.boom);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.WHITE);

        // 计算中心点坐标
        int centerX = getWidth() / 2;
        int centerY = getWidth() / 2;

        // 绘制背景色
        canvas.drawColor(mBackgroundColor);

      /*  // 绘制环
        float radius = Math.min(centerX, centerY);
        mRingWidth = radius / (mRingCount * 1f);

        float lastRadius = -1;
        for (int i = 0; i < mRingCount; i++) {
            float ringRadius = radius - i * mRingWidth;
            lastRadius = ringRadius;
            mPaint.setColor(getRingColor(i));
            canvas.drawCircle(centerX, centerY, ringRadius, mPaint);


            // 绘制环数文本
            String text = String.valueOf(i + 1);
            float textX = centerX + ringRadius - mRingWidth / 2;
            float textY = centerY + mRingWidth / 4;
            canvas.drawText(text, textX, textY, mTextPaint);

        }

        // 绘制中心点
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, lastRadius, mPaint);
        // 绘制环数文本
        String text = String.valueOf(10);
        float textX = centerX;
        float textY = centerY + mRingWidth / 4;
        mTextPaint.setColor(Color.BLACK);
        canvas.drawText(text, textX, textY, mTextPaint);*/


        /*=======CoordinateView=========*/

        int width = getWidth();
        int height = getWidth();
        int xOrigin = width / 2;
        int yOrigin = height / 2;


        //hint 暂时把坐标轴取消掉，对比数值时再显示
        // 绘制X轴
    /*    canvas.drawLine(0, yOrigin, width, yOrigin, axisPaint);
        // 绘制X轴刻度值
//        float xStep = (xMax - xMin) / 10;//刻度强制为1
        float xStep = 1;
        for (float i = xMin + xStep; i <= xMax; i += xStep) {
            float x = xOrigin + i * (width / (xMax - xMin));
            canvas.drawText(String.valueOf((int) i), x, yOrigin + 50, textPaint);
            canvas.drawLine(x, yOrigin - 20, x, yOrigin, axisPaint);
        }

        // 绘制Y轴
        canvas.drawLine(xOrigin, 0, xOrigin, height, axisPaint);
        // 绘制Y轴刻度值
        float yStep = 1;
        for (float i = yMin + yStep; i < yMax; i += yStep) {
            float y = yOrigin - i * (height / (yMax - yMin));
            canvas.drawText(String.valueOf((int) i), xOrigin - 80, y + 20, textPaint);
            canvas.drawLine(xOrigin, y, xOrigin + 20, y, axisPaint);
        }
*/

        // 绘制点

        List<EntryModel> entryList;
        if (mAnimator == null) {
            entryList = entries;
        } else {
            entryList = changeEntries;
        }
        for (int i = 0; i < entryList.size() - 1; i++) {//todo 这里的-1是什么意思？

            float x = xOrigin + entryList.get(i).getX() * (width / (xMax - xMin));
            float y = yOrigin - entryList.get(i).getY() * (height / (yMax - yMin));

            float xN = xOrigin + entryList.get(i + 1).getX() * (width / (xMax - xMin));
            float yN = yOrigin - entryList.get(i + 1).getY() * (height / (yMax - yMin));


            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
//            canvas.drawPath(path, linePaint);

//            linePaint.setColor(mLineColors[i % 5]);//todo 动态修改颜色
            canvas.drawLine(x, y, xN, yN, linePaint);

        }
//
//        for (int i = 0; i < entryList.size(); i++) {
//            float x = xOrigin + entryList.get(i).getX() * (width / (xMax - xMin));
//            float y = yOrigin - entryList.get(i).getY() * (height / (yMax - yMin));
//
//            if (i == 0) {
//                path.moveTo(x, y);
//            } else {
//                path.lineTo(x, y);
//            }
//
//            canvas.drawBitmap(bitmap, x - dp2px(10), y - dp2px(10), pointPaint);
//        }


//        canvas.drawPath(path, linePaint);

        /*=======CoordinateView=========*/

        /*=============People================*/
        // 绘制射击标靶
//
//        pathPeo.moveTo(centerX - radius / 2, centerY + radius / 4);
//        pathPeo.lineTo(centerX - radius / 4, centerY + radius / 2);
//        pathPeo.lineTo(centerX + radius / 4, centerY + radius / 2);
//        pathPeo.lineTo(centerX + radius / 2, centerY + radius / 4);
//        pathPeo.lineTo(centerX + radius / 2, centerY - radius / 2);
//        pathPeo.lineTo(centerX - radius / 2, centerY - radius / 2);
//        pathPeo.close();
//        canvas.drawPath(pathPeo, mPaint);
        /*=============People================*/

    }

    // 根据环数获取环的颜色
    private int getRingColor(int ringIndex) {
        if (ringIndex == mRingCount - 1) {
            return mBackgroundColor;//这么设置后第10环虽然会画出来 但因为和背景色相同所以是看不出来的
        } else {
            return mRingColors[ringIndex];
        }
    }

    // 设置环的颜色数组
    public void setRingColors(int[] ringColors) {
        mRingColors = ringColors;
        invalidate();
    }

    // dp转px
    private float dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    // 设置环数
    public void setRingCount(int ringCount) {
        mRingCount = Math.max(1, Math.min(MAX_RING_COUNT, ringCount));
        invalidate();
    }


    // 设置环宽度
    public void setRingWidth(float ringWidth) {
        mRingWidth = ringWidth;
        invalidate();
    }

    // 设置中心点半径
    public void setCenterRadius(float centerRadius) {
        mCenterRadius = centerRadius;
        invalidate();
    }

    // 设置背景色
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        invalidate();
    }

    // 获取文本的高度
    private float getTextHeight(String text) {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /*=======CoordinateView=========*/
    public void setXAxisRange(float xMin, float xMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        invalidate();
    }

    public void setYAxisRange(float yMin, float yMax) {
        this.yMin = yMin;
        this.yMax = yMax;
        invalidate();
    }

    public void setValues(List<EntryModel> entries) {
        this.entries = entries;
        invalidate();
    }
    /*=======CoordinateView=========*/

    public void setAnimationXAxisMills(long mills) {
        mAnimator = ValueAnimator.ofInt(0, entries.size() - 1);
        mAnimator.setDuration(mills);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> {
            cur = (int) animation.getAnimatedValue();
            if (!set.contains(cur)) {
                // 更新点的位置
                changeEntries.add(entries.get(cur));
                LogUtils.i("TAG----", animation.getAnimatedValue() + "");

                invalidate();
                set.add(cur);
            }

        });
        mAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
    }

    public Bitmap getBitmap(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        TypedValue value = new TypedValue();
        context.getResources().openRawResource(resId, value);
        options.inTargetDensity = value.density;
        options.inScaled = false;//不缩放
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        float scaleWidth = dp2px(20F) / bitmap.getWidth();
        float scaleHeight = dp2px(20F) / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
