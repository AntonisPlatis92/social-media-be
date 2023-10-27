package com.socialmedia.posts.adapter.out.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments_on_own_and_following_posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentOnOwnAndFollowingPostsViewJpa {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "comment_user_email")
    private String commentUserEmail;

    @Column(name = "comment_body")
    private String commentBody;

    @Column(name = "comment_creation_time")
    private Instant commentCreationTime;
}
