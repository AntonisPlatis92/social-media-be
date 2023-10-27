package com.socialmedia.posts.adapter.out;

import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.out.LoadFollowingPostsPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;

import java.util.List;
import java.util.UUID;

public class LoadFollowingPostsJpaAdapter implements LoadFollowingPostsPort {
    @Override
    public List<FollowingPostsReturnVM> loadFollowingPostsByFollowingUserIds(List<UUID> followingUserId) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            return entityManager.createQuery(
                    "SELECT new com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM(p.id, p.userId, p.body, p.creationTime)" +
                            " FROM PostJpa p WHERE p.userId IN :followingUserIds ORDER BY p.creationTime DESC", FollowingPostsReturnVM.class)
                    .setParameter("followingUserIds", followingUserId)
                    .setMaxResults(1000)
                    .getResultList();
        });
    }
}
