package com.meizidelegate.source.model;

public class KanlemaStream {
    int quality;
    String url;

    public KanlemaStream() {}

    @Override
    public String toString() {
        return "KanlemaStream{" +
                "quality=" + quality +
                ", url='" + url + '\'' +
                '}';
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
