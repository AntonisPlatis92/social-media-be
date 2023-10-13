package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.utils.database.DatabaseUtils;

import java.util.Optional;

public class LoadRoleService {
    private final LoadRolePort loadRolePort;

    public LoadRoleService(LoadRolePort loadRolePort) {
        this.loadRolePort = loadRolePort;
    }

    public Optional<Role> loadRole(Long roleId) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> loadRolePort.loadRoleById(roleId));
    }
}
