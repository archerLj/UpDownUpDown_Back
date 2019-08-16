package com.meizidelegate.source.model;

public class Fuli {

    String _id;
    String url;

    public Fuli() {}

    @Override
    public String toString() {
        return "Fuli{" +
                "_id='" + _id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
