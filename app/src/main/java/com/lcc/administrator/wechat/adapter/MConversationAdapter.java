package com.lcc.administrator.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.vo.Conversation;

import java.util.List;

/**
 * 会话数据adpater
 */
public class MConversationAdapter extends BaseAdapter {

    private List<Conversation> mConversationList;
    private LayoutInflater inflater;

    public MConversationAdapter() {
    }

    public MConversationAdapter(List<Conversation> mConversationList, Context mContext) {
        this.mConversationList = mConversationList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mConversationList.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            //从XML文件中加载布局
            view = inflater.inflate(R.layout.layout_chat, viewGroup, false);
        }

        ImageView iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        TextView tv_nickName = (TextView) view.findViewById(R.id.tv_nickName);
        TextView tv_lastMsg = (TextView) view.findViewById(R.id.tv_last_msg);
        TextView tv_laseTime = (TextView) view.findViewById(R.id.tv_time);

        //获取该行的数据
        Conversation conversation = (Conversation) getItem(position);

        tv_nickName.setText(conversation.getNickName());
        tv_lastMsg.setText(conversation.getLastMsg());
        tv_laseTime.setText(conversation.getLastTime());

        return view;
    }

}
