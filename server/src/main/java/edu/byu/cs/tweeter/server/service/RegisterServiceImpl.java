package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.server.dao.RegisterDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse getRegister(RegisterRequest request) {
        RegisterDAO registerDAO = new RegisterDAO();
        RegisterResponse response = registerDAO.getRegister(request);

        if(response.isRegisterSuccessful()){
            AuthTokenDAO authTokenDAO = new AuthTokenDAO();
            AuthToken authToken = authTokenDAO.generateAuthToken(request.getAlias());
            response.setAuthTokenString(authToken.getAuthTokenString());
        }

        return response;
    }
}
