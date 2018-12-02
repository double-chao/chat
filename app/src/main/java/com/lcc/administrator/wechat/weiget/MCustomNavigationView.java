package com.lcc.administrator.wechat.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lcc.administrator.wechat.ControlScrollViewPager;


public class MCustomNavigationView extends View {

    private Context context;
    //上一次触摸行为的x坐标
    private int mLastX;
    //上一次触摸行为的y坐标
    private int mLastY;

    private Paint mPaint;
    //准备数据
    private String[] A_Z = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"
            , "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    //当前字母index
    private int currentChooseIndex = -1;

    //触摸滑动到当前字母时，界面中间重新显示的TextView
    private TextView showCurrentTextView;

    public void setShowCurrentTextView(TextView showCurrentTextView) {
        this.showCurrentTextView = showCurrentTextView;
    }

    private SortNavBarLetterChangeListener sortNavBarLetterChangeListener;

    public void setSortNavBarLetterChangeListener(SortNavBarLetterChangeListener sortNavBarLetterChangeListener) {
        this.sortNavBarLetterChangeListener = sortNavBarLetterChangeListener;
    }

    //定义接口  完成 ListView滑动到当前触摸的字母所对应的位置
    public interface SortNavBarLetterChangeListener {
         void OnLetterChange(String letter);
    }

    /**
     * 事件分发机制  默认返回false  由父控件向子控件分发，传递给onTouchEvent()进行事件的处理
     * return true;  拦截事件  不进行分发
     * <p>
     * 1、当前所对应的字母颜色发生变化
     * 2、界面展示一下触摸时所对应的字母
     * 3、通知ListView滑动到当前触摸的字母所对应的位置     (用接口回调的方法来实现)
     *
     * @param event
     * @return
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        //获取当前字母对应的Y坐标
        float y = event.getY();
        // index / length = Y / height  (当前字母索引 / 数组长度 = 当前字母所对应的Y坐标 / 控件的高度)
        //当前字母索引
        int index = (int) (Math.min(Math.max(y / getHeight(),0),0.99999f) * A_Z.length);

        //获取当前坐标的位置
        int x = (int) event.getX();
        int y1 = (int) event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(Color.GRAY);
                break;

            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                int dx = mLastX - x;
                int dy = mLastY - y1;
                setBackgroundColor(Color.GRAY);

                //Y方向的速度大于X方向的速度并且滑动的距离小于控件的高度
                if (Math.abs(dx) < Math.abs(dy)) {
                    //字母有变化
                    if (currentChooseIndex != index) {
                        //FragmentContacts中央显示一个TextView
                        if (showCurrentTextView != null) {
                            System.out.println(index+"------");
                            showCurrentTextView.setText(A_Z[index]);
                            showCurrentTextView.setVisibility(View.VISIBLE);
                        }

                        // 通知外界(ListView)，字母有变化
                        if (sortNavBarLetterChangeListener != null) {
                            sortNavBarLetterChangeListener.OnLetterChange(A_Z[index]);
                        }

                        //重绘 让当前字母颜色发生变化
                        currentChooseIndex = index; //获取改变滑动到的当前字母的颜色的坐标
                        invalidate(); //重绘  执行onDraw()
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
                break;

            case MotionEvent.ACTION_UP:
                //字母索引改为负数  让其和字母对应的索引不相等
                currentChooseIndex = -1;
                //FragmentContacts中央TextView隐藏
                showCurrentTextView.setVisibility(View.GONE);
                //把MCustomNavView控件背景改为原来的颜色
                setBackgroundColor(Color.rgb(248,248,255));
                break;
        }
        return true;
    }

    /**
     * 事件处理机制  默认返回false
     * return true; 处理事件，事件被拦截，不在向父控件传递，自己消耗
     *
     * @param event
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //获取控件的宽
        int width = getWidth();
        //获取控件的高
        int height = getHeight();
        //单行字母的高
        int signalHeight = height / A_Z.length;

        mPaint = new Paint();

        //遍历数组  开始画
        int i = 0; // i=0 是因为数组第一个数的下标为0
        for (String str : A_Z) {
            //字体加粗
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            //画笔大小
            mPaint.setTextSize(39);

            //x坐标  字母居中显示 = 控件的宽/2-字母宽/2
            float positionX = width / 2 - mPaint.measureText(str) / 2;

            //Y坐标  高度平均分  (乘以i的目的是：在往下加一个字母的高度就比上一个字母的高度多一个字母的高度)
            float positionY = signalHeight * (i+1); //因为i是从0开始计数的

            //改变选中字母的颜色
            if (i == currentChooseIndex) {
                //画笔颜色,绿色
                mPaint.setColor(Color.rgb(0, 255, 0));
            } else {
                //画笔颜色 ，黑色
                mPaint.setColor(Color.rgb(105, 105, 105));
            }
            canvas.drawText(str, positionX, positionY, mPaint);
            i++;
            //重置画笔
            mPaint.reset();
        }
        super.onDraw(canvas);
    }


    public MCustomNavigationView(Context context) {
        super(context);
    }

    public MCustomNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MCustomNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MCustomNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
