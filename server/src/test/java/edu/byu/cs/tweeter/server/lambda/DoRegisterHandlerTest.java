package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

public class DoRegisterHandlerTest {

    private DoRegisterHandler handler;
    RegisterRequest request;
    RegisterResponse response;

    @BeforeEach
    void setup() {
        handler = new DoRegisterHandler();
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
