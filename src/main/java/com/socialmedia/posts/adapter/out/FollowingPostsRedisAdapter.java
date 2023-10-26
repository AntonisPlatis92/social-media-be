package com.socialmedia.posts.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.socialmedia.posts.adapter.in.vms.FollowingPostsReturnVM;
import com.socialmedia.posts.application.port.out.FollowingPostsCachePort;
import com.socialmedia.utils.cache.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

public class FollowingPostsRedisAdapter implements FollowingPostsCachePort {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Integer REDIS_KEY_EXPIRY_TIME_IN_SECONDS = 5;//60 * 60 * 24;

    @Override
    public void setFollowingPostsReturnVMInRedis(UUID userId, List<FollowingPostsReturnVM> followingPostsReturnVM) {
        try {
            String valueAsString = objectMapper.writeValueAsString(followingPostsReturnVM);
            RedisUtils.setKeyValueWithTTL(userId.toString(), valueAsString, REDIS_KEY_EXPIRY_TIME_IN_SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<FollowingPostsReturnVM> getFollowingPostsReturnVMFromRedis(UUID userId) {
        String redisValue = RedisUtils.getFromRedis(userId.toString());

        if (redisValue != null) {
            try {
                return objectMapper.readValue(redisValue, new TypeReference<List<FollowingPostsReturnVM>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return null;
    }


}
