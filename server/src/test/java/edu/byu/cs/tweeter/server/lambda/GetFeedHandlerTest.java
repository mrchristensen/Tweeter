package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;

public class GetFeedHandlerTest {

    private GetFeedHandler handler;
    FeedRequest request;
    FeedResponse response;
    User user;
    int limit;

    @BeforeEach
    void setup() {
        handler = new GetFeedHandler();
        request = null;
        response = null;
        user = new User("Josh", "Smith", "@test", "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
        limit = 10;
    }

    @Test
    void validGetFeed() {
        request = new FeedRequest(user, limit, null, "myAuthToken");
        response = handler.handleRequest(request, null);

        
        assert(request.getAuthTokenString().equals("myAuthToken"));
        assert(response.isSuccess());
        assert(response.hasMorePages());
        assert(response.getStatuses().size() == limit);

        for (int i = 0; i < 10; i++) {
            assert(response.getStatuses().get(i).getUser().getAlias().equals("@tempAlias1"));
            assert(response.getStatuses().get(i).getMessageBody().equals("Fake tweet @test @asdf" + i));
        }
    }

    @Test
    void invalidGetFeed() {
        request = new FeedRequest(user, limit, null, "invalidAuthToken");

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
