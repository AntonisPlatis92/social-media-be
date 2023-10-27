package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.out.mappers.JpaMapper;
import com.socialmedia.accounts.application.port.out.CreateFollowPort;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.utils.database.JpaDatabaseUtils;

public class CreateFollowJpaAdapter implements CreateFollowPort {
    @Override
    public void createFollow(Follow follow) {
        JpaDatabaseUtils.doInTransaction(entityManager -> {
            entityManager.persist(JpaMapper.mapFromFollowToFollowJpaEntity(follow));
        });
    }
}
