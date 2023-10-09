package com.socialmedia.repositories;

import java.util.List;

public interface Repository<T, Id> {
    T findById(Id id);
    List<T> findAll();
    T save(T entity);
}
