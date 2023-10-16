package com.socialmedia.content.domain;

import java.time.Instant;

public class Comment {
    private Long id;
    private Long userId;
    private Long postId;
    private String body;
    private Instant creationTime;
}
