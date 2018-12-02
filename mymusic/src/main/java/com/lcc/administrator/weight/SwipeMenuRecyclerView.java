package com.lcc.administrator.weight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.lcc.administrator.adapter.RVAdapter;

/**
 * @author lcc
 * created at 2018/12/2
 */
public class SwipeMenuRecyclerView extends RecyclerView {

    private final String TAG = "SwipeMenuRecyclerView";
    //置顶
    private TextView tv_top;
    //删除
    private TextView tv_delete;
    //分享
    private ImageView iv_share;
    //item对应的布局
    private FrameLayout mItemLayout;
    //菜单的最大宽度
    private int mMaxLength;
    //上一次触摸X的坐标
    private int mLastX;
    //上一次触摸Y的坐标
    private int mLastY;
    //当前触摸item的位置
    private int mPosition;
    //实现弹性滑动  恢复
    private Scroller mScroller;
    //是否在垂直滑动列表
    private boolean isDragging;
    //item是在否跟随手指移动
    private boolean isItemMoving;
    //item是否开始自动滑动
    private boolean isStartScroll;
    //左滑菜单状态   0：关闭 1：将要关闭 2：将要打开 3：打开
    private int mMenuState;
    private static final int MENU_CLOSED = 0;
    private static final int MENU_WILL_CLOSED = 1;
    private static final int MENU_OPEN = 2;
    private static final int MENU_WILL_OPEN = 3;

    private RVAdapter.Holder holder;

    public SwipeMenuRecyclerView(Context context) {
        super(context, null);
        mScroller = new Scroller(context, new LinearInterpolator());
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mScroller = new Scroller(context, new LinearInterpolator());
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context, new LinearInterpolator());
    }

    /**
     * 事件处理机制
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: //按下
                //Menu关闭状态
                if (mMenuState == MENU_CLOSED) {
                    View view = findChildViewUnder(x, y);
                    if (view == null) {
                        return false;
                    }
                    //获得这个view的viewHolder
                    holder = (RVAdapter.Holder) getChildViewHolder(view);
                    //获得这个view的position
                    mPosition = holder.getAdapterPosition();
                    //获得view的整个布局  (获取的是item的整体布局)
                    mItemLayout = holder.frameLayout;
                    //获得这个view置顶TextView
                    tv_top = holder.tvTop;
                    //获得这个view删除TextView
                    tv_delete = holder.tvDelete;
                    //获得分享图片
                    iv_share = holder.iv_share;
                    //获取两个按钮的宽度
                    mMaxLength = tv_top.getWidth() + tv_delete.getWidth();
                }else if(mMenuState == MENU_OPEN){
                    //(mItemLayout获取在Activity左边界的值/也就是滑动的距离
                    // dx:在X轴上的偏移量,  dy:在y轴上的偏移量,  时间)
                    mScroller.startScroll(mItemLayout.getScrollX(), 0, -mMaxLength, 0, 200);
                    invalidate();
                    mMenuState = MENU_CLOSED;
                    //该点击无效
                    return false;
                }else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量（上一次点击的坐标-现在所在的坐标的位置）
                int dx = mLastX - x;
                int dy = mLastY - y;

                //当前滑动的layout在Activity左边界的值（也就是滑动的距离X）
                int scrollX = mItemLayout.getScrollX();
                //X方向的速度大于Y轴的速度
                if (Math.abs(dx) > Math.abs(dy)) {
                    isItemMoving = true;
                    if (scrollX + dx <= 0) {//超出左边界则始终保持不动,滑动无效
                        mItemLayout.scrollTo(0, 0);
                        return false;
                    }
                    else if (scrollX + dx >= mMaxLength) { //超出右边界则始终保持不动,滑动无效
                        mItemLayout.scrollTo(mMaxLength, 0);
                        return false;
                    }
                    //菜单随着手指移动
                    mItemLayout.scrollBy(dx, 0);

                } else if (Math.abs(dx) > 10) {//如果水平移动距离大于10像素的话，recyclerView不会上下滑动
                    return true;
                }
                if (isItemMoving || mMenuState == 3) {//如果菜单正在打开就不能上下滑动
                    mLastX = x;
                    mLastY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时判断是否点击,静止才有Listener才能点击
//                if (!isItemMoving && !isDragging && mListener != null) {
//                    mListener.OnItemClick(mPosition);
//                }
                isItemMoving = false;
                //等一下控件要自动展开的距离（控件未展示出来的部分）
                int deltaX = 0;
                //当前滑动的在该窗口左边界X轴的距离(也就是滑动过的距离)
                int upScrollX = mItemLayout.getScrollX();

                //滑动距离大于1/3menu长度就自动展开，否则就关掉
                if (upScrollX > mMaxLength / 3) {

                    //控件的宽度 - 滑动过的距离 = 控件剩下未显示的部分
                    deltaX = mMaxLength - upScrollX;
                    mMenuState = MENU_WILL_OPEN;
                } else if (upScrollX <= mMaxLength / 3) {
                    deltaX = -upScrollX;
                    mMenuState = MENU_WILL_CLOSED;
                }

                //知道我们为什么不直接把mMenuState赋值为MENU_OPEN或者MENU_CLOSED吗？
                // 因为滑动时有时间的，我们可以在滑动完成时才把状态改为已经完成
                mScroller.startScroll(upScrollX, 0, deltaX, 0, 200);
                isStartScroll = true;
                //刷新界面
                invalidate();
                break;
        }
        //只有更新mLastX、mLastY  ，数据才会准确
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        //判断scroller是否完成滑动
        if (mScroller.computeScrollOffset()) {
            mItemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else if (isStartScroll) {//如果已经完成就改变状态
            isStartScroll = false;
            if (mMenuState == MENU_WILL_CLOSED) {
                mMenuState = MENU_CLOSED;
            }

            if (mMenuState == MENU_WILL_OPEN) {
                mMenuState = MENU_OPEN;
            }
        }
        super.computeScroll();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //是否在上下滑动
        isDragging = state == SCROLL_STATE_DRAGGING;
    }
}
