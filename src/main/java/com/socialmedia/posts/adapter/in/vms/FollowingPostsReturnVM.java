package com.socialmedia.posts.adapter.in.vms;

public record FollowingPostsReturnVM(
        String postId,
        String postUserEmail,
        String postBody,
        String postCreationTime
) {}
