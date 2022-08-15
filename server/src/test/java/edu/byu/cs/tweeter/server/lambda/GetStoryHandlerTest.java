package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

public class GetStoryHandlerTest {

    private GetStoryHandler handler;
    StoryRequest request;
    StoryResponse response;
    User user;
    int limit;

    @BeforeEach
    void setup() {
        handler = new GetStoryHandler();
        request = null;
        response = null;
        user = new User("Josh", "Smith", "@test", "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
        limit = 10;
    }

    @Test
    void validGetStory() {
        request = new StoryRequest(user, limit, null, "myAuthToken");
        response = handler.handleRequest(request, null);

        
        assert(request.getAuthTokenString().equals("myAuthToken"));
        assert(response.isSuccess());
        assert(response.hasMorePages());
        assert(response.getStory().size() == limit);
        assert(response.getStory().get(0).getUser().getAlias().equals("@test"));

        for (int i = 0; i < 10; i++) {
            assert(response.getStory().get(i).getMessageBody().equals("Fake tweet @test @asdf" + i));
        }
    }

    @Test
    void invalidGetStory() {
        request = new StoryRequest(user, limit, null, "invalidAuthToken");

        try {
            response = handler.handleRequest(request, null);
        }
        catch (Exception e){
            assert(e.getMessage().equals("forbidden"));
        }

        assert(response == null);
        assert(!request.getAuthTokenString().equals("myAuthToken"));
    }

}
