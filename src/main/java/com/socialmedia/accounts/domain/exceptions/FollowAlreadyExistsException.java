package com.socialmedia.accounts.domain.exceptions;

public class FollowAlreadyExistsException extends RuntimeException {
    public FollowAlreadyExistsException(String message) {
        super(message);
    }
}
