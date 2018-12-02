package com.lcc.administrator.wechat.chat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * baseActivity 基类  自带toolbar
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BaseActivityHelper mBaseActivityHelper;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 将Toolbar和自定义的activity的layout布局（View） 按照上下位置放置成为整个Activity的布局
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        mBaseActivityHelper = new BaseActivityHelper(this, layoutResID);
        toolbar = mBaseActivityHelper.getmToolbar();
        super.setContentView(mBaseActivityHelper.getmContentView());
        customerToolbarBeforeSet();
        setSupportActionBar(toolbar);
        customerToolbarAfterSet();
    }

    public void customerToolbarBeforeSet() {
        setUpTitle();
    }

    public void customerToolbarAfterSet() {
        setUpHomeEnable();
        setUpNavigationIcon();
    }

    //设置toolbar标题
    public abstract void setUpTitle();

    //
    public abstract void setUpHomeEnable();

    //设置导航图标
    public abstract void setUpNavigationIcon();

    /**
     * 设置toolbar的标题
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     * 设置toolbar的导航图标
     *
     * @param iconResId
     */
    public void setToolbarNavigationIcon(int iconResId) {
        toolbar.setNavigationIcon(iconResId);
    }

    /**
     * 设置toolbar 返回是否可用
     *
     * @param enable
     */
    public void setHomeUpEnable(boolean enable) {
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(enable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }

    protected void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    protected void showToast(int resId, Context context) {
        Toast.makeText(context, getResources().getText(resId), Toast.LENGTH_LONG).show();
    }

}
