package com.lcc.administrator.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.vo.MContacts;

import java.util.List;

/**
 * 通讯录适配器
 */
public class MContactsAdapter extends BaseAdapter {

    private List<MContacts> mContactsList;
    private LayoutInflater inflater;

    public MContactsAdapter(List<MContacts> mContactsList, Context context) {
        this.mContactsList = mContactsList;
        inflater = LayoutInflater.from(context);
    }

    public void setContactList(List<MContacts> contactList) {
        this.mContactsList = contactList;
    }

    @Override
    public int getCount() {
        return mContactsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHodler viewHodler;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_contacts, viewGroup, false);
            viewHodler = new ViewHodler();
            viewHodler.tv_header_contacts = (TextView) view.findViewById(R.id.tv_contacts);
            viewHodler.iv_avatar_contacts = (ImageView) view.findViewById(R.id.iv_contacts_avatar);
            viewHodler.tv_nickName_contacts = (TextView) view.findViewById(R.id.tv_contacts_nickName);
            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) view.getTag();
        }
        //获取该行的数据
        MContacts mContacts = (MContacts) getItem(position);
        //设置首字母
        viewHodler.tv_header_contacts.setText(mContacts.getHeard());
        //设置昵称
        viewHodler.tv_nickName_contacts.setText(mContacts.getNickName());

        //判断Contacts集合是否是第一个具有该首字母的Item
        //该组的首字母
        String targetHeaderLetter = mContacts.getHeard();

        int firstPosition = getHeaderLatterFirstPisition(targetHeaderLetter);

        //判断该集合中第一个是该首字母的position 是否和当前item的position相等
        if (firstPosition == position) {
            //显示该textview
            viewHodler.tv_header_contacts.setVisibility(View.VISIBLE);
        } else {
            //隐藏该textview
            viewHodler.tv_header_contacts.setVisibility(View.GONE);
        }

        return view;
    }


    /**
     * 返回首字母headerlatter的第一个数据的position
     *
     * @param headerlatter
     * @return
     */
    public int getHeaderLatterFirstPisition(String headerlatter) {

        for (int i = 0; i < mContactsList.size(); i++) {
            MContacts mContacts = mContactsList.get(i);

            if (headerlatter.equals(mContacts.getHeard())) {
                return i;
            }
        }
        return -1;
    }


    /***
     * 内部类 ViewHodler
     */
    class ViewHodler {
        ImageView iv_avatar_contacts;
        TextView tv_nickName_contacts;
        TextView tv_header_contacts;
    }

}
