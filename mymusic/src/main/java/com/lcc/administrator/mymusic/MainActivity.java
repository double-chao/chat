package com.lcc.administrator.mymusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcc
 * created at 2018/11/29
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private LinearLayout layout_music;
    private LinearLayout layout_contacts;
    private LinearLayout layout_conversation;
    private LinearLayout[] linearLayouts;

    private TextView textView_music;
    private TextView textView_contacts;
    private TextView textView_conversation;
    private TextView[] textViews;

    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private MMusicFragment mMusicFragment;
    private MContactsFragment mContactsFragment;
    private MConversationFragment mConversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPage();
        initTabButton();
    }

    /**
     * 初始化tabButton
     * initialize TabButton
     */
    private void initTabButton() {
        textView_music = (TextView) this.findViewById(R.id.tv_music);
        textView_contacts = (TextView) this.findViewById(R.id.tv_contacts);
        textView_conversation = (TextView) this.findViewById(R.id.tv_conversation);
        textViews = new TextView[]{textView_music, textView_contacts, textView_conversation};

        layout_music = (LinearLayout) this.findViewById(R.id.layout_music);
        layout_contacts = (LinearLayout) this.findViewById(R.id.layout_contacts);
        layout_conversation = (LinearLayout) this.findViewById(R.id.layout_conversation);
        linearLayouts = new LinearLayout[]{layout_music, layout_contacts, layout_conversation};
        //首次进程序时TabButton对应的样式
        setSelectedTabButton(0);
        //点击TabButton切换ViewPage并改变TabButton的样式
        switchPageTabButtonOnclick();
    }

    /**
     * 当前选中时TabButton所对应的样式
     *
     * @param index
     */
    private void setSelectedTabButton(int index) {
        linearLayouts[index].setSelected(true);
    }

    /**
     * TabButton默认样式
     */
    private void setResetTabButton() {
        for (LinearLayout layout : linearLayouts) {
            layout.setSelected(false);
        }
    }

    /**
     * 点击TabButton切换ViewPage并改变TabButton的样式
     */
    private void switchPageTabButtonOnclick() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.layout_music: //音乐
                        setSelectedPage(0);
                        break;
                    case R.id.layout_contacts: //通讯录
                        setSelectedPage(1);
                        break;
                    case R.id.layout_conversation: //会话
                        setSelectedPage(2);
                        break;
                }
            }
        };
        //为每个线性布局添加事件
        for (LinearLayout layout : linearLayouts) {
            layout.setOnClickListener(onClickListener);
        }
    }

    /**
     * 选中时当前TabButton的样式和ViewPage对应的页面
     *
     * @param index
     */
    private void setSelectedPage(int index) {
        mViewPager.setCurrentItem(index);
        setSelectedTabButton(index);
    }

    /**
     * 初始化ViewPage以及组合fragment
     * initialize ViewPage and combine fragment
     */
    private void initViewPage() {
        mViewPager = this.findViewById(R.id.viewPage);
        mFragmentList = new ArrayList<Fragment>();
        mMusicFragment = new MMusicFragment();
        mContactsFragment = new MContactsFragment();
        mConversationFragment = new MConversationFragment();
        mFragmentList.add(mMusicFragment);
        mFragmentList.add(mContactsFragment);
        mFragmentList.add(mConversationFragment);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        //加载适配器
        //loading adapter
        mViewPager.setAdapter(fragmentPagerAdapter);

        mViewPager.setOffscreenPageLimit(3);

        setViewPagerSelectedTabButtonChange();
    }

    /**
     * 滑动ViewPage 改变TabButton的样式
     *
     */
    private void setViewPagerSelectedTabButtonChange() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setResetTabButton();
                setSelectedPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
