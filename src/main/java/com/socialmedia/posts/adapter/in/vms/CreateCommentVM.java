package com.socialmedia.posts.adapter.in.vms;

import java.util.UUID;

public record CreateCommentVM(UUID postId, String body) {}
