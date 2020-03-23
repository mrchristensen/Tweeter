package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

public class DoLoginHandlerTest {

    private DoLoginHandler handler;
    LoginRequest request;
    LoginResponse response;

    @BeforeEach
    void setup() {
        handler = new DoLoginHandler();
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
