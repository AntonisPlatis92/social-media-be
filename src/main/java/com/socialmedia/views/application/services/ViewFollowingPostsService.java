package com.socialmedia.views.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.application.port.in.LoadPostUseCase;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.views.application.port.in.ViewFollowingPostsUseCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ViewFollowingPostsService implements ViewFollowingPostsUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final LoadPostUseCase loadPostUseCase;

    public ViewFollowingPostsService(LoadUserUseCase loadUserUseCase, LoadPostUseCase loadPostUseCase) {
        this.loadUserUseCase = loadUserUseCase;
        this.loadPostUseCase = loadPostUseCase;
    }

    @Override
    public List<Post> viewFollowingPostsInDescendingOrder(String userEmail) {
        User user = loadUserUseCase.loadUserByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", userEmail)));
        List<Post> posts = new ArrayList<>();

        user.getFollowing().forEach(
                follow -> {
                    User userFollowing = loadUserUseCase.loadUserById(follow.getFollowingId()).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", follow.getFollowingId())));
                    List<Post> userFollowingPost = loadPostUseCase.loadPostsByUserEmail(userFollowing.getEmail());
                    posts.addAll(userFollowingPost);
                }
        );
        posts.sort(Comparator.comparing(Post::getCreationTime).reversed());

        return posts;
    }
}
