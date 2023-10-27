package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.application.port.out.RemoveFollowPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.Query;

import java.util.UUID;

public class RemoveFollowJpaAdapter implements RemoveFollowPort {

    @Override
    public void removeFollowByFollowerAndFollowingId(UUID followerId, UUID followingId) {
        JpaDatabaseUtils.doInTransaction(entityManager -> {
            Query query = entityManager.createQuery(
                    "DELETE FROM FollowJpa f WHERE f.followId.followerId = :followerId AND f.followId.followingId = :followingId");
            query.setParameter("followerId", followerId);
            query.setParameter("followingId", followingId);
            query.executeUpdate();
        });
    }
}
