package com.meizidelegate.source.model;

public class MeiziListItem {
    String id;
    String title;
    int img_num;
    String thumb_src;
    String thumb_src_min;

    public MeiziListItem() {}

    public String getThumb_src() {
        return thumb_src;
    }

    public void setThumb_src(String thumb_src) {
        this.thumb_src = thumb_src;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg_num() {
        return img_num;
    }

    public void setImg_num(int img_num) {
        this.img_num = img_num;
    }

    public String getThumb_src_min() {
        return thumb_src_min;
    }

    public void setThumb_src_min(String thumb_src_min) {
        this.thumb_src_min = thumb_src_min;
    }
}
