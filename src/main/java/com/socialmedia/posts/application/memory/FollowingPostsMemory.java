package com.socialmedia.posts.application.memory;

import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;

import java.time.LocalDateTime;
import java.util.*;

import static com.socialmedia.utils.formatters.DateFormatter.FORMATTER;

public class FollowingPostsMemory {
    private final Map<UUID, List<FollowingPostsReturnVM>> followingPostsMemory = new HashMap<>();

    public List<FollowingPostsReturnVM> getPostsForUser(UUID userId) {
        return followingPostsMemory.getOrDefault(userId, new ArrayList<>());
    }

    public void addPostForUser(UUID userId, List<FollowingPostsReturnVM> followingPosts) {

        if (followingPostsMemory.containsKey(userId)) {
            List<FollowingPostsReturnVM> userPosts = followingPostsMemory.get(userId);
            userPosts.addAll(followingPosts);
            sortListByCreationTimeDesc(userPosts);
        } else {
            if (followingPosts.size() > 1) {
                sortListByCreationTimeDesc(followingPosts);
            }

            followingPostsMemory.put(userId, followingPosts);
        }
    }

    private void sortListByCreationTimeDesc(List<FollowingPostsReturnVM> list) {
        list.sort(Comparator.comparing(element -> {
            return LocalDateTime.parse(element.postCreationTime(), FORMATTER);
        }, Comparator.reverseOrder()));
    }
}
