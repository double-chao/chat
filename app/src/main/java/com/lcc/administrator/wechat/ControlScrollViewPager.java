package com.lcc.administrator.wechat;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
*  @author lcc
*  created at 2018/12/3
 *
 *  控制viewPage不能滑动  还需要改成和这个类名相同的XML文件名
*/
public class ControlScrollViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public ControlScrollViewPager(Context context) {
        super(context);
    }

    public ControlScrollViewPager(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public void setScanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
