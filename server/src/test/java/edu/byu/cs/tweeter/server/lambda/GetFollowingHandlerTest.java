package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

public class GetFollowingHandlerTest {

    private GetFollowingHandler handler;
    FollowingRequest request;
    FollowingResponse response;
    User user;
    int limit;

    @BeforeEach
    void setup() {
        handler = new GetFollowingHandler();
        request = null;
        response = null;
        user = new User("Josh", "Smith", "@test", "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
        limit = 10;
    }

    @Test
    void validGetFollowing() {
        request = new FollowingRequest(user, limit, null, "myAuthToken");
        response = handler.handleRequest(request, null);

        
        assert(request.getAuthTokenString().equals("myAuthToken"));
        assert(response.isSuccess());
        assert(response.hasMorePages());
        assert(response.getFollowees().size() == limit);

        for (int i = 0; i < 10; i++) {
            assert(response.getFollowees().get(i).getAlias().equals("@tempAlias" + i));
            assert(response.getFollowees().get(i).getFirstName().equals("fname" + i));
            assert(response.getFollowees().get(i).getLastName().equals("lname" + i));
        }
    }

    @Test
    void invalidGetFollowing() {
        request = new FollowingRequest(user, limit, null, "invalidAuthToken");

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
