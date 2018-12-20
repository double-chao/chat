package com.lcc.administrator.mymusic;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * @author lcc
 * created at 2018/12/8
 */
public class BaseActivityHelper {
    private Context mContext;
    private LayoutInflater layoutInflater;
    //整体布局
    private RelativeLayout mContentView;
    private Toolbar toolbar;
    //自定义View
    private View mUserView;

    public BaseActivityHelper(Context mContext, int layoutRes) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        initMContentView();
        initToolBar();
        initUserView(layoutRes);
    }

    /**
     * 初始化整体布局
     */
    private void initMContentView() {
        mContentView = new RelativeLayout(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(layoutParams);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        if (mContentView == null) {
            initMContentView();
        }
        View toolBarLayout = layoutInflater.inflate(R.layout.layout_toolbar, mContentView);
        toolbar = toolBarLayout.findViewById(R.id.layout_toolbar);
    }

    /**
     *初始化自定义View
     */
    private void initUserView(int layoutRes) {
        if (toolbar == null) {
            initToolBar();
        }
        mUserView = layoutInflater.inflate(layoutRes, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //把自定义的View放到Toolbar的下面
        layoutParams.addRule(RelativeLayout.BELOW, toolbar.getId());
        //将自定义View加载到整体布局中
        mContentView.addView(mUserView, layoutParams);
    }

    public Toolbar getmToolbar(){
        return toolbar;
    }

    public View getmUserView(){
        return mUserView;
    }

    public View getmContentView(){
        return mContentView;
    }
}
