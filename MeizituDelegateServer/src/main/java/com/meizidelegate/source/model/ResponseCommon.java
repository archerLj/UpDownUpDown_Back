package com.meizidelegate.source.model;

public class ResponseCommon {

    String code;
    String msg;
    Integer truncks;
    String fileMD5;

    public  ResponseCommon() {}

    @Override
    public String toString() {
        return "ResponseCommon{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", truncks=" + truncks +
                ", fileMD5='" + fileMD5 + '\'' +
                '}';
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public Integer getTruncks() {
        return truncks;
    }

    public void setTruncks(Integer truncks) {
        this.truncks = truncks;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
