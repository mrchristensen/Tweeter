package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.server.lambda.FindUserHandler;
import edu.byu.cs.tweeter.server.lambda.GetRegisterHandler;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

public class FindUserHandlerTest {

    private FindUserHandler handler;
    FindUserRequest request;
    FindUserResponse response;

    @BeforeEach
    void setup() {
        handler = new FindUserHandler();
        request = null;
        response = null;
    }

    @Test
    void validFindTestUser() {
        request = new FindUserRequest("@test");
        response = handler.handleRequest(request, null);

        assert(response.getUser().getAlias().equals("@test"));
        assert(response.getUser().getFirstName().equals("Josh"));
        assert(response.getUser().getLastName().equals("Smith"));
        assert(response.isSuccessful());
    }

    @Test
    void validFindTempUser() {
        for (int i = 0; i < 10; i++) {
            request = new FindUserRequest("@tempAlias" + i);
            response = handler.handleRequest(request, null);

            assert(response.getUser().getAlias().equals("@tempAlias" + i));
            assert(response.getUser().getFirstName().equals("fname" + i));
            assert(response.getUser().getLastName().equals("lname" + i));
            assert(response.isSuccessful());
        }
    }

    @Test
    void invalidFindNonexistentUser() {
        request = new FindUserRequest("@wrongUsername");
        response = handler.handleRequest(request, null);

        assert(response.getUser() == null);
        assert(!response.isSuccessful());
        assert(!response.getUserAlias().equals("@test"));
        assert(!response.getUserAlias().contains("@tempAlias"));
    }

}
