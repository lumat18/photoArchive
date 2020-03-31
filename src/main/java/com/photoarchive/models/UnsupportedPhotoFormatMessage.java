package com.photoarchive.models;

import lombok.Data;

@Data
public class UnsupportedPhotoFormatMessage {
    private final String text = "Unsupported file format";
    private boolean isActive = false;
}
