package com.lcc.administrator.wechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.vo.ThreadPool;

public class RegisterActivity extends BaseActivity {

    private EditText et_username;
    private EditText et_pwd;
    private EditText et_pwd_again;
    private Button btn_register;
    private Button btn_cancel;

    private MyChatApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        application = (MyChatApplication) getApplication();
        initView();
    }

    @Override
    public void setUpTitle() {
        setToolbarTitle("注册");
    }

    @Override
    public void setUpHomeEnable() {
        setHomeUpEnable(true);
    }

    @Override
    public void setUpNavigationIcon() {

    }

    /**
     * 初始化registerView
     */
    private void initView() {
        et_username = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);

        btn_register = (Button) findViewById(R.id.bt_register);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_register.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);
    }

    /**
     * 匿名内部类  按钮添加点击事件
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                String str_username = et_username.getText().toString().trim();
                String str_pwd = et_pwd.getText().toString().trim();
                String str_pwd_again = et_pwd_again.getText().toString().trim();
                doHuanXinRegister(str_username, str_pwd);
                break;
            case R.id.btn_cancel:
                RegisterActivity.this.finish();
                break;
        }
        }
    };

    /**
     * 完成HuanXin注册
     */
    private void doHuanXinRegister(final String username, final String pwd) {

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            ThreadPool.LIMIT_TASK_EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(username, pwd);
                    Intent data = new Intent();
                    data.putExtra(application.KEY_USERNAME, username);
                    data.putExtra(application.KEY_PASSWORD,pwd);
                    RegisterActivity.this.setResult(RESULT_OK,data);
                    RegisterActivity.this.finish();
                } catch (final EaseMobException e) {
                    Looper.prepare();
                    //注册失败
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NONETWORK_ERROR) {
                        Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                        Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                }

                }
            });
        }
    }


}
