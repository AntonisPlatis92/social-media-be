package com.socialmedia.posts.adapter.out.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "following_posts")
public class FollowingPostViewJpa {
    @Id
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "post_id")
    private UUID postId;
    @Column(name = "post_user_email")
    private String postUserEmail;
    @Column(name = "post_body")
    private String postBody;
    @Column(name = "post_creation_time")
    private Instant postCreationTime;
}
