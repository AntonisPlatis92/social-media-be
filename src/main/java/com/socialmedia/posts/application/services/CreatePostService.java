package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Follow;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.in.FollowingPostsCacheUseCase;
import com.socialmedia.posts.domain.exceptions.PostCharsLimitException;
import com.socialmedia.posts.application.port.in.CreatePostUseCase;
import com.socialmedia.posts.application.port.out.CreatePostPort;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.commands.CreatePostCommand;

import java.util.List;
import java.util.UUID;

import static com.socialmedia.posts.application.services.FollowingPostsCacheService.FOLLOWING_USERS_THRESHOLD;

public class CreatePostService implements CreatePostUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final CreatePostPort createPostPort;
    private final FollowingPostsCacheUseCase followingPostsCacheUseCase;

    public CreatePostService(LoadUserUseCase loadUserUseCase, CreatePostPort createPostPort, FollowingPostsCacheUseCase followingPostsCacheUseCase) {
        this.loadUserUseCase = loadUserUseCase;
        this.createPostPort = createPostPort;
        this.followingPostsCacheUseCase = followingPostsCacheUseCase;
    }
    @Override
    public void createPost(CreatePostCommand command) {
        User user = loadUserUseCase.loadUserById(command.userId()).orElseThrow(() -> new UserNotFoundException("User doesn't exist."));

        Role role = user.getRole();

        boolean shouldCheckPostCharsLimit = role.isHasPostCharsLimit();
        if (shouldCheckPostCharsLimit) {
            checkPostCharsLimit(command.body(), role);
        }

        Post newPost = Post.createPostFromCommand(command);
        createPostPort.createNewPost(newPost);

        FollowingPostsReturnVM followingPostsReturnVM = FollowingPostsReturnVM.createFollowingPostsReturnVmFromPost(newPost);
        List<UUID> followerIds = user.getFollowers().stream()
                .map(Follow::getFollowerId)
                .toList();
        followerIds.forEach(followerId -> {
            User followerUser = loadUserUseCase.loadUserById(followerId).get();
            if (followerUser.getFollowing().size() > FOLLOWING_USERS_THRESHOLD) {
                followingPostsCacheUseCase.addPostForUserInFollowingPostsMemory(followerUser, followingPostsReturnVM);
            }
        });
    }

    private void checkPostCharsLimit(String postBody, Role role) {
        if (postBody.length() > role.getPostCharsLimit()) {
            throw new PostCharsLimitException(String.format("Posts have a limit of %d characters for current role.", role.getPostCharsLimit()));
        }
    }
}
