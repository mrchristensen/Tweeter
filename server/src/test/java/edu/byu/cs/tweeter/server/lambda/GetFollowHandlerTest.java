package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

public class GetFollowHandlerTest {

    private GetFollowHandler handler;
    FollowRequest request;
    FollowResponse response;

    @BeforeEach
    void setup() {
        handler = new GetFollowHandler();
        request = null;
        response = null;
    }

    @Test
    void validGetFollow() {
        request = new FollowRequest("@test", "@tempAlias1", "myAuthToken");
        response = handler.handleRequest(request, null);

        assert(response.getUser1().equals("@test"));
        assert(response.getUser2().equals("@tempAlias1"));
        assert(response.isFollows());
    }

    @Test
    void invalidDeleteFollow() {
        request = new FollowRequest(null, null, null);

        try {
            response = handler.handleRequest(request, null);
        }
        catch (Exception e){
            assert(e.getMessage().equals("forbidden"));
        }

        assert(response.getUser1() == null);
        assert(response.getUser2() == null);
        assert(request.getAuthTokenString() == null);
    }

}
