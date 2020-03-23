package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class RegisterDAO {

    public RegisterResponse doRegister(RegisterRequest request) {
        //TODO: Story passwords in hashes
        if (!request.getAlias().equals("test")) {
            //Successful register
            return new RegisterResponse(true, new User(request.getFistName(), request.getLastName(), request.getAlias(), request.getProfileImageURL()));
        }

        //Unsuccessful Login
        return new RegisterResponse(false, null);
    }

}