package com.socialmedia.accounts.adapter.out.database;

import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.utils.database.DatabaseUtils;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.Optional;

@NoArgsConstructor
public class LoadRoleAdapter implements LoadRolePort {

    private static final String LOAD_ROLE_BY_ID_STATEMENT = "SELECT * FROM roles WHERE id = %d;";

    @Override
    public Optional<Role> loadRoleById(Long id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_ROLE_BY_ID_STATEMENT, id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Long roleId = resultSet.getLong("id");
                String roleName = resultSet.getString("role_name");
                boolean hasPostCharsLimit = resultSet.getBoolean("has_post_chars_limit");
                Long postCharsLimit = resultSet.getLong("post_chars_limit");
                boolean hasCommentsLimit = resultSet.getBoolean("has_comments_limit");
                Long commentsLimit = resultSet.getLong("comments_limit");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new Role(
                        roleId,
                        roleName,
                        hasPostCharsLimit,
                        postCharsLimit,
                        hasCommentsLimit,
                        commentsLimit,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }
}
