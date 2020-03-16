package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class RegisterDAO {

    public RegisterResponse getRegister(RegisterRequest request) {
        if (!request.getAlias().equals("@test")) {
            //Successful register
            return new RegisterResponse(true, new User("Josh", "Smith", request.getAlias(), request.getProfileImageURL()));
        }

        //Unsuccessful Login
        return new RegisterResponse(false, null);
    }

}