package com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;
import com.socialmedia.accounts.application.port.out.SearchUserPort;
import com.socialmedia.utils.database.DatabaseUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SearchUserAdapter implements SearchUserPort {
    private static final String SEARCH_USER_BY_EMAIL_LIKE_STATEMENT = "SELECT email FROM users WHERE email ILIKE ?;";;

    @Override
    public SearchUsersReturnVM searchUsers(String termToSearch) {
        return DatabaseUtils.doInTransactionAndReturn((conn) -> {
            List<String> userEmails = new ArrayList<>();
            PreparedStatement preparedStatement = conn.prepareStatement(SEARCH_USER_BY_EMAIL_LIKE_STATEMENT);
            preparedStatement.setString(1, "%" + termToSearch + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userEmail = resultSet.getString("email");
                userEmails.add(userEmail);
            }

            return new SearchUsersReturnVM(userEmails);
        });
    }
}
