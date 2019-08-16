package com.meizidelegate.source.model;

public class BiaoqingCover {

    Boolean isGif;
    String urlPrefix;
    String imgId;
    String imgName;
    String aspectRatio;
    String url;
    String weakUrl;
    String gifUrl;
    String firstUrl;
    String id;

    @Override
    public String toString() {
        return "BiaoqingCover{" +
                "isGif='" + isGif + '\'' +
                ", urlPrefix='" + urlPrefix + '\'' +
                ", imgId='" + imgId + '\'' +
                ", imgName='" + imgName + '\'' +
                ", aspectRatio='" + aspectRatio + '\'' +
                ", url='" + url + '\'' +
                ", weakUrl='" + weakUrl + '\'' +
                ", gifUrl='" + gifUrl + '\'' +
                ", firstUrl='" + firstUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public BiaoqingCover() {}

    public Boolean getGif() {
        return isGif;
    }

    public void setGif(Boolean gif) {
        isGif = gif;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWeakUrl() {
        return weakUrl;
    }

    public void setWeakUrl(String weakUrl) {
        this.weakUrl = weakUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getFirstUrl() {
        return firstUrl;
    }

    public void setFirstUrl(String firstUrl) {
        this.firstUrl = firstUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
