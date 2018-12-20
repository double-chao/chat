package com.lcc.administrator.mymusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lcc.administrator.adapter.MContactAdapter;
import com.lcc.administrator.utils.MPinYinUtil;
import com.lcc.administrator.vo.MContact;
import com.lcc.administrator.vo.MContactsComparator;
import com.lcc.administrator.weight.SortNavBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lcc
 * created at 2018/11/29
 */
public class MContactsFragment extends Fragment {

    private ListView listView;
    //右侧导航栏
    private SortNavBar sortNavBar;
    //通讯录适配器
    private MContactAdapter contactAdapter;
    //通讯录数据
    private List<MContact> contactList;
    //中间显示的TextView
    private TextView tv_selected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        initSortNavBar(rootView);
        initListView(rootView);
        return rootView;
    }

    /**
     * 初始化导航栏
     *
     * @param rootView
     */
    private void initSortNavBar(View rootView) {
        sortNavBar = (SortNavBar) rootView.findViewById(R.id.sortNav_bar);
        tv_selected = (TextView) rootView.findViewById(R.id.tv_selected);
        //
        sortNavBar.setCurrentTextView(tv_selected);
        sortNavBar.setSortNavBarLetterChangeListener(new SortNavBarLetterChangeListener() {
            @Override
            public void OnLetterChange(String letter) {
                if (contactAdapter != null) {
                    int position = contactAdapter.getHeaderLatterFirstPosition(letter);
                    if (position != -1){
                        listView.setSelection(position);
                    }
                }
            }
        });

    }

    /**
     * 初始化listView
     * @param rootView
     */
    private void initListView(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.lv_contacts);
        contactAdapter = new MContactAdapter(getMContactsData(), getActivity());
        //字母排序   a-Z
        Collections.sort(contactList,new MContactsComparator());
        listView.setAdapter(contactAdapter);
    }

    /**
     * 模拟通讯录数据
     * @return
     */
    private List<MContact> getMContactsData() {
        contactList = new ArrayList<MContact>();
        String[] nickNameArray = getResources().getStringArray(R.array.nickNameArray);
        for (String nickName:nickNameArray) {
            MContact mContact =new MContact();
            mContact.setNickName(nickName);
            //排除拼音字符串首字母不是字符串的情况  用“#” 代替  or 用大写字母
            String pinYin = MPinYinUtil.getPinYin(nickName);

            //为空时  用“#”表示
            if (TextUtils.isEmpty(pinYin)) {
                mContact.setHeader("#");
            }
            //获取昵称的首字母的拼音
            else if (Character.toString(pinYin.charAt(0)).matches("[a-zA-Z]")) {

                //在TextView上显示首字母 设置为大写（显示大写）
                mContact.setHeader(Character.toString(pinYin.charAt(0)).toUpperCase());

            } else {//不是字母
                mContact.setHeader("#");
            }
            contactList.add(mContact);
        }
        return contactList;
    }

}
