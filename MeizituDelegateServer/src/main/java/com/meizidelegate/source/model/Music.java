package com.meizidelegate.source.model;

public class Music {

    String albumId;
    String trackId;
    String playPathHq;
    String title;

    public Music() {}

    @Override
    public String toString() {
        return "Music{" +
                "albumId='" + albumId + '\'' +
                ", trackId='" + trackId + '\'' +
                ", playPathHq='" + playPathHq + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getPlayPathHq() {
        return playPathHq;
    }

    public void setPlayPathHq(String playPathHq) {
        this.playPathHq = playPathHq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
