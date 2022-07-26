package com.projuris.projetoStag.utils;

public class Payload {

    private String message;

    public Payload(String message) {
        this.message = message;
    }

    public String toJson(){
        return "{\"message\":\"" + this.message + "\"}";
    }
}
