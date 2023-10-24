package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.LoadFollowPort;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadFollowAdapter implements LoadFollowPort {
    private static final String LOAD_FOLLOW_BY_PK_STATEMENT = "SELECT * FROM follows WHERE follower_id = ? AND following_id = ?;";
    private static final String LOAD_FOLLOW_BY_FOLLOWER_ID_STATEMENT = "SELECT users.email FROM follows JOIN users ON users.id = follows.following_id WHERE follower_id = ? LIMIT 1000;";
    private static final String LOAD_FOLLOW_BY_FOLLOWING_ID_STATEMENT = "SELECT users.email FROM follows JOIN users ON users.id = follows.follower_id WHERE following_id = ? LIMIT 1000;";
    private static final String LOAD_FOLLOWING_USER_IDS_BY_USER_ID_STATEMENT = "SELECT following_id FROM follows WHERE follower_id = ?;";

    @Override
    public Optional<Follow> loadFollowByPk(UUID followerId, UUID followingId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(LOAD_FOLLOW_BY_PK_STATEMENT);
            preparedStatement.setObject(1, followerId);
            preparedStatement.setObject(2, followingId);
            ResultSet resultSet = preparedStatement.executeQuery();

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


            PreparedStatement followerPreparedStatement = conn.prepareStatement(LOAD_FOLLOW_BY_FOLLOWING_ID_STATEMENT);
            followerPreparedStatement.setObject(1, userId);
            ResultSet followerResultSet = followerPreparedStatement.executeQuery();

            while (followerResultSet.next()) {
                String followerEmail = followerResultSet.getString("email");
                followers.add(followerEmail);
            }

            PreparedStatement followingPreparedStatement = conn.prepareStatement(LOAD_FOLLOW_BY_FOLLOWER_ID_STATEMENT);
            followingPreparedStatement.setObject(1, userId);
            ResultSet followingResultSet = followingPreparedStatement.executeQuery();

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

    @Override
    public List<UUID> loadFollowingUserIdsByUserId(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<UUID> followingIds = new ArrayList<>();

            PreparedStatement followerPreparedStatement = conn.prepareStatement(LOAD_FOLLOWING_USER_IDS_BY_USER_ID_STATEMENT);
            followerPreparedStatement.setObject(1, userId);
            ResultSet followerResultSet = followerPreparedStatement.executeQuery();

            while (followerResultSet.next()) {
                UUID followingUserId = (UUID) followerResultSet.getObject("following_id");
                followingIds.add(followingUserId);
            }

            return followingIds;
        });
    }
}
