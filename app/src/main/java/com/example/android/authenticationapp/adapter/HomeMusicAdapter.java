package com.example.android.authenticationapp.adapter;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.android.authenticationapp.R;
import com.example.android.authenticationapp.homeactivity;
import com.example.android.authenticationapp.model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeMusicAdapter extends RecyclerView.Adapter<HomeMusicAdapter.ViewHolder>{

    private Context context;
    private OnSongClickListener mOnSongClickListener;

    private List<Music> musicList;

    public HomeMusicAdapter(Context context, List<Music> musicList, OnSongClickListener mOnSongClickListener) {
        this.context = context;
        this.musicList = musicList;
        this.mOnSongClickListener = mOnSongClickListener;
    }

    public HomeMusicAdapter(homeactivity context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    public void setNewList(ArrayList<Music> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeMusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_card, parent, false);
        return new ViewHolder(view, mOnSongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMusicAdapter.ViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.music_name_tv.setText(music.getMusicName());
        holder.music_duration_tv.setText(music.getMusicDuration());
    }

    @Override
    public int getItemCount() {
     return musicList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView music_name_tv;
        TextView music_duration_tv;
        OnSongClickListener onSongClickListener;
        LottieAnimationView play;

        public ViewHolder(@NonNull View itemView, OnSongClickListener onSongClickListener) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
            music_name_tv = itemView.findViewById(R.id.music_name_tv);
            music_duration_tv = itemView.findViewById(R.id.music_duration_tv);
            play = itemView.findViewById(R.id.animation);
            this.onSongClickListener = onSongClickListener;
        }

        @Override
        public void onClick(View view) {
            onSongClickListener.onSongClick(getAbsoluteAdapterPosition(), play);
            play.setAnimation(R.raw.music_spectrum);
            play.playAnimation();
        }
    }

    public interface OnSongClickListener{
        void onSongClick(int position,LottieAnimationView animationView);
    }

}
