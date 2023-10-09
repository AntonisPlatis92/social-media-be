package com.socialmedia.utils.exceptions;

public class PasswordMinimumCharactersException extends RuntimeException {
    public PasswordMinimumCharactersException(String message) {
        super(message);
    }
}
