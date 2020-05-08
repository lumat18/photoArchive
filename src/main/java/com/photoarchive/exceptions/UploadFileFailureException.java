package com.photoarchive.exceptions;

import java.io.IOException;

public class UploadFileFailureException extends IOException {
    public UploadFileFailureException() {
        super("Failed to upload the file");
    }
}
