package com.socialmedia.accounts.adapter.out.mappers;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.adapter.out.jpa.FollowId;
import com.socialmedia.accounts.adapter.out.jpa.FollowJpa;
import com.socialmedia.accounts.adapter.out.jpa.RoleJpa;
import com.socialmedia.accounts.adapter.out.jpa.UserJpa;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;

import java.util.List;

public class AccountsJpaMapper {
    public static UserJpa mapFromUserToUserJpaEntity(User user) {
        return new UserJpa(
                user.getUserId(),
                user.getEmail(),
                user.getHashedPassword(),
                user.isVerified(),
                user.getRole().getId(),
                user.getCreationTime()
        );
    }

    public static FollowJpa mapFromFollowToFollowJpaEntity(Follow follow) {
        return new FollowJpa(
                new FollowId(
                        follow.getFollowerId(),
                        follow.getFollowingId()
                ),
                follow.getCreationTime()
        );
    }

    public static FollowsReturnVM mapFromFollowJpaEntitiesToFollowsReturnVM(List<FollowJpa> followerEntities, List<FollowJpa> followingEntities) {
        List<String> followerIds = followerEntities.stream()
                .map(follow -> follow.getFollowId().getFollowerId().toString())
                .toList();

        List<String> followingIds = followingEntities.stream()
                .map(follow -> follow.getFollowId().getFollowingId().toString())
                .toList();

        return new FollowsReturnVM(followerIds, followingIds);
    }

    public static Role mapFromRoleJpaToRoleEntity(RoleJpa roleJpa) {
        return new Role(
                roleJpa.getId(),
                roleJpa.getRoleName(),
                roleJpa.isHasPostCharsLimit(),
                roleJpa.getPostCharsLimit(),
                roleJpa.isHasCommentsLimit(),
                roleJpa.getCommentsLimit(),
                roleJpa.getCreationTime()
        );
    }

    public static User mapFromJpaEntitiesToUser(UserJpa userJpa, RoleJpa roleJpa, List<FollowJpa> followersJpa, List<FollowJpa> followingJpa) {
        return new User(
                userJpa.getId(),
                userJpa.getEmail(),
                userJpa.getHashedPassword(),
                userJpa.isVerified(),
                mapFromRoleJpaToRoleEntity(roleJpa),
                userJpa.getCreationTime(),
                followersJpa.stream().map(f -> new Follow(f.getFollowId().getFollowerId(), f.getFollowId().getFollowingId(), f.getCreationTime())).toList(),
                followingJpa.stream().map(f -> new Follow(f.getFollowId().getFollowerId(), f.getFollowId().getFollowingId(), f.getCreationTime())).toList()
        );
    }
}
