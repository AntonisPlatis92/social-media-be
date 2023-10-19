package com.socialmedia.views.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.application.port.in.LoadPostUseCase;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.views.adapter.in.vms.CommentReturnVM;
import com.socialmedia.views.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.views.adapter.in.vms.OwnPostsReturnVM;
import com.socialmedia.views.application.port.in.ViewPostsUseCase;
import com.socialmedia.views.utils.ViewModelsMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ViewPostsService implements ViewPostsUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final LoadPostUseCase loadPostUseCase;

    public ViewPostsService(LoadUserUseCase loadUserUseCase, LoadPostUseCase loadPostUseCase) {
        this.loadUserUseCase = loadUserUseCase;
        this.loadPostUseCase = loadPostUseCase;
    }

    @Override
    public List<FollowingPostsReturnVM> viewFollowingPostsInDescendingOrder(UUID userId) {
        User user = loadUserUseCase.loadUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", userId)));
        List<Post> posts = new ArrayList<>();

        user.getFollowing().forEach(
                follow -> {
                    User userFollowing = loadUserUseCase.loadUserById(follow.getFollowingId()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", follow.getFollowingId())));
                    List<Post> userFollowingPost = loadPostUseCase.loadPostsByUserId(userFollowing.getUserId());
                    posts.addAll(userFollowingPost);
                }
        );
        posts.sort(Comparator.comparing(Post::getCreationTime).reversed());

        return ViewModelsMapper.mapFromPostsToFollowingPostsReturnVM(posts);
    }

    @Override
    public List<OwnPostsReturnVM> viewOwnPostsLimitedToHundredComments(UUID userId) {
        List<Post> posts = loadPostUseCase.loadPostsByUserId(userId);

        posts.sort(Comparator.comparing(Post::getCreationTime).reversed());
        posts.forEach(post -> {
            List<Comment> sortedComments = post.getComments().stream()
                    .sorted(Comparator.comparing(Comment::getCreationTime).reversed())
                    .limit(100).toList();
            post.setComments(sortedComments);
        });

        return ViewModelsMapper.mapFromPostsToOwnPostsReturnVM(posts);
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
