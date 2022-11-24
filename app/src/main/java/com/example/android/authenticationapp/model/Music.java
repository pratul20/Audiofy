package com.example.android.authenticationapp.model;

import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import java.io.Serializable;
import java.util.ArrayList;

public class Music implements Serializable {
    private String musicId;
    private String musicLink;
    private String musicType;
    private String musicName;
    private String musicDuration;
    private String thumbnail;
    private LottieAnimationView animationView;
    private  Boolean isPlaying;

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public Music () {
    }

    public Music(String musicName, String musicDuration) {
        this.musicName = musicName;
        this.musicDuration = musicDuration;
    }

    public Music(String musicName, String musicType, String musicDuration, String thumbnail, String musicLink, Boolean isPlaying) {
        this.musicName = musicName;
        this.musicType = musicType;
        this.musicDuration = musicDuration;
        this.thumbnail = thumbnail;
        this.musicLink = musicLink;
        this.isPlaying = isPlaying;
    }

    public Music(String musicId, String musicName, String musicType, String musicDuration, String thumbnail, String musicLink, Boolean isPlaying) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicType = musicType;
        this.musicDuration = musicDuration;
        this.thumbnail = thumbnail;
        this.musicLink = musicLink;
        this.isPlaying=isPlaying;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicDuration() {
        return musicDuration;
    }

    public void setMusicDuration(String musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<Music> getMusicList(){
        ArrayList<Music> musicList = new ArrayList<>();
        return musicList;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getMusicLink() {
        return musicLink;
    }

    public void setMusicLink(String musicLink) {
        this.musicLink = musicLink;
    }

}
