package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.out.LoadRoleAdapter;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(IntegrationTestConfig.class)
public class LoadRoleServiceIT {
    private LoadRoleService loadRoleService;

    private LoadRolePort loadRolePort;

    @BeforeEach
    public void setup() {
        loadRolePort = new LoadRoleAdapter();
        loadRoleService = new LoadRoleService(loadRolePort);
    }

    @Test
    public void createUser_whenLoadUser_shouldMatchFields() {
        //  Given
        long roleId1 = 1L;
        long roleId2 = 2L;

        // When
        Optional<Role> maybeRole1 = loadRoleService.loadRole(roleId1);
        Optional<Role> maybeRole2 = loadRoleService.loadRole(roleId2);

        // Then
        assertTrue(maybeRole1.isPresent());
        assertTrue(maybeRole2.isPresent());
        assertEquals(maybeRole1.get().getRoleName(), "FREE");
        assertEquals(maybeRole1.get().getPostCharsLimit(), 1000);
        assertEquals(maybeRole2.get().getRoleName(), "PREMIUM");
        assertEquals(maybeRole2.get().getPostCharsLimit(), 3000);
    }
}
