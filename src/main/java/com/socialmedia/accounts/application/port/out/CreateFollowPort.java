package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.Follow;

public interface CreateFollowPort {
    void createFollow(Follow follow);
}
