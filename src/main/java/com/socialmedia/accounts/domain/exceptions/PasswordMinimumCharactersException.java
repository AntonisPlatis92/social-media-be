package com.socialmedia.accounts.domain.exceptions;

public class PasswordMinimumCharactersException extends RuntimeException {
    public PasswordMinimumCharactersException(String message) {
        super(message);
    }
}
