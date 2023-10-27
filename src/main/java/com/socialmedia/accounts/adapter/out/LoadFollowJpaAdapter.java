package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.adapter.out.jpa.FollowId;
import com.socialmedia.accounts.adapter.out.jpa.FollowJpa;
import com.socialmedia.accounts.adapter.out.mappers.JpaMapper;
import com.socialmedia.accounts.application.port.out.LoadFollowPort;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadFollowJpaAdapter implements LoadFollowPort {

    @Override
    public Optional<Follow> loadFollowByPk(UUID followerId, UUID followingId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            return Optional.ofNullable(entityManager.find(Follow.class, new FollowId(followerId, followingId)));
        });
    }

    @Override
    public FollowsReturnVM loadFollowsByUserId(UUID userId) {
        List<FollowJpa> followerEntities = JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            TypedQuery<FollowJpa> query = entityManager.createQuery(
                    "SELECT f FROM FollowJpa f WHERE f.followId.followingId = :userId", FollowJpa.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        });

        List<FollowJpa> followingEntities = JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            TypedQuery<FollowJpa> query = entityManager.createQuery(
                    "SELECT f FROM FollowJpa f WHERE f.followId.followerId = :userId", FollowJpa.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        });

        return JpaMapper.mapFromFollowJpaEntitiesToFollowsReturnVM(followerEntities, followingEntities);
    }

    @Override
    public List<UUID> loadFollowingUserIdsByUserId(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            TypedQuery<UUID> query = entityManager.createQuery(
                    "SELECT f.followId.followingId FROM FollowJpa f WHERE f.followId.followerId = :userId", UUID.class
            );
            query.setParameter("userId", userId);
            return query.getResultList();
        });
    }
}
