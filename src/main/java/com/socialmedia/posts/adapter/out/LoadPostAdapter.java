package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.application.port.out.LoadPostPort;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.*;

public class LoadPostAdapter implements LoadPostPort {
    private static final String LOAD_POST_BY_ID_STATEMENT = "SELECT * FROM posts WHERE id = '%s';";
    private static final String LOAD_POST_BY_USER_ID_STATEMENT = "SELECT * FROM posts WHERE user_id = '%s';";
    private static final String LOAD_COMMENT_BY_POST_ID_STATEMENT = "SELECT * FROM comments WHERE post_id = '%s';";

    @Override
    public Optional<Post> loadPostById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement postStatement = conn.createStatement();
            String postQuery = String.format(LOAD_POST_BY_ID_STATEMENT, id);
            ResultSet postResultSet = postStatement.executeQuery(postQuery);

            if (postResultSet.next()) {
                UUID postId = (UUID) postResultSet.getObject("id");
                UUID postUserId = (UUID) postResultSet.getObject("user_id");
                String postBody = postResultSet.getString("body");
                Instant postCreationTime = postResultSet.getTimestamp("creation_time").toInstant();

                Statement commentsStatement = conn.createStatement();
                String commentsQuery = String.format(LOAD_COMMENT_BY_POST_ID_STATEMENT, postId);
                ResultSet commentsResultSet = commentsStatement.executeQuery(commentsQuery);

                ArrayList<Comment> comments = new ArrayList<>();

                while (commentsResultSet.next()) {
                    UUID commentId = (UUID) commentsResultSet.getObject("id");
                    UUID commentUserId = (UUID) commentsResultSet.getObject("user_id");
                    UUID commentPostId = (UUID) commentsResultSet.getObject("post_id");
                    String commentBody = commentsResultSet.getString("body");
                    Instant commentCreationTime = commentsResultSet.getTimestamp("creation_time").toInstant();

                    comments.add(new Comment(
                            commentId,
                            commentUserId,
                            commentPostId,
                            commentBody,
                            commentCreationTime
                    ));
                }

                return Optional.of(new Post(
                        postId,
                        postUserId,
                        postBody,
                        postCreationTime,
                        comments
                ));
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public List<Post> loadPostsByUserId(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement postStatement = conn.createStatement();
            String postQuery = String.format(LOAD_POST_BY_USER_ID_STATEMENT, userId);
            ResultSet resultSet = postStatement.executeQuery(postQuery);

            ArrayList<Post> posts = new ArrayList<>();

            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                UUID postUserId = (UUID) resultSet.getObject("user_id");
                String postBody = resultSet.getString("body");
                Instant postCreationTime = resultSet.getTimestamp("creation_time").toInstant();

                Statement commentsStatement = conn.createStatement();
                String commentsQuery = String.format(LOAD_COMMENT_BY_POST_ID_STATEMENT, postId);
                ResultSet commentsResultSet = commentsStatement.executeQuery(commentsQuery);

                ArrayList<Comment> comments = new ArrayList<>();

                while (commentsResultSet.next()) {
                    UUID commentId = (UUID) commentsResultSet.getObject("id");
                    UUID commentUserId = (UUID) commentsResultSet.getObject("user_id");
                    UUID commentPostId = (UUID) commentsResultSet.getObject("post_id");
                    String commentBody = commentsResultSet.getString("body");
                    Instant commentCreationTime = commentsResultSet.getTimestamp("creation_time").toInstant();

                    comments.add(new Comment(
                            commentId,
                            commentUserId,
                            commentPostId,
                            commentBody,
                            commentCreationTime
                    ));
                }

                posts.add(new Post(
                        postId,
                        postUserId,
                        postBody,
                        postCreationTime,
                        comments
                ));
            }
            return posts;
        });
    }

}
