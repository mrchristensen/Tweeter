package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.server.lambda.PostStatusHandler;
import edu.byu.cs.tweeter.server.lambda.PutFollowHandler;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;

public class PostStatusHandlerTest {

    private PostStatusHandler handler;
    PostStatusRequest request;
    PostStatusResponse response;
    Status status;
    User user;

    @BeforeEach
    void setup() {
        handler = new PostStatusHandler();
        request = null;
        response = null;
        user = new User("Josh", "Smith", "@test", "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
        status = new Status(user, "date", "This is great status content!");
    }

    @Test
    void validPostStatus() {
        request = new PostStatusRequest(status, "myAuthToken");
        response = handler.handleRequest(request, null);

        assert(request.getAuthTokenString().equals("myAuthToken"));
        assert(response.getStatus().getUser().getAlias().equals("@test"));
        assert(response.getStatus().getUser().getFirstName().equals("Josh"));
        assert(response.getStatus().getUser().getLastName().equals("Smith"));
        assert(response.getStatus().getDate().equals("date"));
        assert(response.getStatus().getMessageBody().equals("This is great status content!"));
    }

    @Test
    void invalidPostStatus() {
        request = new PostStatusRequest(status, "invalidAuthToken");

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
