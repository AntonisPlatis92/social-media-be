package com.socialmedia.accounts.application.port.out;

import java.time.Instant;

public interface CreateUserPort {

    void createUser(String email,
                    String hashedPassword,
                    boolean verified,
                    Long roleId,
                    Instant creationTime);
}
