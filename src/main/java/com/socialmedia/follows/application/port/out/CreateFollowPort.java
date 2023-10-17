package com.socialmedia.follows.application.port.out;

import com.socialmedia.follows.domain.Follow;

public interface CreateFollowPort {
    void createFollow(Follow follow);
}
