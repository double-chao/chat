package com.lcc.administrator.vo;

import android.graphics.Bitmap;

/**
*  @author lcc
*  created at 2018/12/2
 *
 *  歌曲实体类
*/
public class MMusic {
    //歌名
    private String title;
    //歌手
    private String artist;
    //专辑名
    private String album;
    //是否正在播放
    private boolean isPlaying;
    //专辑图片
    private Bitmap albumBip;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Bitmap getAlbumBip() {
        return albumBip;
    }

    public void setAlbumBip(Bitmap albumBip) {
        this.albumBip = albumBip;
    }
}
