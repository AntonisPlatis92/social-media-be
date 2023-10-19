package com.socialmedia.views.application.port.in;

import com.socialmedia.views.adapter.in.vms.FollowsReturnVM;

import java.util.UUID;

public interface ViewFollowsUseCase {
    FollowsReturnVM viewOwnFollowersAndFollowing(UUID userId);
}
