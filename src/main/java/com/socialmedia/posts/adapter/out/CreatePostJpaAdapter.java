package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.out.mappers.PostJpaMapper;
import com.socialmedia.posts.application.port.out.CreatePostPort;
import com.socialmedia.posts.domain.Post;
import com.socialmedia.utils.database.JpaDatabaseUtils;

public class CreatePostJpaAdapter implements CreatePostPort {
    @Override
    public void createNewPost(Post post) {
        JpaDatabaseUtils.doInTransaction(entityManager -> {
            entityManager.persist(PostJpaMapper.mapFromPostToPostJpaEntity(post));
        });
    }
}
