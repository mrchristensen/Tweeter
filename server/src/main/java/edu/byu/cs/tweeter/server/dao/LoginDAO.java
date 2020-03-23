package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class LoginDAO {

    private static final String MALE_IMAGE_URL = "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U";


    public LoginResponse doLogin(LoginRequest request) {
        if (request.getAlias().equals("@test") && request.password.equals("test")) {
            //Successful login
            return new LoginResponse(true, new User("Josh", "Smith", request.getAlias(), MALE_IMAGE_URL));
        }

        //Unsuccessful Login
        return new LoginResponse(false, null);
    }

}