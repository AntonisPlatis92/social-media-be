package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.adapter.in.vms.FollowsReturnVM;

import java.util.UUID;

public interface LoadFollowsUseCase {
    FollowsReturnVM loadFollowsByUserId(UUID userId);
}
