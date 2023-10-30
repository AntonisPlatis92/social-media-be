package com.socialmedia.utils.database;

import com.socialmedia.config.PropertiesManager;
import jakarta.persistence.*;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class JpaDatabaseUtils {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PropertiesManager.getProperty("persistence.unit.name"));

    public static void doInTransaction(Consumer<EntityManager> action) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to execute transaction.", e);
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    public static <T> T doInTransactionAndReturn(Function<EntityManager, T> action) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            T result = action.apply(entityManager);
            transaction.commit();
            return result;
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to execute transaction.", e);
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }
}
