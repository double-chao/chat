package com.lcc.administrator.wechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.chat.LoginActivity;

public class MainFragmentAboutMe extends Fragment {

    private Button btn_logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        initAboutMe(rootView);
        return rootView;
    }

    /**
     * 初始化aboutMe
     */
    private void initAboutMe(View rootView) {
        btn_logout = (Button) rootView.findViewById(R.id.btn_logout);
        //获取当前用户的名字
        btn_logout.setText("注销"+EMChatManager.getInstance().getCurrentUser());
        btn_logout.setOnClickListener(onClickListener);
    }

    /**
     * 添加点击事件
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout: //注销
                EMChatManager.getInstance().logout(new EMCallBack() {
                    @Override
                    public void onSuccess() {  //注销成功
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
        }

        }
    };

}
