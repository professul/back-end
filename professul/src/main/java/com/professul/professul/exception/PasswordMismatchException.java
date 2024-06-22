package com.professul.professul.exception;

public class PasswordMismatchException extends UserModificationException{
    public PasswordMismatchException(String message) {
        super(message);
    }
}
