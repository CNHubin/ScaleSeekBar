package com.hubin.scaleseekbar;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SeekBar mSeekBar1;
    private SeekBar mSeekBar2;
    private SeekBar mSeekBar3;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = View.inflate(this, R.layout.activity_main,null);
        setContentView(mRootView);
        mSeekBar1 = (SeekBar) findViewById(R.id.seekbar1);
        mSeekBar2 = (SeekBar) findViewById(R.id.seekbar2);
        mSeekBar3 = (SeekBar) findViewById(R.id.seekbar3);

        WaveDialSeekbarListener mWaveDialSeekbarListener1 = new WaveDialSeekbarListener(this,
                mSeekBar1.getMax(), 0.5f, "发音间隔", "秒", WaveDialView.PRECISION_MODE_TWO_DECIMAL_PLACES);
        mSeekBar1.setOnSeekBarChangeListener(mWaveDialSeekbarListener1);


        WaveDialSeekbarListener mWaveDialSeekbarListener2 = new WaveDialSeekbarListener(this,
                mSeekBar2.getMax(), 2, "两轮间隔", "秒", WaveDialView.PRECISION_MODE_ONE_DECIMAL_PLACES);
        mSeekBar2.setOnSeekBarChangeListener(mWaveDialSeekbarListener2);


        WaveDialSeekbarListener mWaveDialSeekbarListener3 = new WaveDialSeekbarListener(this,
                mSeekBar3.getMax(), 100, "亮度", "", WaveDialView.PRECISION_MODE_INTEGER);
        mSeekBar3.setOnSeekBarChangeListener(mWaveDialSeekbarListener3);

        mSeekBar3.setProgress(50);
    }

    public void setBgColor(@ColorInt int color) {
        mRootView.setBackgroundColor(color);
    }
}
