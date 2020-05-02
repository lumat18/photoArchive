package com.photoarchive.exceptions;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException() {
        super("Email not found");
    }
}
