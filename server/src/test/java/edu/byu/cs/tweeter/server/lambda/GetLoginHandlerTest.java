package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.server.lambda.DoLogoutHandler;
import edu.byu.cs.tweeter.server.lambda.GetLoginHandler;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

public class GetLoginHandlerTest {

    private GetLoginHandler handler;
    LoginRequest request;
    LoginResponse response;

    @BeforeEach
    void setup() {
        handler = new GetLoginHandler();
        request = null;
        response = null;
    }

    @Test
    void validLogin() {
        request = new LoginRequest("@test", "test");
        response = handler.handleRequest(request, null);

        assert(response.getCurrentUser().getAlias().equals("@test"));
        assert(response.loginSuccessful());
        assert(response.getAuthTokenString().equals("myAuthToken"));
    }

    @Test
    void invalidLoginPassword() {
        request = new LoginRequest("@test", "wrongPassword");
        response = handler.handleRequest(request, null);

        assert(response.getCurrentUser() == null);
        assert(!response.loginSuccessful());
        assert(response.getAuthTokenString() == null);
    }

    @Test
    void invalidLoginUserName() {
        request = new LoginRequest("@wronglogin", "test");
        response = handler.handleRequest(request, null);

        assert(response.getCurrentUser() == null);
        assert(!response.loginSuccessful());
        assert(response.getAuthTokenString() == null);
    }

}
