package com.socialmedia.content.adapter.out;

import com.socialmedia.content.application.port.out.LoadPostPort;
import com.socialmedia.content.domain.Comment;
import com.socialmedia.content.domain.Post;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadPostAdapter implements LoadPostPort {
    private static final String LOAD_POST_BY_ID_STATEMENT = "SELECT * FROM posts WHERE id = '%s';";
    private static final String LOAD_POST_BY_USER_EMAIL_STATEMENT = "SELECT * FROM posts WHERE user_email = '%s';";
    private static final String LOAD_COMMENT_BY_POST_ID_STATEMENT = "SELECT * FROM comments WHERE post_id = '%s';";


    @Override
    public Optional<Post> loadPostById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement postStatement = conn.createStatement();
            String postQuery = String.format(LOAD_POST_BY_ID_STATEMENT, id);
            ResultSet postResultSet = postStatement.executeQuery(postQuery);

            if (postResultSet.next()) {
                UUID postId = (UUID) postResultSet.getObject("id");
                String postUserEmail = postResultSet.getString("user_email");
                String postBody = postResultSet.getString("body");
                Instant postCreationTime = postResultSet.getTimestamp("creation_time").toInstant();

                Statement commentsStatement = conn.createStatement();
                String commentsQuery = String.format(LOAD_COMMENT_BY_POST_ID_STATEMENT, postId);
                ResultSet commentsResultSet = commentsStatement.executeQuery(commentsQuery);

                ArrayList<Comment> comments = new ArrayList<>();

                while (commentsResultSet.next()) {
                    UUID commentId = (UUID) commentsResultSet.getObject("id");
                    String commentUserEmail = commentsResultSet.getString("user_email");
                    UUID commentPostId = (UUID) commentsResultSet.getObject("post_id");
                    String commentBody = commentsResultSet.getString("body");
                    Instant commentCreationTime = commentsResultSet.getTimestamp("creation_time").toInstant();

                    comments.add(new Comment(
                            commentId,
                            commentUserEmail,
                            commentPostId,
                            commentBody,
                            commentCreationTime
                    ));
                }

                return Optional.of(new Post(
                        postId,
                        postUserEmail,
                        postBody,
                        postCreationTime,
                        comments
                ));
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public List<Post> loadPostsByUserEmail(String userEmail) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement postStatement = conn.createStatement();
            String postQuery = String.format(LOAD_POST_BY_USER_EMAIL_STATEMENT, userEmail);
            ResultSet resultSet = postStatement.executeQuery(postQuery);

            ArrayList<Post> posts = new ArrayList<>();

            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                String postUserEmail = resultSet.getString("user_email");
                String postBody = resultSet.getString("body");
                Instant postCreationTime = resultSet.getTimestamp("creation_time").toInstant();

                Statement commentsStatement = conn.createStatement();
                String commentsQuery = String.format(LOAD_COMMENT_BY_POST_ID_STATEMENT, postId);
                ResultSet commentsResultSet = commentsStatement.executeQuery(commentsQuery);

                ArrayList<Comment> comments = new ArrayList<>();

                while (commentsResultSet.next()) {
                    UUID commentId = (UUID) commentsResultSet.getObject("id");
                    String commentUserEmail = commentsResultSet.getString("user_email");
                    UUID commentPostId = (UUID) commentsResultSet.getObject("post_id");
                    String commentBody = commentsResultSet.getString("body");
                    Instant commentCreationTime = commentsResultSet.getTimestamp("creation_time").toInstant();

                    comments.add(new Comment(
                            commentId,
                            commentUserEmail,
                            commentPostId,
                            commentBody,
                            commentCreationTime
                    ));
                }

                posts.add(new Post(
                        postId,
                        postUserEmail,
                        postBody,
                        postCreationTime,
                        comments
                ));
            }
            return posts;
        });
    }

}
