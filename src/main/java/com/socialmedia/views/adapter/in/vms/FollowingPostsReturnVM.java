package com.socialmedia.views.adapter.in.vms;

import java.util.UUID;

public record FollowingPostsReturnVM(
        String postId,
        UUID postUserId,
        String postBody,
        String postCreationTime
) {}
