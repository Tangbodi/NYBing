package com.example.demo.Enumeration;

public enum Endpoint {
    Link("api");
    private String api;
    Endpoint(String api) {
        this.api = api;
    }
    public String api() {
        return api;
    }
}
