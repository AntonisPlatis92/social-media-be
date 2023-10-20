package com.socialmedia.accounts.adapter.in.vms;

import java.util.List;

public record FollowsReturnVM(
        List<String> followers,
        List<String> following
) {
}
