package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.LoadUserPort;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
public class LoadUserAdapter implements LoadUserPort {

    private static final String LOAD_USER_BY_EMAIL_STATEMENT = "SELECT * FROM users WHERE email = '%s';";
    private static final String LOAD_USER_BY_ID_STATEMENT = "SELECT * FROM users WHERE id = '%s';";

    @Override
    public Optional<User> loadUserByEmail(String email) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_USER_BY_EMAIL_STATEMENT,email);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                UUID userId = (UUID) resultSet.getObject("id");
                String userEmail = resultSet.getString("email");
                String hashedPassword = resultSet.getString("hashed_password");
                boolean verified = resultSet.getBoolean("verified");
                Long roleId = resultSet.getLong("role_id");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new User(
                        userId,
                        userEmail,
                        hashedPassword,
                        verified,
                        roleId,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public Optional<User> loadUserById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_USER_BY_ID_STATEMENT,id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                UUID userId = (UUID) resultSet.getObject("id");
                String userEmail = resultSet.getString("email");
                String hashedPassword = resultSet.getString("hashed_password");
                boolean verified = resultSet.getBoolean("verified");
                Long roleId = resultSet.getLong("role_id");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new User(
                        userId,
                        userEmail,
                        hashedPassword,
                        verified,
                        roleId,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }
}
