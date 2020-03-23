package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;

public class GetFollowersHandlerTest {

    private GetFollowersHandler handler;
    FollowersRequest request;
    FollowersResponse response;
    User user;
    int limit;

    @BeforeEach
    void setup() {
        handler = new GetFollowersHandler();
        request = null;
        response = null;
        user = new User("Josh", "Smith", "@test", "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
        limit = 10;
    }

    @Test
    void validGetFollowers() {
        request = new FollowersRequest(user, limit, null, "myAuthToken");
        response = handler.handleRequest(request, null);

        
        assert(request.getAuthTokenString().equals("myAuthToken"));
        assert(response.isSuccess());
        assert(response.hasMorePages());
        assert(response.getFollowers().size() == limit);

        for (int i = 0; i < 10; i++) {
            assert(response.getFollowers().get(i).getAlias().equals("@tempAlias" + i));
            assert(response.getFollowers().get(i).getFirstName().equals("fname" + i));
            assert(response.getFollowers().get(i).getLastName().equals("lname" + i));
        }
    }

    @Test
    void invalidGetFollowers() {
        request = new FollowersRequest(user, limit, null, "invalidAuthToken");

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
