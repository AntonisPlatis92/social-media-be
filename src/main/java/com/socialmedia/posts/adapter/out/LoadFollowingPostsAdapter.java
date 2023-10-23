package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public class LoadFollowingPostsAdapter implements LoadFollowingPostsPort {

    @Override
    public List<FollowingPostsReturnVM> loadFollowingPostsByFollowingUserIds(List<UUID> userIds) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<FollowingPostsReturnVM> followingPostsReturnVM = new ArrayList<>();

            String uuidPlaceholders = String.join(",", Collections.nCopies(userIds.size(), "?"));
            String sqlQuery = "SELECT * FROM posts WHERE user_id IN (" + uuidPlaceholders + ");";

            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            int parameterIndex = 1;
             {preparedStatement.setString(parameterIndex, "1");}
            for (UUID userId : userIds) {
                preparedStatement.setObject(parameterIndex++, userId);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID postId = (UUID) resultSet.getObject("id");
                UUID userId = (UUID) resultSet.getObject("user_id");
                String postBody = resultSet.getString("body");
                Instant postCreationTime = resultSet.getTimestamp("creation_time").toInstant();

                followingPostsReturnVM.add(new FollowingPostsReturnVM(
                        postId.toString(),
                        userId.toString(),
                        postBody,
                        FORMATTER.format(postCreationTime)
                ));
            }

            return followingPostsReturnVM;
        });
    }
}
