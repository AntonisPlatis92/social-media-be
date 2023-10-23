package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public class LoadFollowingPostsAdapter implements LoadFollowingPostsPort {
    private static final String LOAD_FOLLOWING_POSTS_BY_USER_ID_STATEMENT = "SELECT * FROM following_posts WHERE user_id = ?;";

    @Override
    public List<FollowingPostsReturnVM> loadFollowingPosts(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<FollowingPostsReturnVM> followingPosts = new ArrayList<>();

            PreparedStatement preparedStatement = conn.prepareStatement(LOAD_FOLLOWING_POSTS_BY_USER_ID_STATEMENT);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
