package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

class StoryPresenterTest implements RegisterPresenter.View, StoryPresenter.View {

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
    private StoryPresenter storyPresenter;


    @BeforeEach
    void setup() {
        presenter = new RegisterPresenter(this);
        storyPresenter = new StoryPresenter(this);
    }

    @Test
    void testSuccessfulStory() {
        RegisterRequest request = new RegisterRequest("username12", "password", "f", "l", "");
        RegisterResponse response = presenter.getRegister(request);

        Assertions.assertTrue(response.registerSuccessful());
        Assertions.assertNotNull(response.getCurrentUser());
        Assertions.assertEquals("@username12", response.getCurrentUser().getAlias());

        StoryRequest storyRequest = new StoryRequest(new User("f", "l", "username12", ""), 1, null);
        StoryResponse storyResponse = storyPresenter.getStory(storyRequest);

        Assertions.assertNotNull(storyResponse);
        Assertions.assertTrue(storyResponse.getStory().size() > 0);
        Assertions.assertTrue(storyResponse.isSuccess());
        Assertions.assertTrue(storyResponse.hasMorePages());
    }

    @Test
    void testUnsuccessfulStory() {
        RegisterRequest request = new RegisterRequest("username13", "password", "f", "l", "");
        RegisterResponse response = presenter.getRegister(request);
        RegisterRequest request2 = new RegisterRequest("username13", "password", "f", "l", "");
        RegisterResponse response2 = presenter.getRegister(request);

        Assertions.assertFalse(response2.registerSuccessful());
        Assertions.assertNull(response2.getCurrentUser());

        StoryRequest storyRequest = new StoryRequest(new User("f", "l", "username13", ""), 1, null);
        StoryResponse storyResponse = storyPresenter.getStory(storyRequest);

        Assertions.assertNotNull(storyResponse);
        Assertions.assertFalse(storyResponse.getStory().size() < 0);
    }
}
