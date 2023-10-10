package com.socialmedia.application.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.Instant;

@AllArgsConstructor
@Getter
public class Role {
    private Long id;
    private String roleName;
    private boolean hasPostCharsLimit;
    private Long postCharsLimit;
    private boolean commentsLimit;
    private Instant creationTime;
}
