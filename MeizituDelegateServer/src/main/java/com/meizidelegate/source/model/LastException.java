package com.meizidelegate.source.model;

import java.util.List;

public class LastException {

    List<String> symbols;
    String reason;
    String name;

    public LastException() {}

    @Override
    public String toString() {
        return "LastException{" +
                "symbols=" + symbols +
                ", reason='" + reason + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
