package integration.com.socialmedia.accounts.adapter.out;

import com.socialmedia.accounts.adapter.in.vms.SearchUsersReturnVM;
import com.socialmedia.accounts.adapter.out.CreateUserJpaAdapter;
import com.socialmedia.accounts.adapter.out.SearchUserJpaAdapter;
import integration.com.socialmedia.config.IntegrationTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import unit.com.socialmedia.accounts.domain.UserBuilder;

@ExtendWith(IntegrationTestConfig.class)
public class SearchUserJpaAdapterIT {
    private SearchUserJpaAdapter sut;
    private CreateUserJpaAdapter createUserJpaAdapter;

    @BeforeEach
    public void setup() {
        createUserJpaAdapter = new CreateUserJpaAdapter();
        sut = new SearchUserJpaAdapter();
    }

    @Test
    public void searchUser_whenUserExists_shouldReturnUser() {
        //  Given
        String email = "test@test.com";
        String emailTerm = "test";

        // When
        createUserJpaAdapter.createUser(UserBuilder.aRandomUserBuilder().withEmail(email).build());
        SearchUsersReturnVM returnVM = sut.searchUsers(emailTerm);

        // Then
        Assertions.assertNotNull(returnVM);
        Assertions.assertEquals(1, returnVM.userEmails().size());
        Assertions.assertEquals(email, returnVM.userEmails().get(0));
    }
}
