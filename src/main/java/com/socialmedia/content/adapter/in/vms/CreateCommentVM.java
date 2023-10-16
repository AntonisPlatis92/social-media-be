package com.socialmedia.content.adapter.in.vms;

import java.util.UUID;

public record CreateCommentVM(UUID postId, String body) {}
