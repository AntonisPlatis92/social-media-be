package com.socialmedia.accounts.adapter.out.database;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.CreateUserPort;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class CreateUserAdapter implements CreateUserPort {
    private static final String CREATE_USER_STATEMENT = "INSERT INTO users (email,hashed_password,verified,role_id,creation_time) VALUES (?,?,?,?,?);";
    @Override
    public void createUser(User user) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_USER_STATEMENT);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getHashedPassword());
            preparedStatement.setBoolean(3, user.isVerified());
            preparedStatement.setLong(4, user.getRoleId());
            preparedStatement.setTimestamp(5, Timestamp.from(user.getCreationTime()));
            preparedStatement.executeUpdate();
        });
    }
}
