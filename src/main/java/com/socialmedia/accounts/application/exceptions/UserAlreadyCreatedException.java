package com.socialmedia.accounts.application.exceptions;

public class UserAlreadyCreatedException extends RuntimeException {
    public UserAlreadyCreatedException(String message) {
        super(message);
    }
}
