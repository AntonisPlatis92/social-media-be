package com.socialmedia.adapters.out.database;

import com.socialmedia.application.domain.entities.User;
import com.socialmedia.application.domain.utils.database.DatabaseUtils;
import com.socialmedia.application.ports.out.LoadUserPort;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;

public class LoadUserAdapter implements LoadUserPort {
    private final UserMapper mapper;

    public LoadUserAdapter(UserMapper mapper) {
        this.mapper = mapper;
    }
    private static final String LOAD_USER_STATEMENT = "SELECT * FROM users WHERE email = '%s';";

    @Override
    public User loadUser(String email) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_USER_STATEMENT,email);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String userEmail = resultSet.getString("email");
                String hashedPassword = resultSet.getString("hashed_password");
                boolean verified = resultSet.getBoolean("verified");
                Long roleId = resultSet.getLong("role_id");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return mapper.mapToUserEntity(
                        userEmail,
                        hashedPassword,
                        verified,
                        roleId,
                        creationTime
                );
            }
            else {return null;}
        });
    }
}
