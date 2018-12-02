package com.lcc.administrator.wechat.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.util.MPinYinUtil;
import com.lcc.administrator.wechat.vo.MContacts;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private Button btn_register;
    private Button btn_login;
    private EditText et_username;
    private EditText et_pwd;
    private ImageView imageView;

    private ProgressDialog progressDialog; //进度条

    private final int REQUEST_CODE = 100; //请求码

    private MyChatApplication application;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        application = (MyChatApplication) getApplication();
        initLoginView();
    }

    @Override
    public void setUpTitle() {
        setToolbarTitle("登录");
    }

    @Override
    public void setUpNavigationIcon() {

    }

    @Override
    public void setUpHomeEnable() {

    }

    /**
     * 初始化loginView  以及添加点击事件
     */
    private void initLoginView() {
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_username = (EditText) findViewById(R.id.et_name1);
        et_pwd = (EditText) findViewById(R.id.et_pwd1);
        imageView = (ImageView) findViewById(R.id.iv_eye);

        btn_register.setOnClickListener(clickListener);
        btn_login.setOnClickListener(clickListener);
        imageView.setOnClickListener(clickListener);
    }

    /**
     * 添加点击事件
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_register: //注册
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    //执行改方法  回调onActivityResult()
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.btn_login:  //登录
                    String str_username = et_username.getText().toString().trim();
                    String str_pwd = et_pwd.getText().toString().trim();
                    doHuanXinLogin(str_username, str_pwd);
                    break;
                case R.id.iv_eye: //查看密码
                    if (et_pwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    } else {
                        et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    break;
            }
        }
    };


    /**
     * 使用 startActivityForResult()  回调的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE:  //注册页面返回
                if (resultCode == RESULT_OK) {
                    //注册成功，取数据，放到 et_username 上
                    String str_username = data.getStringExtra(application.KEY_USERNAME);
                    String str_password = data.getStringExtra(application.KEY_PASSWORD);
                    et_username.setText(str_username);
                    et_pwd.setText(str_password);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 环信 用户登录
     */
    private void doHuanXinLogin(String username, String pwd) {
        //非空验证
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录...");
        progressDialog.show();

        EMChatManager.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() { //登录成功

                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("正在加载会话群组信息...");
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //初始化好友列表
                initContacts();
                //第一次登录加载本地群组，会话到内存中
                EMGroupManager.getInstance().loadAllGroups();
                EMChatManager.getInstance().loadAllConversations();

                //Dialog正在显示并且LoginActivity没有被关闭掉，进度框隐藏
                if (progressDialog.isShowing() && !LoginActivity.this.isFinishing()) {
                    progressDialog.dismiss();
                }
                //跳转Activity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {
                Log.i("main", "登录聊天服务器失败！");
            }

        });
    }

    /**
     * 初始化好友列表
     */
    private void initContacts() {
        List<MContacts> mContactsList = new ArrayList<MContacts>();
        try {
            List<String> userNames = EMContactManager.getInstance().getContactUserNames();
            for (String s : userNames) {
                Log.i(TAG, "好友---" + s);
                //调用MContacts中的静态方法 把用户名设置成和昵称相同，在进行排序
                MContacts mContacts = MContacts.loadContact(s);
                mContactsList.add(mContacts);
            }
            //保存到内存中
            MyChatApplication.getInstance().setMContactsDataList(mContactsList);

        } catch (EaseMobException e) {
            e.printStackTrace();
            Log.i(TAG, "用户获取环信好友列表失败", e);
        }
    }

}
