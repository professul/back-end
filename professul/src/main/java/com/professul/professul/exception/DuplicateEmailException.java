package com.professul.professul.exception;

public class DuplicateEmailException extends EmailAlreadyExistsException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
