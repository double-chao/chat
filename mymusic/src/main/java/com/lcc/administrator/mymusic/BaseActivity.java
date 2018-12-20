package com.lcc.administrator.mymusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * @author lcc
 * created at 2018/12/8
 * 基类 自带ToolBar
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BaseActivityHelper baseActivityHelper;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        baseActivityHelper = new BaseActivityHelper(this, layoutResID);
        toolbar = baseActivityHelper.getmToolbar();
        super.setContentView(baseActivityHelper.getmContentView());
        setUpTitle();
        setSupportActionBar(toolbar);
        setUpHomeEnable();
        setUpNavIcon();
    }

    /**
     * 标题
     */
    public abstract void setUpTitle();

    /**
     * 是否可以返回上一个activity
     */
    public abstract void setUpHomeEnable();

    /**
     * 图标
     */
    public abstract void setUpNavIcon();

    /**
     * 设置toolbar的标题
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     * 设置toolbar 返回是否可用
     *
     * @param enable
     */
    public void setToolbarHomeEnable(boolean enable) {
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(enable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }

    /**
     * 设置toolbar的导航图标
     *
     * @param iconResId
     */
    public void setNavIcon(int iconResId) {
        toolbar.setNavigationIcon(iconResId);
    }
}
