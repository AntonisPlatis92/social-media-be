package com.socialmedia.accounts.application.services;

import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;
import com.socialmedia.accounts.application.port.in.SearchUsersUseCase;
import com.socialmedia.accounts.application.port.out.SearchUserPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;

public class SearchUsersService implements SearchUsersUseCase {
    SearchUserPort searchUserPort;

    public SearchUsersService(SearchUserPort searchUserPort) {
        this.searchUserPort = searchUserPort;
    }

    @Override
    public SearchUsersReturnVM searchUsers(String searchTerm) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> searchUserPort.searchUsers(searchTerm));
    }
}
