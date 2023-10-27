package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;
import com.socialmedia.accounts.application.port.out.SearchUserPort;
import com.socialmedia.utils.database.JpaDatabaseUtils;
import jakarta.persistence.Query;

import java.util.List;

public class SearchUserJpaAdapter implements SearchUserPort {
    @Override
    public SearchUsersReturnVM searchUsers(String termToSearch) {
        return JpaDatabaseUtils.doInTransactionAndReturn(entityManager -> {
            Query query = entityManager.createQuery(
                    "SELECT u.email FROM UserJpa u WHERE u.email ILIKE :term");
            query.setParameter("term", "%" + termToSearch + "%");

            @SuppressWarnings("unchecked")

            List<String> userEmails = query.getResultList();

            return new SearchUsersReturnVM(userEmails);
        });
    }
}
