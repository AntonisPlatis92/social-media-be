package com.socialmedia.application.port.out;

import com.socialmedia.application.domain.entities.User;

public interface LoadUserPort {
    User loadUser(String email);
}
