package com.socialmedia.content.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Post {
    private UUID id;
    private UUID userId;
    private String body;
    private Instant creationTime;
}
