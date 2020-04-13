package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse doRegister(RegisterRequest request) {
        UserDAO userDAO = new UserDAO();

        //todo hash password
//        request.setPassword(request.getPassword().hash());

        if(userDAO.getUser(request.getAlias()) == null){ //The username isn't taken already
            boolean putUser = userDAO.putUser(request.getAlias(), request.getPassword(),
                    request.getFistName(), request.getLastName(), request.getProfileImageURL());

            if(putUser){ //We successfully put the user into the database
                //todo: auth token creation
//                AuthTokenDAO authTokenDAO = new AuthTokenDAO();
//                AuthToken authToken = authTokenDAO.generateAuthToken(request.getAlias());
//                response.setAuthTokenString(authToken.getAuthTokenString());
                return new RegisterResponse(true, new User(request.getFistName(), request.getLastName(), request.getAlias(), request.getProfileImageURL()));
            }
        }

        return new RegisterResponse(false, null);
    }
}
