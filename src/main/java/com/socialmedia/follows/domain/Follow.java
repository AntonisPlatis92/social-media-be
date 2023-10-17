package com.socialmedia.follows.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Follow {
    private UUID followerId;
    private UUID followingId;
    private Instant creationTime;
}
