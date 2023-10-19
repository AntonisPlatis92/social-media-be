package com.socialmedia.views.application.services;

import com.socialmedia.accounts.application.port.in.LoadUserUseCase;
import com.socialmedia.accounts.domain.User;
import com.socialmedia.accounts.domain.exceptions.UserNotFoundException;
import com.socialmedia.views.adapter.in.vms.FollowsReturnVM;
import com.socialmedia.views.application.port.in.ViewFollowsUseCase;
import com.socialmedia.views.utils.ViewModelsMapper;

import java.util.UUID;

public class ViewFollowsService implements ViewFollowsUseCase {
    private final LoadUserUseCase loadUserUseCase;

    public ViewFollowsService(LoadUserUseCase loadUserUseCase) {
        this.loadUserUseCase = loadUserUseCase;
    }

    @Override
    public FollowsReturnVM viewOwnFollowersAndFollowing(UUID userId) {
        User user = loadUserUseCase.loadUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User %s not found.", userId)));

        return ViewModelsMapper.mapFromUserToFollowsReturnVM(user);
    }
}
