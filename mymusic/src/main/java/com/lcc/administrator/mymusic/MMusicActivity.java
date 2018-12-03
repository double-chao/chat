package com.lcc.administrator.mymusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
*  @author lcc
*  created at 2018/12/3
*/
public class MMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private int position;
    private ImageView pauseImgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        findViewByIdAndSetListener();
        Intent intent = getIntent();
        //得到传过来的值
        position = intent.getIntExtra("position",0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.music_pause_imgv:
                Intent intent = new Intent(MMusicActivity.this,MMusicService.class);
                intent.putExtra("position",position);
                startService(intent);
                break;
        }
    }

    private void findViewByIdAndSetListener(){
        pauseImgV = findViewById(R.id.music_pause_imgv);
        pauseImgV.setOnClickListener(this);
    }
}
