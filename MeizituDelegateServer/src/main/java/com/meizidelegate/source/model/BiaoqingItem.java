package com.meizidelegate.source.model;

public class BiaoqingItem {
    String id;
    String name;
    BiaoqingCover cover;
    String version;

    public BiaoqingItem() {}

    @Override
    public String toString() {
        return "BiaoqingItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cover=" + cover +
                ", version='" + version + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BiaoqingCover getCover() {
        return cover;
    }

    public void setCover(BiaoqingCover cover) {
        this.cover = cover;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
