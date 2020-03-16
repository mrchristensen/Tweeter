package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.server.dao.RegisterDAO;
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
        RegisterDAO dao = new RegisterDAO();
        return dao.getRegister(request);
    }
}
