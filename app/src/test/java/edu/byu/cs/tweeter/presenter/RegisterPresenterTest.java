package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.Follow;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

class RegisterPresenterTest implements RegisterPresenter.View {

    private final User user1 = new User("Dafney", "Daffy", "test", "");
    private final User user2 = new User("Fred", "Flintstone", "");
    private final User user3 = new User("Barney", "Rubble", ""); // 2 followees
    private final User user4 = new User("Wilma", "Rubble", "");
    private final User user5 = new User("Clint", "Eastwood", ""); // 6 followees
    private final User user6 = new User("Mother", "Teresa", ""); // 7 followees
    private final User user7 = new User("Harriett", "Hansen", "");
    private final User user8 = new User("Zoe", "Zabriski", "");
    private final User user9 = new User("Albert", "Awesome", ""); // 1  followee
    private final User user10 = new User("Star", "Student", "");
    private final User user11 = new User("Bo", "Bungle", "");
    private final User user12 = new User("Susie", "Sampson", "");

    private final Follow follow1 = new Follow(user9, user5);

    private final Follow follow2 = new Follow(user3, user1);
    private final Follow follow3 = new Follow(user3, user8);

    private final Follow follow4 = new Follow(user5,  user9);
    private final Follow follow5 = new Follow(user5,  user11);
    private final Follow follow6 = new Follow(user5,  user1);
    private final Follow follow7 = new Follow(user5,  user2);
    private final Follow follow8 = new Follow(user5,  user4);
    private final Follow follow9 = new Follow(user5,  user8);

    private final Follow follow10 = new Follow(user6,  user3);
    private final Follow follow11 = new Follow(user6,  user5);
    private final Follow follow12 = new Follow(user6,  user1);
    private final Follow follow13 = new Follow(user6,  user7);
    private final Follow follow14 = new Follow(user6,  user10);
    private final Follow follow15 = new Follow(user6,  user12);
    private final Follow follow16 = new Follow(user6,  user4);


    private final List<Follow> follows = Arrays.asList(follow1, follow2, follow3, follow4, follow5, follow6,
            follow7, follow8, follow9, follow10, follow11, follow12, follow13, follow14, follow15,
            follow16);

    private RegisterPresenter presenter;


    @BeforeEach
    void setup() {
        presenter = new RegisterPresenter(this);
    }

    @Test
    void testSuccessfulRegister() {
        RegisterRequest request = new RegisterRequest("username10", "password", "f", "l", "");
        RegisterResponse response = presenter.doRegister(request);

        Assertions.assertTrue(response.registerSuccessful());
        Assertions.assertNotNull(response.getCurrentUser());
        Assertions.assertEquals("@username10", response.getCurrentUser().getAlias());
    }

    @Test
    void testUnsuccessfulLogin() {
        RegisterRequest request = new RegisterRequest("username11", "password", "f", "l", "");
        RegisterResponse response = presenter.doRegister(request);

        Assertions.assertTrue(response.registerSuccessful());
        Assertions.assertNotNull(response.getCurrentUser());
        Assertions.assertEquals("@username11", response.getCurrentUser().getAlias());

        request = new RegisterRequest("username11", "password", "f", "l", "");
        response = presenter.doRegister(request);

        Assertions.assertFalse(response.registerSuccessful());
        Assertions.assertNull(response.getCurrentUser());
    }
}
