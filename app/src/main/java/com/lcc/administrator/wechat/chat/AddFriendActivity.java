package com.lcc.administrator.wechat.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.lcc.administrator.wechat.R;

public class AddFriendActivity extends BaseActivity {

    private EditText et_addUserName;
    private EditText et_reason;
    //当前用户名
    private TextView tv_currentUserName;
    //添加好友按钮
    private Button btn_addContact;
    //进度框
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        findViewById();
        initView();
    }

    private void findViewById() {
        et_addUserName = (EditText) findViewById(R.id.et_addFriendUserName);
        et_reason = (EditText) findViewById(R.id.et_addFriendReason);
        tv_currentUserName = (TextView) findViewById(R.id.tv_current_userName);
        btn_addContact = (Button) findViewById(R.id.btn_addContact);
    }

    private void initView() {
        // 添加好友
        btn_addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(AddFriendActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.addContact_is_sending_request));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                //申请添加好友
                String user_name = et_addUserName.getText().toString().trim();
                String reason = et_reason.getText().toString().trim();
                HXSDKHelper.getInstance().addHXContact(user_name, reason, new EMCallBack() {
                    @Override
                    public void onSuccess() { //WorkThread工作线程  一个异步方法
                        //对UI进行操作 需要在UI线程中进行  还可以用Handle的Post机制来进行操作UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //隐藏progressDialog
                                progressDialog.dismiss();
                                showToast(getResources().getString(R.string.addContact_wait), AddFriendActivity.this);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {  //也是异步方法
                        progressDialog.dismiss();
                        showToast(s, AddFriendActivity.this);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }

        });
        tv_currentUserName.setText("当前用户："+ EMChatManager.getInstance().getCurrentUser());
    }

    @Override
    public void setUpTitle() {
        setToolbarTitle("添加好友");
    }

    @Override
    public void setUpHomeEnable() {
        setHomeUpEnable(true);
    }

    @Override
    public void setUpNavigationIcon() {

    }
}
