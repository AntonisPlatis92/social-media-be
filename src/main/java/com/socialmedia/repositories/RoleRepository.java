package com.socialmedia.repositories;

import com.socialmedia.entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class RoleRepository implements Repository<Role, Long>{
    private final EntityManager entityManager;

    public RoleRepository() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public Role save(Role entity) {
        // Implement when needed
        return null;
    }
}
