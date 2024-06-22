package com.professul.professul.exception;

public class InvalidPasswordException extends UserModificationException{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
