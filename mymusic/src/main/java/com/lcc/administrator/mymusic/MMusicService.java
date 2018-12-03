package com.lcc.administrator.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lcc.administrator.vo.Common;

import java.io.IOException;

/**
*  @author lcc
*  created at 2018/12/3
*/
public class MMusicService extends Service {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int position;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent();
        position= intent.getIntExtra("position",0);
        String path = Common.mMusicList.get(position).getPath();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }

}
