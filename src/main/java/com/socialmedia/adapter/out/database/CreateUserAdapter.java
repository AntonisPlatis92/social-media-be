package com.socialmedia.adapter.out.database;

import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.port.out.CreateUserPort;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;

public class CreateUserAdapter implements CreateUserPort {
    private static final String CREATE_USER_STATEMENT = "INSERT INTO users (email,hashed_password,verified,role_id,creation_time) VALUES (?,?,?,?,?);";
    @Override
    public void createUser(String email, String hashedPassword, boolean verified, Long roleId, Instant creationTime) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_USER_STATEMENT);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setBoolean(3, verified);
            preparedStatement.setLong(4, roleId);
            preparedStatement.setTimestamp(5, Timestamp.from(creationTime));
            preparedStatement.executeUpdate();
        });
    }
}
