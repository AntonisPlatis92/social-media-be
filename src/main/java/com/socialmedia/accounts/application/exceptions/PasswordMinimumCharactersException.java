package com.socialmedia.accounts.application.exceptions;

public class PasswordMinimumCharactersException extends RuntimeException {
    public PasswordMinimumCharactersException(String message) {
        super(message);
    }
}
