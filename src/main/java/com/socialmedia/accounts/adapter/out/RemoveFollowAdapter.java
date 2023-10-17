package com.socialmedia.accounts.adapter.out;

import com.socialmedia.utils.database.DatabaseUtils;
import com.socialmedia.accounts.application.port.out.RemoveFollowPort;

import java.sql.PreparedStatement;
import java.util.UUID;

public class RemoveFollowAdapter implements RemoveFollowPort {
    private static final String REMOVE_FOLLOW_STATEMENT = "DELETE FROM follows WHERE follower_id = ? AND following_id = ?;";

    @Override
    public void removeFollow(UUID followerId, UUID followingId) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_FOLLOW_STATEMENT);
            preparedStatement.setObject(1, followerId);
            preparedStatement.setObject(2, followingId);
            preparedStatement.executeUpdate();
        });
    }
}
