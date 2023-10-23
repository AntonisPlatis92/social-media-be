package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public class LoadCommentsOnOwnPostsAdapter implements LoadCommentsOnOwnPostsPort {
    private static final String LOAD_COMMENTS_ON_OWN_POSTS_BY_USER_ID_STATEMENT =
            "SELECT * " +
            "FROM comments_on_own_posts " +
            "WHERE user_id = ? " +
            "ORDER BY comment_creation_time DESC;";

    @Override
    public List<CommentReturnVM> loadCommentsOnOwnPosts(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<CommentReturnVM> commentsOnOwnPosts = new ArrayList<>();

            PreparedStatement preparedStatement = conn.prepareStatement(LOAD_COMMENTS_ON_OWN_POSTS_BY_USER_ID_STATEMENT);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("post_id");
                String commentUserEmail = resultSet.getString("comment_user_email");
                String commentBody = resultSet.getString("comment_body");
                Instant commentCreationTime = resultSet.getTimestamp("comment_creation_time").toInstant();

                commentsOnOwnPosts.add(new CommentReturnVM(
                        postId.toString(),
                        commentUserEmail,
                        commentBody,
                        FORMATTER.format(commentCreationTime)
                ));
            }

            return commentsOnOwnPosts;
        });
    }
}
