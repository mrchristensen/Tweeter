package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class UserDAO {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/dafny_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    public FindUserResponse findUser(FindUserRequest request) {
        String userAlias = request.getUserAlias();

        //TODO: Find the actual user once databases are implemented (instead of looking for hardcoded users)
        if(userAlias.contains("tempAlias") && userAlias.charAt(userAlias.length() - 1) - '0' <= 9){  //Check for tempAlias's 0-9
            char i = userAlias.charAt(userAlias.length() - 1); //get number at the end

            User tempUser = new User("fname" + i, "lname" + i, userAlias,
                    FEMALE_IMAGE_URL);
            return new FindUserResponse(true, tempUser);

        }
        else if (userAlias.equals("@test")){
            return new FindUserResponse(true, new User("Josh", "Smith", "@test", FEMALE_IMAGE_URL));
        }
        else{
            return new FindUserResponse(false, userAlias);
        }
    }

}