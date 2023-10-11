package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.domain.User;

public interface LoadUserPort {
    User loadUser(String email);
}
