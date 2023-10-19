package com.socialmedia.views.utils;

import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.views.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ViewModelsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ClockConfig.utcZone());

    public static List<CommentReturnVM> mapFromCommentsToCommentsReturnVM(List<Comment> comments) {
        List<CommentReturnVM> commentsReturnVM = new ArrayList<>();

        comments.forEach(comment -> commentsReturnVM.add(new CommentReturnVM(
                comment.getPostId().toString(),
                comment.getUserId().toString(),
                comment.getBody(),
                formatter.format(comment.getCreationTime()))));
        return commentsReturnVM;
    }

    public static FollowsReturnVM mapFromUserToFollowsReturnVM(User user) {
        return new FollowsReturnVM(
                user.getFollowers().stream().map(follow -> follow.getFollowerId().toString()).toList(),
                user.getFollowing().stream().map(follow -> follow.getFollowingId().toString()).toList()
        );
    }
}
