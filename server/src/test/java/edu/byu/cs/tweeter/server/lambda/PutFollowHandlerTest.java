package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.server.lambda.DeleteFollowHandler;
import edu.byu.cs.tweeter.server.lambda.PutFollowHandler;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

public class PutFollowHandlerTest {

    private PutFollowHandler handler;
    FollowRequest request;
    FollowResponse response;

    @BeforeEach
    void setup() {
        handler = new PutFollowHandler();
        request = null;
        response = null;
    }

    @Test
    void validPutFollow() {
        request = new FollowRequest("@test", "@tempAlias1", "myAuthToken");
        response = handler.handleRequest(request, null);

        assert(response.getUser1().equals("@test"));
        assert(response.getUser2().equals("@tempAlias1"));
        assert(response.isFollows());
    }

    @Test
    void invalidPutFollow() {
        request = new FollowRequest("@test", "@tempAlias1", "invalidAuthToken");

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
