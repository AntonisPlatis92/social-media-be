package com.socialmedia.accounts.application.port.in;

import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;

public interface SearchUsersUseCase {
    SearchUsersReturnVM searchUsers(String searchTerm);
}
