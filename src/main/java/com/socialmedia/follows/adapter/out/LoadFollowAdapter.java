package com.socialmedia.follows.adapter.out;

import com.socialmedia.follows.application.port.out.LoadFollowPort;
import com.socialmedia.follows.domain.Follow;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class LoadFollowAdapter implements LoadFollowPort {
    private static final String LOAD_FOLLOW_BY_PK_STATEMENT = "SELECT * FROM follows WHERE follower_id = '%s' AND following_id = '%s';";

    @Override
    public Optional<Follow> loadFollowByPk(UUID followerId, UUID followingId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_FOLLOW_BY_PK_STATEMENT, followerId, followingId);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                UUID resultFollowerId = (UUID) resultSet.getObject("follower_id");
                UUID resultFollowingId = (UUID) resultSet.getObject("following_id");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new Follow(
                        resultFollowerId,
                        resultFollowingId,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }
}
