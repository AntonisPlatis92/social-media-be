package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnAndFollowingPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public class LoadCommentsOnOwnAndFollowingPostsAdapter implements LoadCommentsOnOwnAndFollowingPostsPort {
    private static final String LOAD_COMMENTS_ON_OWN_AND_FOLLOWING_POSTS_BY_USER_ID_STATEMENT =
            "SELECT * FROM comments_on_own_and_following_posts WHERE user_id = '%s';";


    @Override
    public List<CommentReturnVM> loadCommentsOnOwnAndFollowingPosts(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<CommentReturnVM> commentsOnOwnPosts = new ArrayList<>();

            Statement statement = conn.createStatement();
            String query = String.format(LOAD_COMMENTS_ON_OWN_AND_FOLLOWING_POSTS_BY_USER_ID_STATEMENT, userId, userId);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String postId = resultSet.getString("post_id");
                String commentUserEmail = resultSet.getString("comment_user_email");
                String commentBody = resultSet.getString("comment_body");
                Instant commentCreationTime = resultSet.getTimestamp("comment_creation_time").toInstant();

                commentsOnOwnPosts.add(new CommentReturnVM(
                        postId,
                        commentUserEmail,
                        commentBody,
                        FORMATTER.format(commentCreationTime)
                ));
            }

            return commentsOnOwnPosts;
        });
    }

}
