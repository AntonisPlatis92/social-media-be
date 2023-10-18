package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.application.port.out.LoadCommentPort;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadCommentAdapter implements LoadCommentPort {
    private static final String LOAD_COMMENT_BY_ID_STATEMENT = "SELECT * FROM comments WHERE id = '%s';";
    private static final String LOAD_COMMENT_BY_USER_EMAIL_AND_POST_ID_STATEMENT = "SELECT * FROM comments WHERE user_email = '%s' AND post_id = '%s';";

    @Override
    public Optional<Comment> loadCommentById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_COMMENT_BY_ID_STATEMENT, id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                UUID commentId = (UUID) resultSet.getObject("id");
                String userEmail = resultSet.getString("user_email");
                UUID postId = (UUID) resultSet.getObject("post_id");
                String body = resultSet.getString("body");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new Comment(
                        commentId,
                        userEmail,
                        postId,
                        body,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public List<Comment> loadCommentByUserEmailAndPostId(String userEmail, UUID postId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_COMMENT_BY_USER_EMAIL_AND_POST_ID_STATEMENT, userEmail, postId);
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Comment> comments = new ArrayList<>();

            while (resultSet.next()) {
                UUID commentId = (UUID) resultSet.getObject("id");
                String commentUserEmail = resultSet.getString("user_email");
                UUID commentPostId = (UUID) resultSet.getObject("post_id");
                String body = resultSet.getString("body");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                comments.add(new Comment(
                        commentId,
                        commentUserEmail,
                        commentPostId,
                        body,
                        creationTime
                ));
            }
            return comments;
        });
    }
}
