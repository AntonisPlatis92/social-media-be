package com.socialmedia.posts.adapter.out.mappers;

import com.socialmedia.posts.adapter.out.jpa.CommentJpa;
import com.socialmedia.posts.adapter.out.jpa.PostJpa;
import com.socialmedia.posts.domain.Comment;
import com.socialmedia.posts.domain.Post;

import java.util.List;

public class PostJpaMapper {
    public static PostJpa mapFromPostToPostJpaEntity(Post post) {
        return new PostJpa(
                post.getId(),
                post.getUserId(),
                post.getBody(),
                post.getCreationTime()
        );
    }

    public static CommentJpa mapFromCommentToCommentJpaEntity(Comment comment) {
        return new CommentJpa(
                comment.getId(),
                comment.getPostId(),
                comment.getUserId(),
                comment.getBody(),
                comment.getCreationTime()
        );
    }

    public static Post mapFromJpaEntitiesToPost(PostJpa postJpa, List<CommentJpa> commentsJpa) {
        return new Post(
                postJpa.getId(),
                postJpa.getUserId(),
                postJpa.getBody(),
                postJpa.getCreationTime(),
                commentsJpa.stream()
                        .map(commentJpa -> new Comment(
                                commentJpa.getId(),
                                commentJpa.getUserId(),
                                commentJpa.getPostId(),
                                commentJpa.getBody(),
                                commentJpa.getCreationTime()
                        ))
                        .toList()
        );
    }
}
