package com.socialmedia.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Role {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "has_post_chars_limit")
    private boolean hasPostCharsLimit;
    @Column(name = "post_chars_limit")
    private Long postCharsLimit;
    @Column(name = "has_comments_limit")
    private boolean commentsLimit;
    @Column(name = "creation_time")
    private Instant creationTime;

}
