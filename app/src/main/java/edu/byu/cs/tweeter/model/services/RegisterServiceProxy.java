package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class RegisterServiceProxy implements RegisterService {

    private static final String URL_PATH = "/getregister";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public RegisterResponse getRegister(RegisterRequest request) throws IOException {
        RegisterResponse response = serverFacade.getRegister(request, URL_PATH);

        if(response.isRegisterSuccessful()){
            CurrentUserService.getInstance().setCurrentUser(response.getCurrentUser());
        }

        return response;
    }
}
