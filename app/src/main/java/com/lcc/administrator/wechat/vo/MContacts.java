package com.lcc.administrator.wechat.vo;

import android.text.TextUtils;

import com.easemob.chat.EMContact;
import com.lcc.administrator.wechat.util.MPinYinUtil;

/**
 *   继承HX的EMContact类 加载好友的用户名
 */
public class MContacts extends EMContact{
    //昵称
    private String nickName;
    //首字母
    private String heard;

    public MContacts() {
    }

    public MContacts(String username){
        super(username);
    }

    public MContacts(String username,String nickName,String heard){
        super(username);
        this.nickName=nickName;
        this.heard=heard;
    }

    public MContacts(String nickName, String heard) {
        this.nickName = nickName;
        this.heard = heard;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeard() {
        return heard;
    }

    public void setHeard(String heard) {
        this.heard = heard;
    }

    /**
     *   重组联系人  把用户名设置为昵称，并获取首字母
     * @param username
     * @return
     */
    public static MContacts loadContact(String username){
        MContacts mContacts = new MContacts(username);
        // 暂时把HX中的用户名设置为昵称
        String nickName = username;
        mContacts.setNickName(nickName);
        String pinYin = MPinYinUtil.getPinYin(nickName);
        //为空时  用“#”表示
        if (TextUtils.isEmpty(pinYin)) {
            mContacts.setHeard("#");
        }
        //获取昵称的首字母的拼音
        else if (Character.toString(pinYin.charAt(0)).matches("[a-zA-Z]")) {
            //在TextView上显示首字母 设置为大写（显示大写）
            mContacts.setHeard(Character.toString(pinYin.charAt(0)).toUpperCase());
        } else {//不是字母
            mContacts.setHeard("#");
        }
        return mContacts;
    }

}
