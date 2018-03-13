package com.hubin.scaleseekbar;

/*
 *  @项目名：  ScaleSeekBar 
 *  @包名：    com.hubin.scaleseekbar
 *  @文件名:   WaveDialSeekbarListener
 *  @创建者:   胡英姿
 *  @创建时间:  2018-03-09 16:26
 *  @描述：    一个将SoftDialView 包装在里面的seekbar监听器
 */

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import java.lang.ref.WeakReference;

public class WaveDialSeekbarListener implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "WaveDialSeekbarListener";
    private WeakReference<MainActivity> mActivityWeakReference;  //activity的弱应用
    private WaveDialView mWaveDialView;
    private PopupWindow mPopupWindow;
    private int mMaxProgress; //最大进度值
    private float mMaxShowValue; //最大显示值
    private String textUnit = ""; //右上角文字 单位
    private String textName = "";//中间要显示的文字名字

    private int precisionModeDefuale;//精度模式
    /**
     * 构造函数
     * @param activity activity的引用
     * @param maxProgress seekbar 的最大进度值
     * @param maxShowValue 刻度盘上需要显示的最大值
     * @param textName  中间要显示的文字名字
     * @param textUnit  右上角文字 单位
     * @param precisionModeDefuale 精度模式  整数，1位小数，2位小数
     */
    public WaveDialSeekbarListener(MainActivity activity, int maxProgress, float maxShowValue, String textName, String textUnit, int precisionModeDefuale) {
        mActivityWeakReference = new WeakReference(activity);
        mMaxProgress= maxProgress;
        mMaxShowValue = maxShowValue;
        this.textName = textName;
        this.textUnit = textUnit;
        this.precisionModeDefuale = precisionModeDefuale;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mWaveDialView != null) {
            mWaveDialView.setChange((float) progress*300/mMaxProgress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mWaveDialView = new WaveDialView(mActivityWeakReference.get());
        mWaveDialView.setInitConfig(precisionModeDefuale,mMaxShowValue,textName,textUnit);
        mWaveDialView.moveWaterLine();
        mWaveDialView.setOnAngleColorListener(onAngleColorListener);//颜色监听器 可自行设置
        //窗口宽度
        int windowWidth = mActivityWeakReference.get().getWindowManager().getDefaultDisplay().getWidth();

        mPopupWindow = new PopupWindow(mWaveDialView, windowWidth/2, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.showAtLocation(View.inflate(mActivityWeakReference.get(),R.layout.activity_main, null),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);    //显示位置
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mWaveDialView=null;
        mPopupWindow.dismiss();
    }



    private WaveDialView.OnAngleColorListener onAngleColorListener=new WaveDialView.OnAngleColorListener() {
        @Override
        public void onAngleColorListener(int red, int green) {
            int c=Color.argb(150, red, green, 0);
            mActivityWeakReference.get().setBgColor(c);
        }
    };
}
