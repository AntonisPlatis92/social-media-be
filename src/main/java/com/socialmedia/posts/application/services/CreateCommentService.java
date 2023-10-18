package com.socialmedia.posts.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.posts.domain.exceptions.CommentsLimitException;
import com.socialmedia.posts.domain.exceptions.PostNotFoundException;
import com.socialmedia.posts.application.port.in.CreateCommentUseCase;
import com.socialmedia.posts.application.port.out.CreateCommentPort;
import com.socialmedia.posts.application.port.out.LoadPostPort;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.commands.CreateCommentCommand;

public class CreateCommentService implements CreateCommentUseCase {
    private final LoadUserUseCase loadUserUseCase;
    private final LoadPostPort loadPostPort;
    private final CreateCommentPort createCommentPort;

    public CreateCommentService(
            LoadUserUseCase loadUserUseCase,
            LoadPostPort loadPostPort,
            CreateCommentPort createCommentPort) {
        this.loadUserUseCase = loadUserUseCase;
        this.loadPostPort = loadPostPort;
        this.createCommentPort = createCommentPort;
    }
    @Override
    public void createComment(CreateCommentCommand command) {
        User user = loadUserUseCase.loadUserByEmail(command.userEmail()).orElseThrow(() -> new UserNotFoundException("User doesn't exist."));

        Role role = user.getRole();

        Post post = loadPostPort.loadPostById(command.postId()).orElseThrow(() -> new PostNotFoundException("Post doesn't exist."));

        boolean shouldCheckCommentsLimit = role.isHasCommentsLimit();
        if (shouldCheckCommentsLimit) {
            checkCommentsLimit(post, role);
        }

        createCommentPort.createNewComment(Comment.createCommentFromCommand(command));
    }

    private void checkCommentsLimit(Post post, Role role) {
        if (post.getComments().size() >= role.getCommentsLimit()) {
            throw new CommentsLimitException(String.format("Current role is restricted to %d comments per post.", role.getCommentsLimit()));
        }
    }
}
