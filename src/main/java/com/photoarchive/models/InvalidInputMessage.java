package com.photoarchive.models;

import lombok.Data;

@Data
public class InvalidInputMessage {
    private String text;
    private boolean isActive = false;


    public void activate(String text) {
        isActive = true;
        this.text = text;
    }
}
