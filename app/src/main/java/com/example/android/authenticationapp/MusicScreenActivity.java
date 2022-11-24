package com.example.android.authenticationapp;

import static com.example.android.authenticationapp.homeactivity.play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.android.authenticationapp.model.Music;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicScreenActivity extends AppCompatActivity {

    Boolean is_playing = true;
    static MediaPlayer mediaPlayer;
    AudioManager audioManager;
    ImageView stop_btn, thumbnail_img;
    TextView songName, singerName;
    Intent mIntent;
    Context context;
    int current_position;
    final String prog = "prog", prevUrl = "prevUrl";
    String url, thumbnail;
    LottieAnimationView previous_btn, next_btn, play_pause;
    Animation animation;
    boolean isRunning;
    int progress;
    ArcSeekBar seekProg;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);
        mIntent = getIntent();
        context = this;
        Utils.statusBarcolor(MusicScreenActivity.this, R.color.light);


        Bundle args = mIntent.getBundleExtra("BUNDLE");
        current_position = args.getInt("position", 0);

        if(mediaPlayer!=null)
        {
            mediaPlayer.start();
            mediaPlayer.release();
        }

        init();
        playSong();


    }


    void init() {
        previous_btn = findViewById(R.id.prev_btn);
        next_btn = findViewById(R.id.next_btn);

        thumbnail_img = findViewById(R.id.thumbnail_img);
        play_pause = findViewById(R.id.play_pause);

        songName = findViewById(R.id.song_name);
        singerName = findViewById(R.id.singer_name);


        play_pause.setAnimation(R.raw.play_pause);
        previous_btn.setAnimation(R.raw.previous_track);
        next_btn.setAnimation(R.raw.next_track);

        songName.setText(AllSongs.musicArrayList.get(current_position).getMusicName());
    }


    void playSong() {




        thumbnail = AllSongs.musicArrayList.get(current_position).getThumbnail();
        url = AllSongs.musicArrayList.get(current_position).getMusicLink();
        Glide.with(this)
                .load(thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_music_note)
                .into(thumbnail_img);


         mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

//        if(mediaPlayer!=null) {
//            duration_tv.setText(mediaPlayer.getDuration());
//        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                thumbnail_img.animate().rotationBy(360).withEndAction(this).setDuration(5000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };

        thumbnail_img.animate().rotationBy(360).withEndAction(runnable).setDuration(5000)
                .setInterpolator(new LinearInterpolator()).start();

        if (is_playing) {
            play_pause.setSpeed(1);
            play_pause.playAnimation();
            mediaPlayer.start();
            is_playing=false;
        }

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_playing) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            thumbnail_img.animate().rotationBy(360).withEndAction(this).setDuration(5000)
                                    .setInterpolator(new LinearInterpolator()).start();
                        }
                    };

                    thumbnail_img.animate().rotationBy(360).withEndAction(runnable).setDuration(5000)
                            .setInterpolator(new LinearInterpolator()).start();
                    play_pause.setSpeed(1);
                    play_pause.playAnimation();
                    mediaPlayer.start();
                    is_playing=false;
                } else {
                    thumbnail_img.animate().cancel();
                    play_pause.setSpeed(-1);
                    play_pause.playAnimation();
                    mediaPlayer.pause();
                    is_playing=true;
                }
            }
        });


        previous_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                previous_btn.playAnimation();
                mediaPlayer.stop();
                AllSongs.musicArrayList.get(current_position).setPlaying(false);
                if (current_position == 0) {
                    current_position = AllSongs.musicArrayList.size() - 1;
                } else {
                    current_position -= 1;
                }
                songName.setText(AllSongs.musicArrayList.get(current_position).getMusicName());
                url = AllSongs.musicArrayList.get(current_position).getMusicLink();
                thumbnail = AllSongs.musicArrayList.get(current_position).getThumbnail();
                AllSongs.musicArrayList.get(current_position).setPlaying(true);
                Glide.with(context)
                        .load(thumbnail)
                        .centerCrop()
                        .placeholder(R.drawable.ic_music_note)
                        .into(thumbnail_img);
                mediaPlayer.stop();
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();

            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                AllSongs.musicArrayList.get(current_position).setPlaying(false);
                if (current_position == AllSongs.musicArrayList.size() - 1) {
                    current_position = 0;
                } else {
                    current_position += 1;
                }
                AllSongs.musicArrayList.get(current_position).setPlaying(true);
                next_btn.playAnimation();
                songName.setText(AllSongs.musicArrayList.get(current_position).getMusicName());
                url = AllSongs.musicArrayList.get(current_position).getMusicLink();
                thumbnail = AllSongs.musicArrayList.get(current_position).getThumbnail();
                Glide.with(context)
                        .load(thumbnail)
                        .centerCrop()
                        .placeholder(R.drawable.ic_music_note)
                        .into(thumbnail_img);

                mediaPlayer.stop();
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();

            }
        });


//        SeekBar seekVol = findViewById(R.id.seekVol);
//        seekVol.setMax(maxVol);
//        seekVol.setProgress(curVol);
//
//        seekVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
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

        seekProg = findViewById(R.id.seekProg);
        seekProg.setMaxProgress(mediaPlayer.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    isRunning = true;
                    seekProg.setProgress(mediaPlayer.getCurrentPosition());
                    progress = mediaPlayer.getCurrentPosition();
//                    current_time_tv.setText(mediaPlayer.getCurrentPosition());
                }

            }
        }, 0, 100);


        seekProg.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int i) {
                if (!isRunning) {
                    mediaPlayer.seekTo(i);
//                    current_time_tv.setText(i);


                } else {
                    isRunning = !isRunning;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}