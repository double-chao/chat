package com.lcc.administrator.mymusic;

/**
*  @author lcc
*  created at 2018/12/3
*/
public interface OnItemActionListener {
    void OnItemClick(int position);
    void OnItemTop(int position);
    void OnItemDelete(int position);
    void OnItemShare(int position);
}
