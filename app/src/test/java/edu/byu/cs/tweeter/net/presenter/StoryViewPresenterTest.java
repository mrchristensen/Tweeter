package edu.byu.cs.tweeter.net.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.FollowGenerator;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.presenter.StoryViewPresenter;

class StoryViewPresenterTest implements StoryViewPresenter.View, RegisterPresenter.View{

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

    private ServerFacade serverFacadeSpy;
    private StoryViewPresenter storyViewPresenter;
    private RegisterPresenter presenter;


    @BeforeEach
    void setup() {
        serverFacadeSpy = Mockito.spy(new ServerFacade());
        FollowGenerator mockFollowGenerator = Mockito.mock(FollowGenerator.class);
        Mockito.when(mockFollowGenerator.generateUsersAndFollowsAndFollowers(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), (User) Mockito.any())).thenReturn(follows);
        Mockito.when(serverFacadeSpy.getFollowGenerator()).thenReturn(mockFollowGenerator);

        storyViewPresenter = new StoryViewPresenter(this);
        presenter = new RegisterPresenter(this);

    }

    @Test
    void mainSuccess() {
        RegisterRequest loginRequest = new RegisterRequest("username", "password", "f", "l", "");
        RegisterResponse loginResponse = presenter.getRegister(loginRequest);

        Assertions.assertTrue(loginResponse.registerSuccessful());
        Assertions.assertNotNull(loginResponse.getCurrentUser());
        Assertions.assertEquals("@username", loginResponse.getCurrentUser().getAlias());


        User user = storyViewPresenter.getCurrentUser();

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getAlias(), "@username");
        Assertions.assertEquals(storyViewPresenter.getClass(), StoryViewPresenter.class);
    }

    @Test
    void mainUnsuccessful() {
        RegisterRequest loginRequest = new RegisterRequest("username1", "password", "f", "l", "");
        RegisterResponse loginResponse1 = presenter.getRegister(loginRequest);
        loginRequest = new RegisterRequest("username1", "password", "f", "l", "");
        RegisterResponse loginResponse2 = presenter.getRegister(loginRequest);

        Assertions.assertFalse(loginResponse2.registerSuccessful());
        Assertions.assertNull(loginResponse2.getCurrentUser());

        User user = storyViewPresenter.getCurrentUser();

        Assertions.assertEquals(storyViewPresenter.getClass(), StoryViewPresenter.class);
        Assertions.assertNotEquals(user.getAlias(), "username1");
    }
}