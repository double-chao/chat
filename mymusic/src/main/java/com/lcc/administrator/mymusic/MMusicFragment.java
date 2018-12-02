package com.lcc.administrator.mymusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        initDatas();
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
        setRecyclerViewOnItemClick();
        //添加分割线 垂直方向的分割线
        swipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }

    /**
     *  为recyclerView添加点击事件
     */
    private void setRecyclerViewOnItemClick(){
        swipeMenuRecyclerView.setOnItemActionClickListener(new OnItemActionListener() {
            @Override
            public void OnItemClick(int position) {
                Toast.makeText(getActivity(),"点击了",Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnItemTop(int position) {
                Toast.makeText(getActivity(),"点击了顶置",Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnItemDelete(int position) {
                Toast.makeText(getActivity(),"点击了删除",Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnItemShare(int position) {
                Toast.makeText(getActivity(),"点击了分享",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 初始化音乐数据
     */
    private void initDatas() {
        mMusicList.clear();
        //获取ContentResolver的对象，并进行实例化
        ContentResolver resolver = getActivity().getContentResolver();
        //获取游标
        //创建游标MediaStore.Audio.Media.EXTERNAL_CONTENT_URI获取音频的文件，
        //                      后面的是关于select筛选条件，这里填土null就可以了
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        //游标归零
        cursor.moveToFirst();
        do {
            //获取歌名
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            //获取歌唱者
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            //获取专辑名
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            //获取专辑图片id
            int albumID = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            //歌曲的总播放时长
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            //音频文件路径
            String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //歌曲文件大小
            Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

            //创建Music对象，并赋值
            MMusic music = new MMusic();
            music.setTitle(title);
            music.setArtist(artist);
            music.album = album;
//            music.albumBip = getAlbumArt(albumID);
            music.setLength(duration);
            music.setPath(url);
            //将music放入musicList集合中
            mMusicList.add(music);
        }
        //当游标不能搜索到底时，cursor.moveToNext()==false
        while (cursor.moveToNext());
        //关闭游标
        cursor.close();
    }

}
