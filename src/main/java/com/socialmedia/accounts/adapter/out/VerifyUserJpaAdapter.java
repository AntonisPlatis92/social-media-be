package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.application.port.out.VerifyUserPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.Query;

public class VerifyUserJpaAdapter implements VerifyUserPort {
    @Override
    public void verifyUser(String email) {
        JpaDatabaseUtils.doInTransaction(entityManager -> {
            Query query = entityManager.createQuery(
                    "UPDATE UserJpa u SET u.verified = true WHERE u.email = :email");
            query.setParameter("email", email);
            query.executeUpdate();
        });
    }
}
