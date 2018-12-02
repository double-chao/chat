package com.lcc.administrator.wechat.chat;

import android.content.Context;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lcc.administrator.wechat.R;

public class BaseActivityHelper {

    private Context mContext;
    //视图构造器
    private LayoutInflater layoutInflater;
    //整体布局
    private RelativeLayout mContentView;
    //用户自定义View
    private View mUserView;
    //toolbar
    private Toolbar mToolbar;

    public BaseActivityHelper(Context context, int layoutRes) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        //初始化整体布局
        initMContentView();
        //初始化Toolbar
        initToolBar();
        //初始化自定义View
        initUserView(layoutRes);
    }

    /**
     * 初始化整体布局 mContentView
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
        View toolbarLayout = layoutInflater.inflate(R.layout.layout_toolbar, mContentView);
        mToolbar= (Toolbar) toolbarLayout.findViewById(R.id.toolbar);
    }

    /**
     * 初始化自定义View
     */
    private void initUserView(int layoutRes) {
        if(mToolbar==null){
            initToolBar();
        }
        mUserView = layoutInflater.inflate(layoutRes, null);
        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //把自定义的View放到Toolbar的下面
        layoutParams.addRule(RelativeLayout.BELOW,mToolbar.getId());
        //将自定义View加载到整体布局中
        mContentView.addView(mUserView,layoutParams);
    }

    public Toolbar getmToolbar(){
        return mToolbar;
    }

    public View getmUserView(){
        return mUserView;
    }

    public View getmContentView(){
        return mContentView;
    }

}
