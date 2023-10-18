package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.CreateUserPort;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class CreateUserAdapter implements CreateUserPort {
    private static final String CREATE_USER_STATEMENT = "INSERT INTO users (id,email,hashed_password,verified,role_id,creation_time) VALUES (?,?,?,?,?,?);";
    @Override
    public void createUser(User user) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_USER_STATEMENT);
            preparedStatement.setObject(1, user.getUserId());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getHashedPassword());
            preparedStatement.setBoolean(4, user.isVerified());
            preparedStatement.setLong(5, user.getRole().getId());
            preparedStatement.setTimestamp(6, Timestamp.from(user.getCreationTime()));
            preparedStatement.executeUpdate();
        });
    }
}
