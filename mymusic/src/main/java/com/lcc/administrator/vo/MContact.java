package com.lcc.administrator.vo;

/**
 * @author lcc
 * created at 2018/12/18
 */
public class MContact {
    //
    private String username;
    //昵称
    private String nickName;
    //首字母
    private String header;

    public MContact() {
    }

    public MContact(String username) {
        this.username = username;
    }

    public MContact(String username, String nickName, String header) {
        this.username = username;
        this.nickName = nickName;
        this.header = header;
    }

    public MContact(String nickName, String header) {
        this.nickName = nickName;
        this.header = header;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
