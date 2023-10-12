package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.User;

public interface CreateUserPort {

    void createUser(User user);
}
