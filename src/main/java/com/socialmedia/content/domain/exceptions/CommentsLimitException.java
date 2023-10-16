package com.socialmedia.content.domain.exceptions;

public class CommentsLimitException extends RuntimeException {
    public CommentsLimitException(String message) {
        super(message);
    }
}
