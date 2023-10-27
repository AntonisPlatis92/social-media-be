package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostReturnVM;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public class LoadOwnPostsAdapter implements LoadOwnPostsPort {
    private static final String LOAD_OWN_POSTS_BY_USER_ID_STATEMENT =
            "SELECT p.id post_id, p.body post_body," +
            "p.creation_time post_creation_time " +
            "FROM posts p " +
            "WHERE p.user_id = ? " +
            "ORDER BY p.creation_time DESC " +
            "LIMIT 1000;";
    private static final String LOAD_COMMENTS_ON_OWN_POSTS_BY_POST_ID_LIMIT_100_STATEMENT =
            "SELECT post_id, comment_user_email, comment_body, " +
            "comment_creation_time " +
            "FROM comments_on_own_posts " +
            "WHERE post_id = ? " +
            "ORDER BY comment_creation_time DESC " +
            "LIMIT 100;";

    @Override
    public List<OwnPostReturnVM> loadOwnPosts(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<OwnPostReturnVM> ownPosts = new ArrayList<>();

            PreparedStatement postPreparedStatement = conn.prepareStatement(LOAD_OWN_POSTS_BY_USER_ID_STATEMENT);
            postPreparedStatement.setObject(1, userId);
            ResultSet postResultSet = postPreparedStatement.executeQuery();

            while (postResultSet.next()) {
                UUID postId = (UUID) postResultSet.getObject("post_id");
                String postBody = postResultSet.getString("post_body");
                Instant postCreationTime = postResultSet.getTimestamp("post_creation_time").toInstant();

                PreparedStatement commentsPreparedStatement = conn.prepareStatement(LOAD_COMMENTS_ON_OWN_POSTS_BY_POST_ID_LIMIT_100_STATEMENT);
                commentsPreparedStatement.setObject(1, postId);
                ResultSet commentsResultSet = commentsPreparedStatement.executeQuery();

                ArrayList<CommentReturnVM> commentsOnOwnPosts = new ArrayList<>();

                while (commentsResultSet.next()) {
                    String commentUserEmail = commentsResultSet.getString("comment_user_email");
                    String commentBody = commentsResultSet.getString("comment_body");
                    Instant commentCreationTime = commentsResultSet.getTimestamp("comment_creation_time").toInstant();

                    commentsOnOwnPosts.add(new CommentReturnVM(
                            postId.toString(),
                            commentUserEmail,
                            commentBody,
                            FORMATTER.format(commentCreationTime)
                    ));
                }

                ownPosts.add(new OwnPostReturnVM(
                        postId.toString(),
                        postBody,
                        FORMATTER.format(postCreationTime),
                        commentsOnOwnPosts));
            }
            return ownPosts;
        });
    }
}
