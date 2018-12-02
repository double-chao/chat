package com.lcc.administrator.wechat.chat;

import android.app.Application;

import com.lcc.administrator.wechat.vo.MContacts;

import java.util.List;

public class MyChatApplication extends Application {

    public final String KEY_USERNAME = "key_username";
    public final String KEY_PASSWORD = "key_password";
    //HX通讯录数据
    public List<MContacts> mContactsDataList;

    public static MyChatApplication myChatApplication;

    public static MyChatApplication getInstance() {
        return myChatApplication;
    }

    private HXSDKHelper hxsdkHelper = HXSDKHelper.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        myChatApplication = this;
        //初始化HX SDK
        hxsdkHelper.initHXSDK(this);
    }

    public void setMContactsDataList(List<MContacts> mContactsDataList) {
        this.mContactsDataList = mContactsDataList;
    }

    /**
     *   得到HX好友通讯录上的数据
     * @return
     */
    public List<MContacts> getMContactsDataList() {
        if (mContactsDataList != null) {
            return mContactsDataList;
        }
        return null;
    }
    /**
     *  增加好友联系人
     * @param contacts
     */
    public void addContact(MContacts contacts){
        mContactsDataList.add(contacts);
    }

}
