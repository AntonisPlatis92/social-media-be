package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.out.jpa.CommentJpa;
import com.socialmedia.posts.adapter.out.jpa.PostJpa;
import com.socialmedia.posts.adapter.out.mappers.PostJpaMapper;
import com.socialmedia.posts.application.port.out.LoadPostPort;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoadPostJpaAdapter implements LoadPostPort {
    @Override
    public Optional<Post> loadPostById(UUID id) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
             Optional<PostJpa> maybePostJpa = Optional.ofNullable(entityManager.find(PostJpa.class, id));
            if (maybePostJpa.isEmpty()) {return Optional.empty();}
            PostJpa postJpa = maybePostJpa.get();

            TypedQuery<CommentJpa> commentsQuery = entityManager.createQuery(
                    "SELECT c FROM CommentJpa c WHERE c.postId = :postId", CommentJpa.class);
            commentsQuery.setParameter("postId", postJpa.getId());
            List<CommentJpa> commentsEntities = commentsQuery.getResultList();

            return Optional.of(PostJpaMapper.mapFromJpaEntitiesToPost(postJpa, commentsEntities));
        });
    }

    @Override
    public List<Post> loadPostsByUserId(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            List<Post> posts = new ArrayList<>();
            TypedQuery<PostJpa> postsQuery = entityManager.createQuery(
                    "SELECT p FROM PostJpa p WHERE p.userId = :userId", PostJpa.class);
            postsQuery.setParameter("userId", userId);
            List<PostJpa> postsEntities = postsQuery.getResultList();

            postsEntities.forEach(postJpa -> {
                TypedQuery<CommentJpa> commentsQuery = entityManager.createQuery(
                        "SELECT c FROM CommentJpa c WHERE c.postId = :postId", CommentJpa.class);
                commentsQuery.setParameter("postId", postJpa.getId());
                List<CommentJpa> commentsEntities = commentsQuery.getResultList();

                posts.add(PostJpaMapper.mapFromJpaEntitiesToPost(postJpa, commentsEntities));
            });

            return posts;
        });
    }
}
