package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.application.port.out.CreatePostPort;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class CreatePostAdapter implements CreatePostPort {
    private static final String CREATE_POST_STATEMENT = "INSERT INTO posts (id,user_id,body,creation_time) VALUES (?,?,?,?);";
    @Override
    public void createNewPost(Post post) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_POST_STATEMENT);
            preparedStatement.setObject(1, post.getId());
            preparedStatement.setObject(2, post.getUserId());
            preparedStatement.setString(3, post.getBody());
            preparedStatement.setTimestamp(4, Timestamp.from(post.getCreationTime()));
            preparedStatement.executeUpdate();
        });
    }
}
