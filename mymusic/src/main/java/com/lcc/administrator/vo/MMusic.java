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
    public String album;
    //是否正在播放
    private boolean isPlaying;
    //专辑图片
    public Bitmap albumBip;
    private int length;
    private String path;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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
