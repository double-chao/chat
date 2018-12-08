package com.lcc.administrator.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lcc.administrator.vo.Common;

import java.io.IOException;

/**
*  @author lcc
*  created at 2018/12/3
*/
public class MMusicService extends Service {

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private int position;

//    private final IBinder iBinder = new MusicBinder();

    public MMusicService (){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    class MusicBinder extends Binder{
//
//        public MMusicService getService(){
//            return MMusicService.this;
//        }
//    }
//


//    public void playMusic(String path){
//        if(mediaPlayer.isPlaying()){
//            mediaPlayer.stop();
//        }
//        mediaPlayer.reset();
//        try {
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
            if(mediaPlayer.isPlaying()){
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.pause();
            }else {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
