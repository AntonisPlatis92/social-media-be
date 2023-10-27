package com.socialmedia.accounts.adapter.out.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoleJpa {
    @Id
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "has_post_chars_limit", nullable = false)
    private boolean hasPostCharsLimit;

    @Column(name = "post_chars_limit")
    private Long postCharsLimit;

    @Column(name = "has_comments_limit", nullable = false)
    private boolean hasCommentsLimit;

    @Column(name = "comments_limit")
    private Long commentsLimit;

    @Column(name = "creation_time", nullable = false)
    private Instant creationTime;
}