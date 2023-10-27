package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.adapter.in.vms.OwnPostReturnVM;

import java.util.List;
import java.util.UUID;

public interface LoadOwnPostsPort {
    List<OwnPostReturnVM> loadOwnPosts(UUID userId);}
