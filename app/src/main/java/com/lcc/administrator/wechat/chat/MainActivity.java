package com.lcc.administrator.wechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.vo.MContacts;
import com.lcc.administrator.wechat.weiget.MCustomNavigationView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EMEventListener {

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    //滑动导航栏时，中间显示的TextView
    private TextView textView;
    //自定义导航栏
    private MCustomNavigationView mCustomNavigationView;

    //fragment_tab对应的ImageView
    private ImageView icon_chat;
    private ImageView icon_contacts;
    private ImageView icon_find;
    private ImageView icon_about_me;
    private ImageView[] icon_imageViews;

    //fragment_tab对应的TextView
    private TextView textView_chat;
    private TextView textView_contacts;
    private TextView textView_find;
    private TextView textView_about_me;
    private TextView[] textViews;

    //fragment_tab对应的LinerLayout布局
    private LinearLayout layout_chat;
    private LinearLayout layout_contacts;
    private LinearLayout layout_find;
    private LinearLayout layout_about_me;
    private LinearLayout[] linearLayouts;

    private Toolbar toolbar;

    private MainFragmentConversation mFragmentConversation;
    private MainFragmentContacts mFragmentContacts;
    private MainFragmentFind mFragmentFind;
    private MainFragmentAboutMe mFragmentAboutMe;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViewPage();
        initTabButton();
        initHXContactListener();
    }

    /**
     * HX 好友状态监听
     */
    private void initHXContactListener() {
        //好友状态的监听
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        //添加接收消息的监听
        EMChatManager.getInstance().registerEventListener(this);
        //通知SDK UI已经初始化完毕，注册了相应的receiver或者listener
        EMChat.getInstance().setAppInited();
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {

        switch (emNotifierEvent.getEvent()) {
            case EventDeliveryAck:      //已经发送的回执消息
                Log.i(TAG, "onEvent------->EventDeliveryAck");
                break;
            case EventNewCMDMessage:    //接收透传的消息
                Log.i(TAG, "onEvent------->EventNewCMDMessage");
                break;
            case EventNewMessage:   //接收新消息
                Log.i(TAG, "onEvent------->EventNewMessage");
                break;
            case EventOfflineMessage:   //接收离线消息
                Log.i(TAG, "onEvent------->EventOfflineMessage");
                break;
            case EventReadAck:     //已读消息回执
                Log.i(TAG, "onEvent------->EventReadAck");
                break;
            case EventConversationListChanged:  //通知会话列表
                Log.i(TAG, "onEvent------->EventConversationListChanged");
                break;
        }
    }

    private void reFreshConversationUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * 实现EMContactListener     监听好友状态
     */
    public class MyContactListener implements EMContactListener {
        private final String TAG = "MyContactListener";

        //增加的好友联系人 当用户收到邀请并同意时回调的方法
        @Override
        public void onContactAdded(List<String> list) {
            Log.i(TAG, "----onContactAdded----");
            outer:
            for (String username_tmp : list) {
                //得到通讯录的所有好友数据
                List<MContacts> current_MContacts = MyChatApplication.getInstance().getMContactsDataList();
                for (MContacts contacts : current_MContacts) {
                    //判断当前用户是否和 username_tmp 已经是好友了
                    if (username_tmp.equals(contacts.getUsername())) {
                        Log.i(TAG, "用户:" + username_tmp + "已经成为当前用户的好友-----");
                        continue outer; //跳出本次循环
                    }
                }
                Log.i(TAG, "-----增加的好友：" + username_tmp);
                //调用MContacts中的静态方法 把用户名设置成和昵称相同，在进行排序
                MContacts mContacts_tmp = MContacts.loadContact(username_tmp);
                MyChatApplication.getInstance().addContact(mContacts_tmp);
                //对界面进行操作必须在UI线程(主线程)中，不能在子线程中
                //这里还可以用Handle的Post机制
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新通讯录界面
                        mFragmentContacts.reFreshContactUI();
                    }
                });
            }
        }

        //删除
        @Override
        public void onContactDeleted(List<String> list) {
            Log.i(TAG, "-----onContactDeleted-----");
        }

        //收到好友申请
        @Override
        public void onContactInvited(String username, String reason) {
            Log.i(TAG, "-----onContactInvited-----");
//            Looper.prepare();
//            Toast.makeText(MainActivity.this,
//                    "接受到好友" + username + "的申请", Toast.LENGTH_LONG).show();
//            Looper.loop();
            HXSDKHelper.getInstance().acceptHXInvatation(username, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "------同意好友申请成功-----");
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "------同意好友申请失败------");
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }

        //同意申请
        @Override
        public void onContactAgreed(String s) {
            Log.i(TAG, "-----onContactAgreed------");
        }

        //拒绝申请
        @Override
        public void onContactRefused(String s) {

        }

    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("微信");
        setSupportActionBar(toolbar);
        //设置点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_newChat: //发起群聊
                        break;
                    case R.id.action_addFriend: //添加好友
                        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_search: //搜索
                        Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });

    }

    /**
     * 创建toolbar菜单项
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * menu 图片显示
     *
     * @param view
     * @param menu
     * @return
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            //通过反射机制
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    /**
     * 初始化TabButton
     */
    private void initTabButton() {
        icon_chat = (ImageView) findViewById(R.id.iv_chat);
        icon_contacts = (ImageView) findViewById(R.id.iv_contacts);
        icon_find = (ImageView) findViewById(R.id.iv_find);
        icon_about_me = (ImageView) findViewById(R.id.iv_about_me);
        icon_imageViews = new ImageView[]{icon_chat, icon_contacts, icon_find, icon_about_me};

        textView_chat = (TextView) findViewById(R.id.tv_chat);
        textView_contacts = (TextView) findViewById(R.id.tv_contacts);
        textView_find = (TextView) findViewById(R.id.tv_find);
        textView_about_me = (TextView) findViewById(R.id.tv_about_me);
        textViews = new TextView[]{textView_chat, textView_contacts, textView_find, textView_about_me};

        layout_chat = (LinearLayout) findViewById(R.id.layout_chat);
        layout_contacts = (LinearLayout) findViewById(R.id.layout_contacts);
        layout_find = (LinearLayout) findViewById(R.id.layout_find);
        layout_about_me = (LinearLayout) findViewById(R.id.layout_about_me);
        linearLayouts = new LinearLayout[]{layout_chat, layout_contacts, layout_find, layout_about_me};

        //首次进入初始化的TabButton的样式
        setSelectedTabButton(0);

        switchPageTabButtonOnClick();
    }

    /**
     * 设置当前TabButton的样式
     */
    private void setSelectedTabButton(int index) {
        linearLayouts[index].setSelected(true);
    }

    /**
     * 恢复TabButton默认样式
     */
    private void setResetTabButton() {
        for (LinearLayout layout : linearLayouts) {
            layout.setSelected(false);
        }
    }

    /**
     * 点击TabButton时改变ViewPage  并改变TabButton的样式
     */
    private void switchPageTabButtonOnClick() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.layout_chat:
                        setSelectedPage(0);
                        break;
                    case R.id.layout_contacts:
                        setSelectedPage(1);
                        break;
                    case R.id.layout_find:
                        setSelectedPage(2);
                        break;
                    case R.id.layout_about_me:
                        setSelectedPage(3);
                        break;
                }
            }
        };

        //为TabButton添加点击事件
        for (LinearLayout layout : linearLayouts) {
            layout.setOnClickListener(onClickListener);
        }

    }

    /**
     * TabButton选择时所对应的样式和ViewPage对应的样式
     */
    private void setSelectedPage(int index) {
        viewPager.setCurrentItem(index);
        setSelectedTabButton(index);
    }

    /**
     * 初始化ViewPage
     */
    private void initViewPage() {
        viewPager = (ViewPager) findViewById(R.id.view_page);
        fragmentList = new ArrayList<Fragment>();
        mFragmentConversation = new MainFragmentConversation();
        mFragmentContacts = new MainFragmentContacts();
        mFragmentFind = new MainFragmentFind();
        mFragmentAboutMe = new MainFragmentAboutMe();
        fragmentList.add(mFragmentConversation);
        fragmentList.add(mFragmentContacts);
        fragmentList.add(mFragmentFind);
        fragmentList.add(mFragmentAboutMe);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        //ViewPage添加适配器
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        setViewPageSelectedTabButtonLookChange();
    }

    /***
     * 切换ViewPage时 改变TabButton的样式
     *
     * viewPage三个方法执行的顺序及细节
     * 1、当ViewPage可见时，执行onPageScrolled()
     * 2、当按住页面左右拖动并且手指未松开时，先执行onPageScrollStateChanged()，然后在执行onPageScrolled(),
     * 3、当拖动后松开手指，无论是否显示新的界面，将先执行onPageScrollStateChanged(),然后不断执行onPageScrolled()
     *      如果滑动到新的界面，将执行onPageSelected()，然后不断执行onPageScrolled()
     */
    private void setViewPageSelectedTabButtonLookChange() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println(position);
//                if(position == fragmentList.indexOf(mFragmentContacts)){
//                    mCustomNavigationViewAndTextViewVisible();
//                }else {
//                    mCustomNavigationViewAndTextViewInVisible();
//                }
            }

            //ViewPage切换时改变的TabButton的样式
            @Override
            public void onPageSelected(int position) {
                setResetTabButton();
                setSelectedPage(position);
            }

            /**
             state有三个值：0（END）,1(PRESS) , 2(UP)
             当用手指滑动翻页时，手指按下去的时候会触发这个方法，state值为1，
             手指抬起时，如果发生了滑动（即使很小），这个值会变为2，然后最后变为0 。
             总共执行这个方法三次。
             一种特殊情况是手指按下去以后一点滑动也没有发生，这个时候只会调用这个方法两次，state值分别是1,0
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    /**
     * 设置MCustomNavigationView和TextView是可见的
     */
    private void mCustomNavigationViewAndTextViewVisible() {
        mCustomNavigationView = (MCustomNavigationView) mFragmentContacts.getView().findViewById(R.id.sortNavBar);
        mCustomNavigationView.setVisibility(View.VISIBLE);
        textView = (TextView) mFragmentContacts.getView().findViewById(R.id.tv_selected);
        textView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置MCustomNavigationView和TextView是隐藏的
     */
    private void mCustomNavigationViewAndTextViewInVisible() {
        mCustomNavigationView = (MCustomNavigationView) mFragmentContacts.getView().findViewById(R.id.sortNavBar);
        mCustomNavigationView.setVisibility(View.GONE);
        textView = (TextView) mFragmentContacts.getView().findViewById(R.id.tv_selected);
        textView.setVisibility(View.GONE);
    }

}
