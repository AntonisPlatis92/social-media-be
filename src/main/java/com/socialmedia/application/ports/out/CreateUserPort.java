package com.socialmedia.application.ports.out;

import java.time.Instant;

public interface CreateUserPort {

    void createUser(String email,
                    String hashedPassword,
                    boolean verified,
                    Long roleId,
                    Instant creationTime);
}
