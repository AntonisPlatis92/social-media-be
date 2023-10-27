package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.out.jpa.FollowJpa;
import com.socialmedia.accounts.adapter.out.jpa.RoleJpa;
import com.socialmedia.accounts.adapter.out.jpa.UserJpa;
import com.socialmedia.accounts.adapter.out.mappers.AccountsJpaMapper;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadUserJpaAdapter implements LoadUserPort {
    @Override
    public Optional<User> loadUserByEmail(String email) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            Query query = entityManager.createQuery("SELECT u FROM UserJpa u WHERE u.email = :email", UserJpa.class);
            query.setParameter("email", email);
            @SuppressWarnings("unchecked")
            List<UserJpa> users = query.getResultList();
            UserJpa userJpa;
            if (!users.isEmpty()) {
                userJpa = users.get(0);
            } else {
                return Optional.empty();
            }

            Optional<RoleJpa> maybeRoleJpa = Optional.ofNullable(entityManager.find(RoleJpa.class, userJpa.getRoleId()));
            if (maybeRoleJpa.isEmpty()) {return Optional.empty();}
            RoleJpa roleJpa = maybeRoleJpa.get();

            TypedQuery<FollowJpa> followersQuery = entityManager.createQuery(
                    "SELECT f FROM FollowJpa f WHERE f.followId.followingId = :userId", FollowJpa.class);
            followersQuery.setParameter("userId", userJpa.getId());
            List<FollowJpa> followerEntities = followersQuery.getResultList();


            TypedQuery<FollowJpa> followingQuery = entityManager.createQuery(
                        "SELECT f FROM FollowJpa f WHERE f.followId.followerId = :userId", FollowJpa.class);
            followingQuery.setParameter("userId", userJpa.getId());
            List<FollowJpa> followingEntities = followingQuery.getResultList();

            return Optional.of(AccountsJpaMapper.mapFromJpaEntitiesToUser(userJpa, roleJpa, followerEntities, followingEntities));
        });
    }

    @Override
    public Optional<User> loadUserById(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            Optional<UserJpa> maybeUserJpa = Optional.ofNullable(entityManager.find(UserJpa.class, userId));
            if (maybeUserJpa.isEmpty()) {return Optional.empty();}
            UserJpa userJpa = maybeUserJpa.get();

            Optional<RoleJpa> maybeRoleJpa = Optional.ofNullable(entityManager.find(RoleJpa.class, userJpa.getRoleId()));
            if (maybeRoleJpa.isEmpty()) {return Optional.empty();}
            RoleJpa roleJpa = maybeRoleJpa.get();

            TypedQuery<FollowJpa> followersQuery = entityManager.createQuery(
                    "SELECT f FROM FollowJpa f WHERE f.followId.followingId = :userId", FollowJpa.class);
            followersQuery.setParameter("userId", userJpa.getId());
            List<FollowJpa> followerEntities = followersQuery.getResultList();


            TypedQuery<FollowJpa> followingQuery = entityManager.createQuery(
                    "SELECT f FROM FollowJpa f WHERE f.followId.followerId = :userId", FollowJpa.class);
            followingQuery.setParameter("userId", userJpa.getId());
            List<FollowJpa> followingEntities = followingQuery.getResultList();

            return Optional.of(AccountsJpaMapper.mapFromJpaEntitiesToUser(userJpa, roleJpa, followerEntities, followingEntities));
        });
    }
}
