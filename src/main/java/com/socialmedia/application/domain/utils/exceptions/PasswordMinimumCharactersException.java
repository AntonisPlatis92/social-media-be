package com.socialmedia.application.domain.utils.exceptions;

public class PasswordMinimumCharactersException extends RuntimeException {
    public PasswordMinimumCharactersException(String message) {
        super(message);
    }
}
