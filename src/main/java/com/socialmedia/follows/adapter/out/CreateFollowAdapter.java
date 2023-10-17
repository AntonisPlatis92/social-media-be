package com.socialmedia.follows.adapter.out;

import com.socialmedia.follows.application.port.out.CreateFollowPort;
import com.socialmedia.follows.domain.Follow;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class CreateFollowAdapter implements CreateFollowPort {
    private static final String CREATE_FOLLOW_STATEMENT = "INSERT INTO follows (follower_id,following_id,creation_time) VALUES (?,?,?);";

    @Override
    public void createFollow(Follow follow) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_FOLLOW_STATEMENT);
            preparedStatement.setObject(1, follow.getFollowerId());
            preparedStatement.setObject(2, follow.getFollowingId());
            preparedStatement.setTimestamp(3, Timestamp.from(follow.getCreationTime()));
            preparedStatement.executeUpdate();
        });
    }
}
