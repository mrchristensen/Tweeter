package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.RegisterDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse doRegister(RegisterRequest request) {
        RegisterDAO registerDAO = new RegisterDAO();
        RegisterResponse response = registerDAO.doRegister(request);

        if(response.isRegisterSuccessful()){
            AuthTokenDAO authTokenDAO = new AuthTokenDAO();
            AuthToken authToken = authTokenDAO.generateAuthToken(request.getAlias());
            response.setAuthTokenString(authToken.getAuthTokenString());
        }

        return response;
    }
}
