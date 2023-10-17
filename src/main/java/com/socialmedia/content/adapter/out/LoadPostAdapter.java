package com.socialmedia.content.adapter.out;

import com.socialmedia.content.application.port.out.LoadPostPort;
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

    @Override
    public Optional<Post> loadPostById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_POST_BY_ID_STATEMENT, id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                String userEmail = resultSet.getString("user_email");
                String body = resultSet.getString("body");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new Post(
                        postId,
                        userEmail,
                        body,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public List<Post> loadPostsByUserEmail(String userEmail) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_POST_BY_USER_EMAIL_STATEMENT, userEmail);
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Post> posts = new ArrayList<>();

            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                String postUserEmail = resultSet.getString("user_email");
                String body = resultSet.getString("body");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                posts.add(new Post(
                        postId,
                        postUserEmail,
                        body,
                        creationTime
                ));
            }
            return posts;
        });
    }

}
