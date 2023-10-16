package com.socialmedia.content.adapter.out;

import com.socialmedia.accounts.domain.Role;
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

import static java.util.Collections.emptyList;

public class LoadPostAdapter implements LoadPostPort {
    private static final String LOAD_POST_BY_ID_STATEMENT = "SELECT * FROM posts WHERE id = '%s';";
    private static final String LOAD_POST_BY_USER_ID_STATEMENT = "SELECT * FROM posts WHERE user_id = '%s';";

    @Override
    public Optional<Post> loadPostById(UUID id) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_POST_BY_ID_STATEMENT, id);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                UUID userId = (UUID) resultSet.getObject("user_id");
                String body = resultSet.getString("body");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                return Optional.of(new Post(
                        postId,
                        userId,
                        body,
                        creationTime
                ));
            }
            else {return Optional.empty();}
        });
    }

    @Override
    public List<Post> loadPostByUserId(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            Statement statement = conn.createStatement();
            String query = String.format(LOAD_POST_BY_USER_ID_STATEMENT, userId);
            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Post> posts = new ArrayList<>();

            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                UUID postUserId = (UUID) resultSet.getObject("user_id");
                String body = resultSet.getString("body");
                Instant creationTime = resultSet.getTimestamp("creation_time").toInstant();

                posts.add(new Post(
                        postId,
                        postUserId,
                        body,
                        creationTime
                ));
            }
            return posts;
        });
    }

}
