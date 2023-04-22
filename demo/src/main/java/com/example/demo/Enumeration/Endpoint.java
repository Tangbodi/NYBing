package com.example.demo.Enumeration;

public enum Endpoint {
    ORIGIN("http://192.168.1.23:3000/");

    private String url;
    Endpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
