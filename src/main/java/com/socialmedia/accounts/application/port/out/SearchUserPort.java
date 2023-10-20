package com.socialmedia.accounts.application.port.out;

import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;

import java.util.List;

public interface SearchUserPort {
    SearchUsersReturnVM searchUsers(String termToSearch);
}
