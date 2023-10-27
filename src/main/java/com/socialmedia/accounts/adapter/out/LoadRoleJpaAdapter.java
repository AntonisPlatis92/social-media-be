package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.out.jpa.RoleJpa;
import com.socialmedia.accounts.adapter.out.mappers.JpaMapper;
import com.socialmedia.accounts.application.port.out.LoadRolePort;
import com.socialmedia.accounts.domain.Role;
import com.socialmedia.utils.database.JpaDatabaseUtils;

import java.util.Optional;

public class LoadRoleJpaAdapter implements LoadRolePort {
    @Override
    public Optional<Role> loadRoleById(Long roleId) {
        Optional<RoleJpa> maybeRoleJpa = JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            return Optional.ofNullable(entityManager.find(RoleJpa.class, roleId));
        });
        return maybeRoleJpa.map(JpaMapper::mapFromRoleJpaToRoleEntity);
    }
}
