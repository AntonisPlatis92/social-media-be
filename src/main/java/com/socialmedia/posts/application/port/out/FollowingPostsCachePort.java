package com.socialmedia.posts.application.port.out;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;

import java.util.List;
import java.util.UUID;

public interface FollowingPostsCachePort {
    void setFollowingPostsReturnVMInRedis(UUID userId, List<FollowingPostsReturnVM> followingPostsReturnVM);
    List<FollowingPostsReturnVM> getFollowingPostsReturnVMFromRedis(UUID userId);
}
