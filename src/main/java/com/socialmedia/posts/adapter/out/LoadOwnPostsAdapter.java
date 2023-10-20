package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.Statement;
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
            "WHERE p.user_id = '%s' " +
            "ORDER BY p.creation_time DESC;";
    private static final String LOAD_COMMENTS_ON_OWN_POSTS_BY_POST_ID_LIMIT_100_STATEMENT =
            "SELECT post_id, comment_user_email, comment_body," +
            "comment_creation_time " +
            "FROM comments_on_own_posts " +
            "WHERE post_id = '%s' " +
            "ORDER BY comment_creation_time DESC " +
            "LIMIT 100;";

    @Override
    public List<OwnPostsReturnVM> loadOwnPosts(UUID userId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<OwnPostsReturnVM> ownPosts = new ArrayList<>();

            Statement postStatement = conn.createStatement();
            String postQuery = String.format(LOAD_OWN_POSTS_BY_USER_ID_STATEMENT, userId);
            ResultSet postResultSet = postStatement.executeQuery(postQuery);

            while (postResultSet.next()) {
                String postId = postResultSet.getString("post_id");
                String postBody = postResultSet.getString("post_body");
                Instant postCreationTime = postResultSet.getTimestamp("post_creation_time").toInstant();

                Statement commentsStatement = conn.createStatement();
                String commentsQuery = String.format(LOAD_COMMENTS_ON_OWN_POSTS_BY_POST_ID_LIMIT_100_STATEMENT, postId);
                ResultSet commentsResultSet = commentsStatement.executeQuery(commentsQuery);

                ArrayList<CommentReturnVM> commentsOnOwnPosts = new ArrayList<>();

                while (commentsResultSet.next()) {
                    String commentUserEmail = commentsResultSet.getString("comment_user_email");
                    String commentBody = commentsResultSet.getString("comment_body");
                    Instant commentCreationTime = commentsResultSet.getTimestamp("comment_creation_time").toInstant();

                    commentsOnOwnPosts.add(new CommentReturnVM(
                            postId,
                            commentUserEmail,
                            commentBody,
                            FORMATTER.format(commentCreationTime)
                    ));
                }

                ownPosts.add(new OwnPostsReturnVM(
                        postId,
                        postBody,
                        FORMATTER.format(postCreationTime),
                        commentsOnOwnPosts));
            }
            return ownPosts;
        });
    }
}
