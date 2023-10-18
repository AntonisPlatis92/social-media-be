package com.socialmedia.posts.domain.exceptions;

public class PostCharsLimitException extends RuntimeException {
    public PostCharsLimitException(String message) {
        super(message);
    }
}
