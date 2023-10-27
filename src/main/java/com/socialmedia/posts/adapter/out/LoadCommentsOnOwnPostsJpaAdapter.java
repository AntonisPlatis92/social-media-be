package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.CommentReturnVM;
import com.socialmedia.posts.application.port.out.LoadCommentsOnOwnPostsPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;

import java.util.List;
import java.util.UUID;

public class LoadCommentsOnOwnPostsJpaAdapter implements LoadCommentsOnOwnPostsPort {
    @Override
    public List<CommentReturnVM> loadCommentsOnOwnPosts(UUID userId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            return entityManager.createQuery(
                            "SELECT new com.socialmedia.posts.adapter.in.vms.CommentReturnVM(c.postId, c.commentUserEmail, c.commentBody, c.commentCreationTime)" +
                                    " FROM CommentOnOwnPostsViewJpa c WHERE c.userId = :userId ORDER BY c.commentCreationTime DESC",
                            CommentReturnVM.class)
                    .setParameter("userId", userId)
                    .setMaxResults(1000)
                    .getResultList();
        });
    }
}
