package com.meizidelegate.source.model;

public class HuabanADS {
    String orig_source; // video url
    HuabanADFile file; // video preview key
    String raw_text; // video title
    String seq; // video id

    public  HuabanADS() {}

    @Override
    public String toString() {
        return "HuabanADS{" +
                "orig_source='" + orig_source + '\'' +
                ", file=" + file +
                ", raw_text='" + raw_text + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }

    public String getOrig_source() {
        return orig_source;
    }

    public void setOrig_source(String orig_source) {
        this.orig_source = orig_source;
    }

    public HuabanADFile getFile() {
        return file;
    }

    public void setFile(HuabanADFile file) {
        this.file = file;
    }

    public String getRaw_text() {
        return raw_text;
    }

    public void setRaw_text(String raw_text) {
        this.raw_text = raw_text;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
