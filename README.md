##一、前言

最近项目里面需要用到多个seekbar并且需要显示不同的精度，于是模仿了华为的天气刻度盘添加了精度控制和自己定义的seekbar实现了如下效果(画面模糊是因为录制Gif图的工具导致的)。


本控件最低兼容到API16  更低的版本没有getThumb()函数

![](img/icon1.gif)

##二、刻度盘部分

![](img/icon2.gif)

* 1.刻度：刻度的绘制类似于绘制时钟刻度绘制出一条刻度通过旋转canvas绘制出其他刻度
* 2.波浪：波浪的绘制是通过两个正弦函数y = Asin(wx+b)+h  实时变换sin的初相
* 3.精度控制：通过设定 精度模式，最大进度值和显示的最大值共同决定。最大进度值/当前进度值=实际显示的最大值/当前显示的值



##三、seekbar部分

![](img/icon3.gif)

* 1.thumb（滑块）：通知扩展已有的seekbar控件获取滑块位置并且通过canvas绘制图像和进度的数值

* 2.进度线：进度线条是一个圆角的矩形通过滑块的位置绘制矩形的长度,滑块左边一根右边一根拼起来

* 3.尺寸的确定都是基于滑块而绘制的所以在初始化时需要先有一个确定尺寸的滑块，我是在drawable中定义的一个只有尺寸的滑块，这样做的缺陷在于在XML布局文件中的android:layout_height="" 任何设置均无效设置成wrap_content即可，要调整大小就修改drawable中绘制的滑块的尺寸

* 4.精度控制：如果api>24可以直接通过setTickMark(Drawable tickMark);设置Drawable下的图形，如果是低版本在drawable中绘制一条居中的实线，通过setBackground(Drawable background);绘制出来

##四、使用
* 1.布局文件

        <com.hubin.scaleseekbar.DigitalThumbSeekbar
                    android:id="@+id/seekbar"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:max="100"
                    app:PrecisionMode="float_two"
                    app:maxShowValue="0.5"/>

* 2.使用

         SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekbar);

         WaveDialSeekbarListener mWaveDialSeekbarListener = new WaveDialSeekbarListener(this,
           mSeekBar.getMax(), 0.5f, "发音间隔", "秒", WaveDialView.PRECISION_MODE_TWO_DECIMAL_PLACES);
         mSeekBar.setOnSeekBarChangeListener(mWaveDialSeekbarListener1);

