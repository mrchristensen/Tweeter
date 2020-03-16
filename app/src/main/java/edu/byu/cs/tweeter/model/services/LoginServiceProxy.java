package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.FollowingService;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class LoginServiceProxy implements LoginService {

    private static final String URL_PATH = "/getlogin";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public LoginResponse getLogin(LoginRequest request) throws IOException {
        LoginResponse response = serverFacade.getLogin(request, URL_PATH);

        if(response.isLoginSuccessful()){
            CurrentUserService.getInstance().setCurrentUser(new User("Test","User","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
        }

        return response;
    }
}
