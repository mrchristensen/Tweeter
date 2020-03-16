package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.model.domain.Follow;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class LoginDAO {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    public LoginResponse getLogin(LoginRequest request) {
        if (request.getAlias().equals("@test") && request.password.equals("test")) {
            //Successful login
            return new LoginResponse(true, new User("Josh", "Smith", request.getAlias(), MALE_IMAGE_URL));
        }

        //Unsuccessful Login
        return new LoginResponse(false, null);
    }

}