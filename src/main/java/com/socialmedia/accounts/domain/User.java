package com.socialmedia.accounts.domain;

import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.application.port.out.RemoveFollowPort;
import com.socialmedia.accounts.domain.commands.CreateUserCommand;
import com.socialmedia.accounts.domain.exceptions.FollowAlreadyExistsException;
import com.socialmedia.accounts.domain.exceptions.FollowNotFoundException;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.utils.encoders.PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class User {
    private UUID userId;
    private String email;
    private String hashedPassword;
    private boolean verified;
    private Role role;
    private Instant creationTime;
    private List<Follow> followers;
    private List<Follow> following;


    public static User createUserFromCommandAndRole(CreateUserCommand command, Role role){
        return new User(
                UUID.randomUUID(),
                command.email(),
                PasswordEncoder.encode(command.password()),
                false,
                role,
                Instant.now(ClockConfig.utcClock()),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public void follow(User followingUser, CreateFollowPort createFollowPort) {
        List<UUID> followingUserIds = following.stream()
                .map(Follow::getFollowingId)
                .toList();
        if (followingUserIds.contains(followingUser.userId)) {throw new FollowAlreadyExistsException("Follow already exists.");}

        createFollowPort.createFollow(
                Follow.createFollowFromFollowerIdAndFollowingId(
                        this.userId,
                        followingUser.userId
                ));
    }

    public void unfollow(User unfollowingUser, RemoveFollowPort removeFollowPort) {
        List<UUID> followingUserIds = following.stream()
                .map(Follow::getFollowingId)
                .toList();
        if (!followingUserIds.contains(unfollowingUser.userId)) {throw new FollowNotFoundException("Follow already exists.");}

        removeFollowPort.removeFollowByFollowerAndFollowingId(
                this.userId,
                unfollowingUser.userId
        );
    }
}
