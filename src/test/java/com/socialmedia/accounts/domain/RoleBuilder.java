package com.socialmedia.accounts.domain;

import com.socialmedia.config.ClockConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@With
@AllArgsConstructor
@Builder
public class RoleBuilder {
    private Long id;
    private String roleName;
    private boolean hasPostCharsLimit;
    private Long postCharsLimit;
    private boolean hasCommentsLimit;
    private Long commentsLimit;
    private Instant creationTime;

    private static final Long FREE_USER_ROLE_ID = 1L;
    private static final Long PREMIUM_USER_ROLE_ID = 2L;
    private static final String FREE_USER_ROLE_NAME = "FREE";
    private static final String PREMIUM_USER_ROLE_NAME = "PREMIUM";
    private static final Long FREE_POST_CHARS_LIMIT = 1000L;
    private static final Long PREMIUM_POST_CHARS_LIMIT = 1000L;
    private static final Long FREE_COMMENTS_LIMIT = 5L;


    public static RoleBuilder aFreeUserRoleBuilder() {
        return RoleBuilder.builder()
                .id(FREE_USER_ROLE_ID)
                .roleName(FREE_USER_ROLE_NAME)
                .hasPostCharsLimit(true)
                .postCharsLimit(FREE_POST_CHARS_LIMIT)
                .hasCommentsLimit(true)
                .commentsLimit(FREE_COMMENTS_LIMIT)
                .creationTime(Instant.now(ClockConfig.utcClock()))
                .build();
    }

    public static RoleBuilder aPremiumUserRoleBuilder() {
        return RoleBuilder.builder()
                .id(PREMIUM_USER_ROLE_ID)
                .roleName(FREE_USER_ROLE_NAME)
                .hasPostCharsLimit(true)
                .postCharsLimit(FREE_POST_CHARS_LIMIT)
                .hasCommentsLimit(true)
                .commentsLimit(FREE_COMMENTS_LIMIT)
                .creationTime(Instant.now(ClockConfig.utcClock()))
                .build();
    }

    public Role build() {
        return Role.builder()
                .id(id)
                .roleName(roleName)
                .hasPostCharsLimit(hasPostCharsLimit)
                .postCharsLimit(postCharsLimit)
                .hasCommentsLimit(hasCommentsLimit)
                .commentsLimit(commentsLimit)
                .creationTime(creationTime)
                .build();
    }
}
