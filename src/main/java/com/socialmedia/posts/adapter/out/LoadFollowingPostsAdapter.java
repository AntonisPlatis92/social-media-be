package com.socialmedia.posts.adapter.out;

import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoadFollowingPostsAdapter implements LoadFollowingPostsPort {
    private static final String LOAD_FOLLOWING_POSTS_BY_USER_ID_STATEMENT = "SELECT * FROM following_posts WHERE user_id = '%s';";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ClockConfig.utcZone());


    @Override
    public List<FollowingPostsReturnVM> loadFollowingPosts(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<FollowingPostsReturnVM> followingPosts = new ArrayList<>();

            Statement statement = conn.createStatement();
            String query = String.format(LOAD_FOLLOWING_POSTS_BY_USER_ID_STATEMENT, userId);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("post_id");
                String post_user_email = resultSet.getString("post_user_email");
                String postBody = resultSet.getString("post_body");
                Instant postCreationTime = resultSet.getTimestamp("post_creation_time").toInstant();

                followingPosts.add(new FollowingPostsReturnVM(
                        postId.toString(),
                        post_user_email,
                        postBody,
                        FORMATTER.format(postCreationTime)));
            }
            return followingPosts;
        });
    }
}
