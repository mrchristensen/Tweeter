package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

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

            //Todo: authtoken stuff
//            AuthTokenDAO authTokenDAO = new AuthTokenDAO();
//            AuthToken authToken = authTokenDAO.generateAuthToken(request.getAlias());
//            response.setAuthTokenString(authToken.getAuthTokenString());

            return new LoginResponse(true, userDAO.getUser(request.alias));

        }
        else {
            //Unsuccessful login
            return new LoginResponse(false, null);
        }

    }
}
