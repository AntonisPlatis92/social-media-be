package com.socialmedia.accounts.adapter.out.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FollowId implements Serializable {
    @Column(name = "follower_id")
    private UUID followerId;
    @Column(name = "following_id")
    private UUID followingId;
}

