package com.socialmedia.adapters.out.database;

import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.ports.out.VerifyUserPort;

import java.sql.PreparedStatement;

public class VerifyUserAdapter implements VerifyUserPort {
    private static final String VERIFY_USER_STATEMENT = "UPDATE users SET verified = true WHERE email = (?);";

    @Override
    public void verifyUser(String email) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(VERIFY_USER_STATEMENT);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        });
    }
}
