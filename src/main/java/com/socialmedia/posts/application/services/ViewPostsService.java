package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.application.port.in.LoadPostUseCase;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.posts.application.port.in.ViewPostsUseCase;
import com.socialmedia.views.utils.ViewModelsMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ViewPostsService implements ViewPostsUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final LoadPostUseCase loadPostUseCase;
    private final LoadFollowingPostsPort loadFollowingPostsPort;
    private final LoadOwnPostsPort loadOwnPostsPort;

    public ViewPostsService(LoadUserUseCase loadUserUseCase, LoadPostUseCase loadPostUseCase, LoadFollowingPostsPort loadFollowingPostsPort, LoadOwnPostsPort loadOwnPostsPort) {
        this.loadUserUseCase = loadUserUseCase;
        this.loadPostUseCase = loadPostUseCase;
        this.loadFollowingPostsPort = loadFollowingPostsPort;
        this.loadOwnPostsPort = loadOwnPostsPort;
    }

    @Override
    public List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId) {

        return loadFollowingPostsPort.loadFollowingPosts(userId);
    }

    @Override
    public List<OwnPostsReturnVM> viewOwnPostsLimitedToHundredComments(UUID userId) {

        return loadOwnPostsPort.loadOwnPosts(userId);
    }

    @Override
    public List<CommentReturnVM> viewCommentsOnOwnPosts(UUID userId) {
        List<Post> posts = loadPostUseCase.loadPostsByUserId(userId);

        List<Comment> commentsOnOwnPosts = new ArrayList<>();

        posts.sort(Comparator.comparing(Post::getCreationTime).reversed());
        posts.forEach(post -> {
            List<Comment> sortedComments = post.getComments().stream()
                    .sorted(Comparator.comparing(Comment::getCreationTime).reversed())
                    .toList();
            commentsOnOwnPosts.addAll(sortedComments);
        });

        return ViewModelsMapper.mapFromCommentsToCommentsReturnVM(commentsOnOwnPosts);
    }

    @Override
    public List<CommentReturnVM> viewCommentsOnOwnAndFollowingPosts(UUID userId) {
        User user = loadUserUseCase.loadUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", userId)));

        List<Comment> comments = new ArrayList<>();

        List<Post> ownPosts = loadPostUseCase.loadPostsByUserId(userId);
        ownPosts.forEach(post -> comments.addAll(post.getComments()));

        user.getFollowing().forEach(
                follow -> {
                    User userFollowing = loadUserUseCase.loadUserById(follow.getFollowingId()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", follow.getFollowingId())));
                    List<Post> followingPosts = loadPostUseCase.loadPostsByUserId(userFollowing.getUserId());
                    followingPosts.forEach(post -> comments.addAll(post.getComments()));
                });

        comments.sort(Comparator.comparing(Comment::getCreationTime).reversed());

        return ViewModelsMapper.mapFromCommentsToCommentsReturnVM(comments);
    }
}
