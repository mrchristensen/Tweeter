package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

public class DoLogoutHandlerTest {

    private DoLogoutHandler handler;
    LogoutRequest request;
    LogoutResponse response;
    User user;

    @BeforeEach
    void setup() {
        handler = new DoLogoutHandler();
        request = null;
        response = null;
        user = new User("Josh", "Smith", "@test", "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U");
    }

    @Test
    void validLogout() {
        request = new LogoutRequest(user, "myAuthToken");
        response = handler.handleRequest(request, null);

        assert(request.getAuthTokenString().equals("myAuthToken"));
        assert(response.getCurrentUser().getAlias().equals("@test"));
        assert(response.isLogoutSuccessful());
    }

    @Test
    void invalidLogout() {
        request = new LogoutRequest(user, "invalidAuthToken");

        try {
            response = handler.handleRequest(request, null);
        }
        catch (Exception e){
            assert(e.getMessage().equals("forbidden"));
        }

        assert(!request.getAuthTokenString().equals("myAuthToken"));
        assert(response.isLogoutSuccessful());
        assert(response.getCurrentUser().getAlias().equals("@test"));
    }

}
