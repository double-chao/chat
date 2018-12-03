package com.lcc.administrator.mymusic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lcc.administrator.adapter.RVAdapter;
import com.lcc.administrator.vo.Common;
import com.lcc.administrator.vo.MMusic;
import com.lcc.administrator.weight.SwipeMenuRecyclerView;

/**
 * @author lcc
 * created at 2018/11/29
 * <p>
 * 音乐列表界面
 */
public class MMusicFragment extends Fragment {

    //通知管理的id
    private int nId = 1;
    //android 8.0中通知栏必须设置channel  这是其Id
    private String channelId = "123";
    //这是其name  最好是汉字
    private String channelName = "超超";

    private SwipeMenuRecyclerView swipeMenuRecyclerView;
    private RVAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        initRecyclerView(rootView);
        initMusicData();
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
        adapter = new RVAdapter(getActivity(), Common.mMusicList);
        swipeMenuRecyclerView.setAdapter(adapter);
        //为Menu添加点击事件
        setRecyclerViewOnItemClick();
        //添加分割线 垂直方向的分割线
        swipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }

    /**
     * 为recyclerView添加点击事件
     */
    private void setRecyclerViewOnItemClick() {
        swipeMenuRecyclerView.setOnItemActionClickListener(new OnItemActionListener() {
            @Override
            public void OnItemClick(int position) {

                for (MMusic mMusic : Common.mMusicList) {
                    mMusic.isPlaying = false;
                }
                Common.mMusicList.get(position).isPlaying = true;
                //刷新界面
                adapter.notifyDataSetChanged();
                //跳转activity
                Intent intent = new Intent(getActivity(),MMusicActivity.class);
                //
                intent.putExtra("position",position);

                //创建延时意图  传入intent对象（要跳转到的activity）
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //远程View Object的子类  主要应用于通知栏 和 桌面小控件
                RemoteViews remoteViews = new RemoteViews(getActivity().getPackageName(),
                        R.layout.notification_layout);
                //设置 通知的题目 和 歌手名字
                remoteViews.setTextViewText(R.id.nf_title_tv, Common.mMusicList.get(position).getTitle());
                remoteViews.setTextViewText(R.id.nf_artist_tv, Common.mMusicList.get(position).getArtist());

                //为延时意图中的按钮设置点击事件
                remoteViews.setOnClickPendingIntent(R.id.nf_pause_imgv, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.nf_next_imgv, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.nf_last_iv, pendingIntent);

                if (Common.mMusicList.get(position).albumBip != null) {
                    remoteViews.setImageViewBitmap(R.id.nf_album_imgv,
                            Common.mMusicList.get(position).albumBip);
                }

                //创建notificationManager对象
                NotificationManager notificationManager = (NotificationManager)
                        getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                //匹配安卓8.0以上的通知栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, channelName,
                            NotificationManager.IMPORTANCE_DEFAULT);
                    //是否在桌面icon右上角展示小红点
                    channel.enableLights(false);
                    //是否在久按桌面图标时显示此渠道的通知
                    channel.setShowBadge(false);
                    channel.enableVibration(false);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                }

                //创建通知对象
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), channelId);
                //设置在状态栏的小图标
                builder.setSmallIcon(R.drawable.qq_music);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                //通知时间
                builder.setWhen(System.currentTimeMillis());
                //消息标题
                builder.setContentTitle("我的通知");
                //消息来临时显示的提示信息
                builder.setContentText("正在播放音乐");
                //设置默认的提示音 震动 提示灯
                builder.setDefaults(Notification.DEFAULT_SOUND);
                //设置是否可以手动取消
                builder.setAutoCancel(true);
                //添加 remoteViews
                builder.setContent(remoteViews);
                builder.setContentIntent(pendingIntent);
                //这里的id最好保证每次都是不一样的,否则第二次发送无效发通知(就还是第一次点击的通知)
                notificationManager.notify(nId, builder.build());
                //开始MusicActivity
                startActivity(intent);
                //变换nId数字
                nId++;
            }

            @Override
            public void OnItemTop(int position) {
                Toast.makeText(getActivity(), "点击了顶置", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemDelete(int position) {
                Toast.makeText(getActivity(), "点击了删除", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemShare(int position) {
                Toast.makeText(getActivity(), "点击了分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化音乐数据 ，
     * 暂时没有配置安卓6.0以上的要获取存取权限的代码，只在清单文件中写了权限
     */
    private void initMusicData() {
        Common.mMusicList.clear();
        //获取ContentResolver的对象，并进行实例化
        ContentResolver resolver = getActivity().getContentResolver();
        //获取游标
        //创建游标MediaStore.Audio.Media.EXTERNAL_CONTENT_URI获取音频的文件，
        //                      后面的是关于select筛选条件，这里填上null就可以了
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
            music.albumBip = getAlbumArt(albumID);
            music.setLength(duration);
            music.setPath(url);
            //将music放入musicList集合中
            Common.mMusicList.add(music);
        }
        //当游标不能搜索到底时，cursor.moveToNext()==false
        while (cursor.moveToNext());
        //关闭游标
        cursor.close();
    }

    /**
     * 获取专辑图片
     *
     * @param album_id
     * @return
     */
    private Bitmap getAlbumArt(int album_id) {  //前面我们只是获取了专辑图片id，在这里实现通过id获取掉专辑图片
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = getActivity().getContentResolver().query(Uri.parse(mUriAlbums + "/"
                + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.avatar);
        }
        return bm;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
