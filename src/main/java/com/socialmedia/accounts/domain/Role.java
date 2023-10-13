package com.socialmedia.accounts.domain;

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
    private boolean hasCommentsLimit;
    private Long commentsLimit;
    private Instant creationTime;
}
