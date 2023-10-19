package com.socialmedia.posts.application.port.out;

import com.socialmedia.posts.adapter.in.vms.OwnPostsReturnVM;

import java.util.List;
import java.util.UUID;

public interface LoadOwnPostsPort {
    List<OwnPostsReturnVM> loadOwnPosts(UUID userId);}
