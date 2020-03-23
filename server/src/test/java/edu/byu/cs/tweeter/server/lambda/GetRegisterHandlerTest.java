package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.server.lambda.GetLoginHandler;
import edu.byu.cs.tweeter.server.lambda.GetRegisterHandler;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

public class GetRegisterHandlerTest {

    private GetRegisterHandler handler;
    RegisterRequest request;
    RegisterResponse response;

    @BeforeEach
    void setup() {
        handler = new GetRegisterHandler();
        request = null;
        response = null;
    }

    @Test
    void validRegister() {
        request = new RegisterRequest("username", "test", "firstn", "lastn", "url");
        response = handler.handleRequest(request, null);

        assert(response.getCurrentUser().getAlias().equals("username"));
        assert(response.isRegisterSuccessful());
        assert(response.getAuthTokenString().equals("myAuthToken"));
    }

    @Test
    void invalidRegisterTakenUsername() {
        request = new RegisterRequest("test", "test", "firstn", "lastn", "url");
        response = handler.handleRequest(request, null);

        assert(response.getCurrentUser() == null);
        assert(!response.isRegisterSuccessful());
        assert(response.getAuthTokenString() == null);
    }

}
