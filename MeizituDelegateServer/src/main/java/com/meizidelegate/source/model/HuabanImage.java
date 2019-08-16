package com.meizidelegate.source.model;

public class HuabanImage {
    HuabanADFile file;
    String pin_id;

    public HuabanImage() {}

    @Override
    public String toString() {
        return "HuabanImage{" +
                "file=" + file +
                ", pin_id='" + pin_id + '\'' +
                '}';
    }

    public HuabanADFile getFile() {
        return file;
    }

    public void setFile(HuabanADFile file) {
        this.file = file;
    }

    public String getPin_id() {
        return pin_id;
    }

    public void setPin_id(String pin_id) {
        this.pin_id = pin_id;
    }
}
