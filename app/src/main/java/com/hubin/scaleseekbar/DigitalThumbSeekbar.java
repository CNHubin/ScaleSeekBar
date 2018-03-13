package com.hubin.scaleseekbar;

/*
 *  @项目名：  ScaleSeekBar 
 *  @包名：    com.hubin.scaleseekbar
 *  @文件名:   DigitalThumbSeekbar
 *  @创建者:   胡英姿
 *  @创建时间:  2018-03-10 14:38
 *  @描述：    自定义进度显示在thumb上的Seekbar
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import java.text.DecimalFormat;

public class DigitalThumbSeekbar extends AppCompatSeekBar {

    private Paint mTextPaint;
    private Paint mLinePaint;
    private RectF mRectF;
    private Paint mCirclePaint;
    private Paint mCirclePaint2;

    private Drawable mThumb;


    private float mMaxShowValue; //需要显示的最大值
    private int mPrecisionMode;//精度模式
    private int mViewWidth;
    private int mCenterX;
    private int mCenterY;
    private int mThumbHeight;

    public DigitalThumbSeekbar(Context context) {
        this(context, null);
    }


    public DigitalThumbSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalThumbSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DigitalThumbSeekbar);
        mMaxShowValue = typedArray.getFloat(R.styleable.DigitalThumbSeekbar_maxShowValue, getMax());//获取最大显示值
        mPrecisionMode = typedArray.getInt(R.styleable.DigitalThumbSeekbar_PrecisionMode, 0);//进度模式
        typedArray.recycle();//释放

        //设置滑块样式
        mThumb = context.getResources().getDrawable(R.drawable.circle_thumb);
        setThumb(mThumb);
        setThumbOffset(0);
        initPaint();//初始化画笔
    }

    private void initPaint() {
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setARGB(255, 255, 255, 255);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mThumb.getMinimumHeight() * 3 / 7);//文字大小为滑块高度的2/3
        mTextPaint.setTextAlign(Paint.Align.CENTER); // 设置文本对齐方式，居中对齐

        //进度画笔
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);

        //实心圆
        mCirclePaint = new Paint();
//        mCirclePaint.setColor(0xFFFFFFFF);
        mCirclePaint.setARGB(255, 255, 65, 130);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        //空心圆
        mCirclePaint2 = new Paint();
        mCirclePaint2.setARGB(255, 255, 255, 255);
        mCirclePaint2.setAntiAlias(true);
        mCirclePaint2.setStrokeWidth(mThumb.getMinimumHeight() / 20);
        mCirclePaint2.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //父容器传过来的宽度的值
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
       //根据滑块的尺寸确定大小 布局文件中的android:layout_height="" 任何设置不会改变绘制的大小
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mThumb.getMinimumHeight(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取滑块坐标
        Rect thumbRect = getThumb().getBounds();
        mCenterX = thumbRect.centerX();//中心X坐标
        mCenterY = thumbRect.centerY(); //中心Y坐标
        mThumbHeight = thumbRect.height();//滑块高度

        //绘制进度条
        drawRect(canvas);

        //绘制滑块
        canvas.drawCircle(mCenterX, mCenterY, mThumbHeight / 2, mCirclePaint);
        canvas.drawCircle(mCenterX, mCenterY, mThumbHeight / 2 - mThumbHeight / 20, mCirclePaint2);//描边

        //绘制进度文字
        drawProgress(canvas);
    }

    /**
     * 绘制进度条
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        //绘制左边的进度
        mRectF = new RectF();
        mRectF.left = 0;
//        mRectF.right = thumbRect.left;
        mRectF.right = mCenterX;
        mRectF.top = mCenterY - mThumbHeight / 4;
        mRectF.bottom = mCenterY + mThumbHeight / 4;
        mLinePaint.setColor(Color.GREEN);
        canvas.drawRoundRect(mRectF, mThumbHeight / 4, mThumbHeight / 4, mLinePaint);

        //绘制右边剩余的进度
        mRectF.left= mCenterX;
        mRectF.right = mViewWidth;
        mRectF.top = mCenterY - mThumbHeight / 15;
        mRectF.bottom = mCenterY + mThumbHeight / 15;
        mLinePaint.setARGB(255,255,65,130);
        canvas.drawRoundRect(mRectF, mThumbHeight / 15, mThumbHeight /15, mLinePaint);
    }


    /**
     * 绘制显示的进度文本
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        String progress;
        float score = mMaxShowValue*getProgress()/getMax();
        switch (mPrecisionMode) {
            case 1://小数点后一位
                float  f1Score   =  (float)(Math.round(score*10))/10;
                progress="" + f1Score;
                break;
            case 2://小数点后两位
                DecimalFormat fnum  =   new DecimalFormat("##0.00");
                progress=fnum.format(score);
                break;
            default://默认 整数模式
                progress="" +(int)score;
                break;
        }
        //测量文字高度
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(progress, 0, progress.length(), bounds);
        int mTextHeight = bounds.height();//文字高度
//      float mTextWidth = mTextPaint.measureText(progress);
        canvas.drawText(progress, mCenterX, mCenterY + mTextHeight / 2, mTextPaint);
    }

}
