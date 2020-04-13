package edu.byu.cs.tweeter.server.service;

import java.time.ZonedDateTime;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.generateAuthTokenString;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class RegisterServiceImpl implements RegisterService {
    @Override
    public RegisterResponse doRegister(RegisterRequest request) {
        UserDAO userDAO = new UserDAO();

        //Hash
        request.setPassword(HashingService.hash(request.getPassword()));

        if(userDAO.getUser(request.getAlias()) == null){ //The username isn't taken already
            //todo: upload the picture to S3

            boolean putUser = userDAO.putUser(request.getAlias(), request.getPassword(),
                    request.getFistName(), request.getLastName(), request.getProfileImageURL());

            if(putUser){ //We successfully put the user into the database
                //AuthToken stuff
                String authTokenString = generateAuthTokenString();
                AuthTokenDAO authTokenDAO = new AuthTokenDAO();
                authTokenDAO.putAuthToken(request.getAlias(), authTokenString, ZonedDateTime.now().toString());

                return new RegisterResponse(true, new User(request.getFistName(), request.getLastName(), request.getAlias(), request.getProfileImageURL()), authTokenString);
            }
        }

        return new RegisterResponse(false, null, null);
    }
}
