package com.meizidelegate.source.model;

import java.util.List;

public class Kanlema {
    String title;
    String cover;
    String docid; // videoID
    List<KanlemaStream> streams;

    public Kanlema() {}

    @Override
    public String toString() {
        return "Kanlema{" +
                "title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", docid='" + docid + '\'' +
                ", streams=" + streams +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public List<KanlemaStream> getStreams() {
        return streams;
    }

    public void setStreams(List<KanlemaStream> streams) {
        this.streams = streams;
    }
}
