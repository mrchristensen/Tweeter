package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse getLogin(LoginRequest request) {
        LoginDAO dao = new LoginDAO();
        return dao.getLogin(request);
    }
}
