package com.meizidelegate.source.model;

public class HuabanADFile {
    String key;

    public HuabanADFile() {}

    @Override
    public String toString() {
        return "HuabanADFile{" +
                "key='" + key + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
