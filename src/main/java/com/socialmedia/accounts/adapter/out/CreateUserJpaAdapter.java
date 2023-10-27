package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.out.mappers.AccountsJpaMapper;
import com.socialmedia.accounts.application.port.out.CreateUserPort;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.JpaDatabaseUtils;

public class CreateUserJpaAdapter implements CreateUserPort {
    @Override
    public void createUser(User user) {
        JpaDatabaseUtils.doInTransaction(entityManager -> {
            entityManager.persist(AccountsJpaMapper.mapFromUserToUserJpaEntity(user));
        });
    }
}
