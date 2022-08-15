package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class LoginServiceProxy implements LoginService {

    private static final String URL_PATH = "/dologin";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public LoginResponse doLogin(LoginRequest request) throws IOException {
        LoginResponse response = serverFacade.doLogin(request, URL_PATH);

        return response;
    }
}
