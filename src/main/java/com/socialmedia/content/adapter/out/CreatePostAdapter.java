package com.socialmedia.content.adapter.out;

import com.socialmedia.content.application.port.out.CreatePostPort;
import com.socialmedia.content.domain.Post;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class CreatePostAdapter implements CreatePostPort {
    private static final String CREATE_POST_STATEMENT = "INSERT INTO posts (id,user_email,body,creation_time) VALUES (?,?,?,?);";
    @Override
    public void createNewPost(Post post) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_POST_STATEMENT);
            preparedStatement.setObject(1, post.getId());
            preparedStatement.setString(2, post.getUserEmail());
            preparedStatement.setString(3, post.getBody());
            preparedStatement.setTimestamp(4, Timestamp.from(post.getCreationTime()));
            preparedStatement.executeUpdate();
        });
    }
}
