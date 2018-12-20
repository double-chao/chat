package com.lcc.administrator.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lcc.administrator.mymusic.SortNavBarLetterChangeListener;

/**
 * @author lcc
 * created at 2018/12/19
 * 自定义View
 */
public class SortNavBar extends View {
    //画笔
    private Paint mPaint;
    //字符数组
    private String[] A_Z = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    //当前字母下标
    private int currentChooseIndex = -1;
    //触摸滑动到当前字母时，界面中间重新显示字母下标所对应的TextView
    private TextView showCurrentTextView;

    public void setCurrentTextView(TextView showCurrentTextView) {
        this.showCurrentTextView = showCurrentTextView;
    }
    //自定义接口
    private SortNavBarLetterChangeListener sortNavBarLetterChangeListener;

    public void setSortNavBarLetterChangeListener(SortNavBarLetterChangeListener sortNavBarLetterChangeListener) {
        this.sortNavBarLetterChangeListener = sortNavBarLetterChangeListener;
    }

    public SortNavBar(Context context) {
        super(context);
    }

    public SortNavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SortNavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 事件分发机制  默认返回false  由父控件向子控件分发，传递给onTouchEvent()进行事件的处理
     * return true;  拦截事件  不进行分发
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //获取当前字母y轴的坐标
        float y = event.getY();
        //当前字母索引
        // index / length = Y / height  (当前字母索引 / 数组长度 = 当前字母所对应的Y坐标 / 控件的高度)
        int index = (int) (Math.min(Math.max(y / getHeight(), 0), 0.99999f) * A_Z.length);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                //滑动时，字母发生变化
                if (currentChooseIndex != index) {
                    //设置当前字母和让其显示
                    if (showCurrentTextView != null) {
                        showCurrentTextView.setText(A_Z[index]);
                        showCurrentTextView.setVisibility(View.VISIBLE);
                    }
                    // 通知外界(ListView)，字母有变化
                    if (sortNavBarLetterChangeListener != null) {
                        sortNavBarLetterChangeListener.OnLetterChange(A_Z[index]);
                    }
                    //获取改变滑动到的当前字母的颜色的坐标
                    currentChooseIndex = index;
                    //重绘  执行onDraw()
                    invalidate();
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                //恢复其原来的值  让其和字母对应的索引不相等
                currentChooseIndex = -1;
                //隐藏中间的textView
                showCurrentTextView.setVisibility(View.GONE);
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //控件的宽高
        int width = getWidth();
        int height = getHeight();
        //单个字母的高
        int signalHeight = height / A_Z.length;
        //初始化画笔
        mPaint = new Paint();
        int i = 0;
        for (String str : A_Z) {
            //字体加粗
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            //画笔大小
            mPaint.setTextSize(29f);
            //字母水平居中显示(控件宽/2 - 字母宽/2)
            float positionX = width / 2 - mPaint.measureText(str) / 2;
            //字母Y坐标 高度平均分  (乘以i的目的是：在往下加一个字母的高度就比上一个字母的高度多一个字母的高度)
            float positionY = signalHeight * (i + 1); //因为i是从0开始计数的

            if (i == currentChooseIndex) {
                //画笔颜色,绿色
                mPaint.setColor(Color.rgb(0, 255, 0));
            }else {
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

}
