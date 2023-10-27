package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.OwnPostReturnVM;
import com.socialmedia.posts.adapter.out.jpa.CommentOnOwnPostsViewJpa;
import com.socialmedia.posts.adapter.out.jpa.PostJpa;
import com.socialmedia.posts.adapter.out.mappers.PostJpaMapper;
import com.socialmedia.posts.application.port.out.LoadOwnPostsPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoadOwnPostsJpaAdapter implements LoadOwnPostsPort {
    @Override
    public List<OwnPostReturnVM> loadOwnPosts(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            List<OwnPostReturnVM> ownPostReturnVM = new ArrayList<>();

            TypedQuery<PostJpa> postsQuery = entityManager.createQuery(
                    "SELECT p FROM PostJpa p WHERE p.userId = :userId ORDER BY p.creationTime DESC", PostJpa.class);
            postsQuery.setParameter("userId", userId);
            List<PostJpa> postsEntities = postsQuery.getResultList();

            postsEntities.forEach(postJpa -> {
                List<CommentOnOwnPostsViewJpa> commentsOnOwnPostsJpa = entityManager.createQuery(
                        "SELECT new com.socialmedia.posts.adapter.out.jpa.CommentOnOwnPostsViewJpa" +
                                "(c.userId, c.postId, c.commentUserEmail, c.commentBody, c.commentCreationTime) " +
                                "FROM CommentOnOwnPostsViewJpa c " +
                                "WHERE c.postId = :postId ORDER BY c.commentCreationTime DESC", CommentOnOwnPostsViewJpa.class)
                                .setParameter("postId", postJpa.getId())
                                .setMaxResults(100)
                                .getResultList();

                ownPostReturnVM.add(PostJpaMapper.mapFromJpaEntitiesToOwnPostReturnVM(postJpa, commentsOnOwnPostsJpa));
            });

            return ownPostReturnVM;
        });

    }
}
