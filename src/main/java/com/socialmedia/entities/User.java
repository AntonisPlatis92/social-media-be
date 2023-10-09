package com.socialmedia.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "hashed_password")
    private String hashedPassword;
    @Column(name = "verified")
    private boolean verified;
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "creation_time")
    private Instant creationTime;

    public void verifyUser() {
        this.verified = true;
    }
}
