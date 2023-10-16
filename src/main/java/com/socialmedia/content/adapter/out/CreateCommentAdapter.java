package com.socialmedia.content.adapter.out;

import com.socialmedia.content.application.port.out.CreateCommentPort;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class CreateCommentAdapter implements CreateCommentPort {
    private static final String CREATE_COMMENT_STATEMENT = "INSERT INTO comments (id,user_id,post_id,body,creation_time) VALUES (?,?,?,?,?);";

    @Override
    public void createNewComment(Comment comment) {
        DatabaseUtils.doInTransaction((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(CREATE_COMMENT_STATEMENT);
            preparedStatement.setObject(1, comment.getId());
            preparedStatement.setObject(2, comment.getUserId());
            preparedStatement.setObject(3, comment.getPostId());
            preparedStatement.setString(4, comment.getBody());
            preparedStatement.setTimestamp(5, Timestamp.from(comment.getCreationTime()));
            preparedStatement.executeUpdate();
        });
    }
}
