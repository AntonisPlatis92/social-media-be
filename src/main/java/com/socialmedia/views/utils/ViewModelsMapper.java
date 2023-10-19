package com.socialmedia.views.utils;

import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.config.ClockConfig;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.views.adapter.in.vms.CommentReturnVM;
import com.socialmedia.views.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.views.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.views.adapter.in.vms.OwnPostsReturnVM;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ViewModelsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ClockConfig.utcZone());
    public static List<FollowingPostsReturnVM> mapFromPostsToFollowingPostsReturnVM(List<Post> posts) {
        List<FollowingPostsReturnVM> postsReturnVM = new ArrayList<>();

        posts.forEach(post -> postsReturnVM.add(new FollowingPostsReturnVM(
                post.getId().toString(),
                post.getUserId(),
                post.getBody(),
                formatter.format(post.getCreationTime()))));
        return postsReturnVM;
    }

    public static List<OwnPostsReturnVM> mapFromPostsToOwnPostsReturnVM(List<Post> posts) {
        List<OwnPostsReturnVM> postsReturnVM = new ArrayList<>();

        posts.forEach(post ->
            postsReturnVM.add(new OwnPostsReturnVM(
                    post.getId().toString(),
                    post.getBody(),
                    formatter.format(post.getCreationTime()),
                    mapFromCommentsToCommentsReturnVM(post.getComments())))
        );
        return postsReturnVM;
    }

    public static List<CommentReturnVM> mapFromCommentsToCommentsReturnVM(List<Comment> comments) {
        List<CommentReturnVM> commentsReturnVM = new ArrayList<>();

        comments.forEach(comment -> commentsReturnVM.add(new CommentReturnVM(
                comment.getPostId().toString(),
                comment.getUserId(),
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
