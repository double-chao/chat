package com.lcc.administrator.mymusic;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lcc.administrator.utils.BlurUtil;
import com.lcc.administrator.utils.MergeImage;
import com.lcc.administrator.vo.Common;
import com.lcc.administrator.vo.MMusic;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lcc
 * created at 2018/12/3
 */
public class MMusicActivity extends AppCompatActivity implements View.OnClickListener {


    //设置音乐播放模式
    private int i = 0;
    private int playMode = 0;
    private int buttonWitch = 0;
    //背景图片
    private ImageView bgImgV;
    //上一曲
    private ImageView prevImgV;
    //下一曲
    private ImageView nextImgV;

    private TextView titleTv;
    private TextView artistTv;
    private TextView currentTv;
    private TextView totalTv;

    private int position;
    private ImageView discImgV;
    private ImageView needleImgV;
    private MediaPlayer mediaPlayer;
    //暂停
    private ImageView pauseImgV;
    private ImageView downImg;
    private ImageView styleImg;
    //进度条
    private SeekBar seekBar;
    private boolean isStop;
    private ObjectAnimator objectAnimator = null;
    private RotateAnimation rotateAnimation = null;
    private RotateAnimation rotateAnimation2 = null;
    private String TAG = "MusicActivity";


    //Handler实现向主线程进行传值
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            seekBar.setProgress((int) (msg.what));
            currentTv.setText(formatTime(msg.what));
        }
    };

    /**
     * 创建一个类MusicThread实现Runnable接口，实现多线程
     */
    class MusicThread implements Runnable {
        @Override
        public void run() {
            while (!isStop && Common.mMusicList.get(position) != null) {
                try {
                    //让线程睡眠1000毫秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //放送给Handler现在的运行到的时间，进行ui更新
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        findViewByIdAndSetListener();
        //通过getIntent()方法实现intent信息的获取  从LogicFragment跳转过来
        Intent intent = getIntent();
        //获取position 从LogicFragment获取值
        position = intent.getIntExtra("position", 0);

        mediaPlayer = new MediaPlayer();
        prevAndNextPlaying(Common.mMusicList.get(position).getPath());

        //seekBar设置监听 (进度条的拖动)
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//        bindServiceConnection();
    }

    //  在Activity中调用 bindService 保持与 Service 的通信
//    private void bindServiceConnection() {
//        Intent intent = new Intent(MusicActivity.this, MusicService.class);
//        startService(intent);
//        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
//    }

    //  回调onServiceConnected 函数，通过IBinder 获取 Service对象，实现Activity与 Service的绑定
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            musicService = ((MusicService.MyBinder) (service)).getService();
//            Log.i("musicService", musicService + "");
////            musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            musicService = null;
//        }
//    };

    /**
     * 上一曲  下一曲  实现页面展示
     *
     * @param path
     */
    private void prevAndNextPlaying(String path) {
        isStop = false;
        mediaPlayer.reset();
        titleTv.setText(Common.mMusicList.get(position).getTitle());
        artistTv.setText(Common.mMusicList.get(position).getArtist() + "--" +
                Common.mMusicList.get(position).album);
        pauseImgV.setImageResource(R.drawable.ic_play_btn_pause);
        setAlbumBip();
        try {
            //歌曲地址
            mediaPlayer.setDataSource(path);
            // 准备
            mediaPlayer.prepare();
            // 启动
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mediaPlayer.isPlaying()) {
                        setPlayMode();
                    }

                }
            });
        } catch (IllegalArgumentException | SecurityException | IllegalStateException
                | IOException e) {
            e.printStackTrace();
        }

        //设置歌曲时长
        totalTv.setText(formatTime(Common.mMusicList.get(position).getLength()));
        //设置拖动条的最大长度和歌曲时间长度一样
        seekBar.setMax(Common.mMusicList.get(position).getLength());
        //启动线程
        MusicThread musicThread = new MusicThread();
        new Thread(musicThread).start();

        //实例化，设置旋转对象
        objectAnimator = ObjectAnimator.ofFloat(discImgV, "rotation", 0f, 360f);
        //设置转一圈要多长时间
        objectAnimator.setDuration(10000);
        //设置旋转速率
        objectAnimator.setInterpolator(new LinearInterpolator());
        //设置循环次数 -1为一直循环
        objectAnimator.setRepeatCount(-1);
        //设置转一圈后怎么转
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();

        musicIsPalyingPointOnUp();
        musicIsPausePointOut();
    }

    /**
     * 设置背景专辑图片
     */
    private void setAlbumBip() {
        if (Common.mMusicList.get(position).albumBip != null) { //获取到了专辑图片
            //将专辑虚化
            Bitmap bgbm = BlurUtil.doBlur(Common.mMusicList.get(position).albumBip, 10, 5);
            //设置虚化后的专辑图片为背景
            bgImgV.setImageBitmap(bgbm);
            //BitmapFactory.decodeResource用于根据给定的资源ID从指定的资源文件中解析、创建Bitmap对象。
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_disc);
            //将专辑图片放到圆盘中
            Bitmap bm = MergeImage.mergeThumbnailBitmap(bitmap1, Common.mMusicList.get(position).albumBip);
            discImgV.setImageBitmap(bm);
        } else { //歌曲没有专辑图片 自定义放一张图片作为专辑图片背景虚化
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
            bgImgV.setImageBitmap(bitmap);
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_disc);
            Bitmap bm = MergeImage.mergeThumbnailBitmap(bitmap1, bitmap);
            discImgV.setImageBitmap(bm);
        }
    }

    /**
     * //播放音乐时，”留声机”指针对应的样式--->搭在音乐转盘上
     */
    private void musicIsPalyingPointOnUp() {
        rotateAnimation = new RotateAnimation(-25f, 0f,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        //时间长
        rotateAnimation.setDuration(300);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        //偏移量
        rotateAnimation.setStartOffset(500);
        needleImgV.setAnimation(rotateAnimation);
        rotateAnimation.cancel();
    }

    /**
     * //暂停音乐时，”留声机”指针对应的样式--->偏离音乐转盘
     */
    private void musicIsPausePointOut() {
        rotateAnimation2 = new RotateAnimation(0f, -25f,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation2.setDuration(300);
        rotateAnimation2.setInterpolator(new LinearInterpolator());
        rotateAnimation2.setRepeatCount(0);
        rotateAnimation2.setFillAfter(true);
        needleImgV.setAnimation(rotateAnimation2);
        rotateAnimation2.cancel();
    }

    /**
     * 三种播放模式
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPlayMode() {
        //全部循环
        if (playMode == 0) {
            //默认循环播放
            if (position == Common.mMusicList.size() - 1) {
                position = 0;// 第一首
                setPlayModeStyle();
            } else {
                position++;
                setPlayModeStyle();
            }
        } else if (playMode == 1) { //单曲循环
            //position不需要更改
            setPlayModeStyle();
        } else if (playMode == 2) { //随机
            //随机播放 利用的是伪随机的函数Math.random()  随机产生的数字在0-1之间的一个double数字
            position = (int) (Math.random() * Common.mMusicList.size());
            setPlayModeStyle();
        }
    }

    /**
     * 设置播放样式
     */
    private void setPlayModeStyle() {
        mediaPlayer.reset();
        objectAnimator.pause();
        needleImgV.startAnimation(rotateAnimation2);
        prevAndNextPlaying(Common.mMusicList.get(position).getPath());
    }

    /**
     * 求换按钮
     * 设置播放模式
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setBtnMode() {
        //全部循环 顺序播放
        if (playMode == 0) {
            //默认循环播放
            if (position == Common.mMusicList.size() - 1) {
                if (buttonWitch == 1) { //上一曲
                    //判断歌曲列表是否只有一首，若只有一首，在确定position的位置
                    if (Common.mMusicList.size() == 1) {
                        position = Common.mMusicList.size() - 1;
                    } else {
                        position--;
                    }
                    setPlayModeStyle();
                } else if (buttonWitch == 2) {  //下一曲
                    // 第一首
                    position = 0;
                    setPlayModeStyle();
                }
            } else if (position == 0) {
                if (buttonWitch == 1) { //上一曲
                    position = Common.mMusicList.size() - 1;
                    setPlayModeStyle();
                } else if (buttonWitch == 2) {  //下一曲
                    position++;
                    setPlayModeStyle();
                }
            } else {
                if (buttonWitch == 1) { //上一曲
                    position--;
                    setPlayModeStyle();
                } else if (buttonWitch == 2) { //下一曲
                    position++;
                    setPlayModeStyle();
                }
            }
        } else if (playMode == 1) {  //单曲循环
            //position不需要更改
            setPlayModeStyle();
        } else if (playMode == 2) {  //随机
            //随机播放 利用的是伪随机的函数Math.random()  随机产生的数字在0-1之间的一个double数字
            position = (int) (Math.random() * Common.mMusicList.size());
            setPlayModeStyle();
        }
    }

    /**
     * 格式化 时间 数字
     *
     * @param length
     * @return
     */
    private String formatTime(int length) {
        Date date = new Date(length);
        //规定固定的格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String totalTime = simpleDateFormat.format(date);
        return totalTime;
    }

    /**
     * 绑定id，设置监听
     */
    private void findViewByIdAndSetListener() {
        titleTv = findViewById(R.id.music_title_tv);
        artistTv = findViewById(R.id.music_artist_tv);
        bgImgV = findViewById(R.id.music_bg_imgv);
        currentTv = findViewById(R.id.music_current_tv);
        totalTv = findViewById(R.id.music_total_tv);
        prevImgV = findViewById(R.id.music_prev_imgv);
        nextImgV = findViewById(R.id.music_next_imgv);
        discImgV = findViewById(R.id.music_disc_imagv);
        needleImgV = findViewById(R.id.music_needle_imag);
        pauseImgV = findViewById(R.id.music_pause_imgv);
        downImg = findViewById(R.id.music_down_imgv);
        seekBar = findViewById(R.id.music_seekbar);
        styleImg = findViewById(R.id.music_play_btn_loop_img);
        pauseImgV.setOnClickListener(this);
        prevImgV.setOnClickListener(this);
        nextImgV.setOnClickListener(this);
        downImg.setOnClickListener(this);
        styleImg.setOnClickListener(this);

    }

    /**
     * 上一曲  下一曲  暂停  播放
     * onClick（）点击监听
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_prev_imgv: //上一曲
                buttonWitch = 1;
                setBtnMode();
                break;

            case R.id.music_next_imgv: //下一曲
                buttonWitch = 2;
                setBtnMode();
                break;

            case R.id.music_pause_imgv: //暂停
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    objectAnimator.pause();
                    needleImgV.startAnimation(rotateAnimation2);
                    pauseImgV.setImageResource(R.drawable.ic_play_btn_play);
                } else { //播放
                    mediaPlayer.start();
                    objectAnimator.resume();
                    needleImgV.startAnimation(rotateAnimation);
                    pauseImgV.setImageResource(R.drawable.ic_play_btn_pause);
                }
                break;

            case R.id.music_play_btn_loop_img:  //播放模式
                i++;
                if (i % 3 == 1) { //单曲循环
                    Toast.makeText(MMusicActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                    playMode = 1;
                    styleImg.setImageResource(R.drawable.ic_play_btn_one);
                }
                if (i % 3 == 2) { //随机播放
                    Toast.makeText(MMusicActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                    playMode = 2;
                    styleImg.setImageResource(R.drawable.ic_play_btn_shuffle);
                }
                if (i % 3 == 0) { //顺序播放
                    Toast.makeText(MMusicActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                    playMode = 0;
                    styleImg.setImageResource(R.drawable.ic_play_btn_loop);
                }
                break;

            case R.id.music_down_imgv:  //音乐播放界面的返回图片按钮

                break;

            default:
                break;
        }

    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        for (MMusic music : Common.mMusicList
                ) {
            music.isPlaying = false;
        }
        Common.mMusicList.get(position).isPlaying = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        i = 0;
        isStop = false;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }


//    private int position;
//    private ImageView pauseImgV;
//    private SeekBar seekBar;

//    private MMusicService mMusicService;
//    private String path;
//    private boolean bound;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_music);
//        findViewByIdAndSetListener();
//        Intent intent = getIntent();
//        //得到传过来的值
//        position = intent.getIntExtra("position",0);
//
//        //seekBar设置监听 (进度条的拖动)
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if (b){
//                    MMusicService.mediaPlayer.seekTo(i);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.music_pause_imgv:
//                Intent intent = new Intent(MMusicActivity.this,MMusicService.class);
//                intent.putExtra("position",position);
//                startService(intent);
//
////                path = Common.mMusicList.get(position).getPath();
////                bindService(intent,connection,Context.BIND_AUTO_CREATE);
//                break;
//        }
//    }

//    /**
//     *
//     */
//    private void findViewByIdAndSetListener(){
//        pauseImgV = findViewById(R.id.music_pause_imgv);
//        seekBar = findViewById(R.id.music_seekbar);
//
//        pauseImgV.setOnClickListener(this);
//    }


//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            MMusicService.MusicBinder binder = (MMusicService.MusicBinder)iBinder;
//            mMusicService = binder.getService();
//            mMusicService.playMusic(path);
//            bound=true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            bound = false;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        if (bound){
//            unbindService(connection);
//            bound= false;
//        }
//        super.onDestroy();
//    }
}
