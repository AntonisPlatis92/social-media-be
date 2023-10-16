package com.socialmedia.content.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Comment {
    private UUID id;
    private UUID userId;
    private UUID postId;
    private String body;
    private Instant creationTime;
}
