package com.lcc.administrator.wechat.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.adapter.MContactsAdapter;
import com.lcc.administrator.wechat.util.MPinYinUtil;
import com.lcc.administrator.wechat.vo.MContacts;
import com.lcc.administrator.wechat.vo.MContactsComparator;
import com.lcc.administrator.wechat.weiget.MCustomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFragmentContacts extends Fragment {

    private List<MContacts> mContactsList;
    private ListView listView;
    private MContactsAdapter mContactsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        initListView(rootView);
        initMCustomNavigationView(rootView);
        return rootView;
    }
    /**
     * 初始化MCustomNavigationView
     */
    private void initMCustomNavigationView(View rootView) {
        MCustomNavigationView mNavBar = (MCustomNavigationView) rootView.findViewById(R.id.sortNavBar);
        TextView tv_selected = (TextView) rootView.findViewById(R.id.tv_selected);
        mNavBar.setShowCurrentTextView(tv_selected);

        //自定义接口
        mNavBar.setSortNavBarLetterChangeListener(new MCustomNavigationView.SortNavBarLetterChangeListener() {
            @Override
            public void OnLetterChange(String letter) {
            //ListView快速滑动 第一个字母是letter的MContacts的数据
            if (mContactsAdapter != null) {
                int position = mContactsAdapter.getHeaderLatterFirstPisition(letter);

                if (position != -1) {
                    listView.setSelection(position);
                }
            }
            }
        });

    }

    /**
     * 初始化ListView
     */
    private void initListView(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.lv_contacts);
        //加载HX好友体系的数据
        mContactsList = MyChatApplication.getInstance().getMContactsDataList();
        //字母排序   a-Z
        Collections.sort(mContactsList, new MContactsComparator());
        //加载适配器
        mContactsAdapter = new MContactsAdapter(mContactsList, getActivity());
        listView.setAdapter(mContactsAdapter);
    }

    /**
     *  通讯录页面的刷新
     */
    public void reFreshContactUI(){
        //获取好友数据
        mContactsList = MyChatApplication.getInstance().getMContactsDataList();
        //好友排序
        Collections.sort(mContactsList,new MContactsComparator());
        //判断适配器是否初始化
        if(mContactsAdapter==null){
            mContactsAdapter = new MContactsAdapter(mContactsList,getActivity());
            listView.setAdapter(mContactsAdapter);
        }else {
            //适配器添加数据
            mContactsAdapter.setContactList(mContactsList);
            //刷新界面
            mContactsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 模拟通讯录数据
     */
    private void getMContactsListDatas() {
        mContactsList = new ArrayList<MContacts>();
        //从xml文件中加载昵称数据
        String[] nickNameArray = getResources().getStringArray(R.array.nickNameArray);

        for (String nickName : nickNameArray) {
            MContacts mContacts = new MContacts();
            mContacts.setNickName(nickName);

            //排除拼音字符串首字母不是字符串的情况  用“#” 代替  or 用大写字母
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

            mContactsList.add(mContacts);
        }
    }

}
