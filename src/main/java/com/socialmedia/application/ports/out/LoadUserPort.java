package com.socialmedia.application.ports.out;

import com.socialmedia.application.domain.entities.User;

public interface LoadUserPort {
    User loadUser(String email);
}
