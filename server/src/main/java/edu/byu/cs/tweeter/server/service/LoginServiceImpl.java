package edu.byu.cs.tweeter.server.service;

import java.time.ZonedDateTime;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.generateAuthTokenString;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse doLogin(LoginRequest request) {
        UserDAO userDAO = new UserDAO();

        //Hash
        request.setPassword(HashingService.hash(request.getPassword()));

        if(userDAO.validateLogin(request.getAlias(), request.getPassword())){
            //Successful login

            //AuthToken stuff
            String authTokenString = generateAuthTokenString();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO();
            authTokenDAO.putAuthToken(request.getAlias(), authTokenString, ZonedDateTime.now().toString());

            return new LoginResponse(true, userDAO.getUser(request.alias), authTokenString);

        }
        else {
            //Unsuccessful login
            return new LoginResponse(false, null, null);
        }

    }
}
