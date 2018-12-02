package com.lcc.administrator.mymusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcc.administrator.adapter.RVAdapter;
import com.lcc.administrator.vo.MMusic;
import com.lcc.administrator.weight.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcc
 * created at 2018/11/29
 */
public class MMusicFragment extends Fragment {

    private SwipeMenuRecyclerView swipeMenuRecyclerView;
    private RVAdapter adapter;
    private List<MMusic> mMusicList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        initRecyclerView(rootView);
        return rootView;
    }

    /**
     * 初始化SwipeMenuRecyclerView
     *
     * @param rootView
     */
    private void initRecyclerView(View rootView) {
        swipeMenuRecyclerView = (SwipeMenuRecyclerView) rootView.findViewById(R.id.rv);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMusicList = new ArrayList<MMusic>();
        adapter = new RVAdapter(getActivity(), mMusicList);
        swipeMenuRecyclerView.setAdapter(adapter);
        //添加分割线 垂直方向的分割线
        swipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }

}
