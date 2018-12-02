package com.lcc.administrator.wechat.vo;

public class Conversation {

    private String nickName;
    private String lastMsg;
    private String lastTime;

    public Conversation() {
    }

    public Conversation(String nickName, String lastMsg, String lastTime) {
        this.nickName = nickName;
        this.lastMsg = lastMsg;
        this.lastTime = lastTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

}
