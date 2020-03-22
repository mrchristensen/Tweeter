package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.LogoutService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class LogoutServiceProxy implements LogoutService {

    private static final String URL_PATH = "/dologout";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public LogoutResponse doLogout(LogoutRequest request) throws IOException {
        LogoutResponse response = serverFacade.doLogout(request, URL_PATH);

        return response;
    }
}
