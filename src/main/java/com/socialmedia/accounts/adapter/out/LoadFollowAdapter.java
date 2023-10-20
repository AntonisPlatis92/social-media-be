package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.LoadFollowPort;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadFollowAdapter implements LoadFollowPort {
    private static final String LOAD_FOLLOW_BY_PK_STATEMENT = "SELECT * FROM follows WHERE follower_id = '%s' AND following_id = '%s';";
    private static final String LOAD_FOLLOW_BY_FOLLOWER_ID_STATEMENT = "SELECT users.email FROM follows JOIN users ON users.id = follows.following_id WHERE follower_id = '%s';";
    private static final String LOAD_FOLLOW_BY_FOLLOWING_ID_STATEMENT = "SELECT users.email FROM follows JOIN users ON users.id = follows.follower_id WHERE following_id = '%s';";

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

    @Override
    public FollowsReturnVM loadFollowsByUserId(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<String> followers = new ArrayList<>();
            List<String> following = new ArrayList<>();


            Statement followerStatement = conn.createStatement();
            String followerQuery = String.format(LOAD_FOLLOW_BY_FOLLOWING_ID_STATEMENT, userId);
            ResultSet followerResultSet = followerStatement.executeQuery(followerQuery);

            while (followerResultSet.next()) {
                String followerEmail = followerResultSet.getString("email");
                followers.add(followerEmail);
            }

            Statement followingStatement = conn.createStatement();
            String followingQuery = String.format(LOAD_FOLLOW_BY_FOLLOWER_ID_STATEMENT, userId);
            ResultSet followingResultSet = followingStatement.executeQuery(followingQuery);

            while (followingResultSet.next()) {
                String followingEmail = followingResultSet.getString("email");
                following.add(followingEmail);
            }

            return new FollowsReturnVM(
                    followers,
                    following
            );
        });
    }
}
