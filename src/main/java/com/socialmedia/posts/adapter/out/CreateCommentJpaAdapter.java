package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.out.mappers.PostJpaMapper;
import com.socialmedia.posts.application.port.out.CreateCommentPort;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.utils.database.JpaDatabaseUtils;

public class CreateCommentJpaAdapter implements CreateCommentPort {
    @Override
    public void createNewComment(Comment comment) {
        JpaDatabaseUtils.doInTransaction(entityManager -> {
            entityManager.persist(PostJpaMapper.mapFromCommentToCommentJpaEntity(comment));
        });
    }
}
