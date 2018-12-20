package com.lcc.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.administrator.mymusic.R;
import com.lcc.administrator.vo.MContact;

import java.util.List;

/**
 * @author lcc
 * created at 2018/12/18
 * 通讯录适配器
 */
public class MContactAdapter extends BaseAdapter {

    private List<MContact> mContactList;
    private LayoutInflater layoutInflater;

    public MContactAdapter(List<MContact> mContactList, Context context) {
        this.mContactList = mContactList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            //从XML文件中加载布局
            view = layoutInflater.inflate(R.layout.layout_contacts, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_header_contacts = (TextView) view.findViewById(R.id.tv_contacts);
            viewHolder.iv_avatar_contacts = (ImageView) view.findViewById(R.id.iv_contacts_avatar);
            viewHolder.tv_nickName_contacts = (TextView) view.findViewById(R.id.tv_contacts_nickName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //获取该行的联系人数据
        MContact mContact = (MContact) getItem(position);
        //设置昵称
        viewHolder.tv_nickName_contacts.setText(mContact.getNickName());
        //设置首字母
        viewHolder.tv_header_contacts.setText(mContact.getHeader());

        //判断Contacts集合是否是第一个具有该首字母的Item
        //该组的首字母
        String targetHeaderLetter = mContact.getHeader();

        int firstPosition = getHeaderLatterFirstPosition(targetHeaderLetter);

        //判断该集合中第一个是该首字母的position 是否和当前item的position相等
        if (firstPosition == position) {
            //显示该textView
            viewHolder.tv_header_contacts.setVisibility(View.VISIBLE);
        } else {
            //隐藏该textView
            viewHolder.tv_header_contacts.setVisibility(View.GONE);
        }

        return view;
    }

    /**
     * 返回首字母headerLatter的第一个数据的position
     *
     * @param headerlatter
     * @return
     */
    public int getHeaderLatterFirstPosition(String headerlatter) {

        for (int i = 0; i < mContactList.size(); i++) {
            MContact mContacts = mContactList.get(i);

            if (headerlatter.equals(mContacts.getHeader())) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     */
    class ViewHolder {
        ImageView iv_avatar_contacts;
        TextView tv_nickName_contacts;
        TextView tv_header_contacts;
    }
}
