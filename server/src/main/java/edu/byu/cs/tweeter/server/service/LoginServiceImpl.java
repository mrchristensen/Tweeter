package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse getLogin(LoginRequest request) {
        LoginDAO loginDAO = new LoginDAO();
        LoginResponse response = loginDAO.getLogin(request);

        if(response.loginSuccessful()){ //If correct login
            AuthTokenDAO authTokenDAO = new AuthTokenDAO();
            AuthToken authToken = authTokenDAO.generateAuthToken(request.getAlias());
            response.setAuthTokenString(authToken.getAuthTokenString());
        }

        return response;
    }
}
