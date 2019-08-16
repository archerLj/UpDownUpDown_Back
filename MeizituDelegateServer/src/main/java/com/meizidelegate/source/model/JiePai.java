package com.meizidelegate.source.model;

public class JiePai {
    String id;
    String img_src;

    @Override
    public String toString() {
        return "JiePai{" +
                "id='" + id + '\'' +
                ", img_src='" + img_src + '\'' +
                '}';
    }

    public JiePai() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }
}
