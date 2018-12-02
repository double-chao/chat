package com.lcc.administrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.administrator.mymusic.R;
import com.lcc.administrator.vo.MMusic;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.Holder> {

    private Context context;
    private List<MMusic> musicList;

    public RVAdapter(Context context, List<MMusic> musicList) {
        this.musicList = musicList;
        this.context = context;
    }

    @Override
    public RVAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RVAdapter.Holder holder, final int position) {
        //头像  标题  歌手名字的设置
        holder.iv_avatar.setImageBitmap(musicList.get(position).getAlbumBip());
        holder.tvTitle.setText(musicList.get(position).getTitle());
        holder.tvArtist.setText(musicList.get(position).getArtist()+ "-"
                + musicList.get(position).getAlbum());
        //播放时有竖线View是否显示
        if (musicList.get(position).isPlaying()){
            holder.iv_isPlayingView.setVisibility(View.VISIBLE);
        }else {
            holder.iv_isPlayingView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvArtist;

        public TextView tvDelete;
        public TextView tvTop;

        public ImageView iv_avatar;
        public ImageView iv_share;

        public View iv_isPlayingView;

        public FrameLayout frameLayout;

        public Holder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.musicItem_title_tv);
            tvArtist=(TextView)itemView.findViewById(R.id.musicItem_artist_tv);

            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvTop = (TextView) itemView.findViewById(R.id.tvTop);

            iv_avatar=(ImageView)itemView.findViewById(R.id.musicItem_album_iv);
            iv_share=(ImageView)itemView.findViewById(R.id.iv_share);

            iv_isPlayingView=(View) itemView.findViewById(R.id.musicItem_playing_v);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.layout_frame);
        }
    }
}
