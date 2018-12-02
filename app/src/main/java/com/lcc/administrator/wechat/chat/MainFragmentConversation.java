package com.lcc.administrator.wechat.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.adapter.MConversationAdapter;
import com.lcc.administrator.wechat.vo.Conversation;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentConversation extends Fragment {

    private List<Conversation> mConversation;
    private ListView listView;

    private int item_position;
    private MConversationAdapter conversationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        initListView(rootView);
        return rootView;
    }

    /***
     *  初始化ListView
     * @param rootView
     */

    private void initListView(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.lv_chat);
        conversationAdapter = new MConversationAdapter(getConversationDatas(),getActivity());
        listView.setAdapter(conversationAdapter);
        //添加点击事件
        listView.setOnItemClickListener(onItemClickListener);
        //为listView注册上下文菜单
        registerForContextMenu(listView);
        contextMenu();
    }

    /***
     *   ListView的点击事件
     */
    private AdapterView.OnItemClickListener onItemClickListener =
        new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击了第" + position + "个Item", Toast.LENGTH_LONG).show();
            }
        };

    /**
     *   长按listView 显示上下文菜单
     */
    private void contextMenu(){
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                            ContextMenu.ContextMenuInfo contextMenuInfo) {
                //获取对应item的id
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                item_position = info.position;
                //加载menu布局
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.context_menu,contextMenu);
            }
        });
    }

    /**
     * 监听上下文菜单项点击事件的回调用函数
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_top:

                break;
            case R.id.id_unRead:

                break;
            case R.id.id_delete:
                mConversation.remove(item_position);
                conversationAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    /**
     * 模拟会话数据
     *
     * @return
     */
    private List<Conversation> getConversationDatas() {
        mConversation = new ArrayList<Conversation>();
        for (int i = 0; i < 21; i++) {
            mConversation.add(new Conversation("pikaqiu" + i,
                    "要打个什么信息才会长，然后才能让消息超过边界，后面下面是省略号", "9月18号"));
        }
        return mConversation;
    }
}
