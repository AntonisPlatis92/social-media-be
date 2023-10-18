package com.socialmedia.posts.application.services;

import com.socialmedia.posts.application.port.in.LoadPostUseCase;
import com.socialmedia.posts.application.port.out.LoadPostPort;
import com.socialmedia.posts.domain.Post;

import java.util.List;

public class LoadPostService implements LoadPostUseCase {
    private final LoadPostPort loadPostPort;

    public LoadPostService(LoadPostPort loadPostPort) {
        this.loadPostPort = loadPostPort;
    }
    @Override
    public List<Post> loadPostsByUserEmail(String userEmail) {
        return loadPostPort.loadPostsByUserEmail(userEmail);
    }
}
