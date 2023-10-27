package com.socialmedia.accounts.adapter.out.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "follows")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FollowJpa {
    @EmbeddedId
    private FollowId followId;

    @Column(name = "creation_time", nullable = false)
    private Instant creationTime;
}
