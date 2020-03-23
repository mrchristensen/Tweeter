package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class UserDAO {

    private static final String MALE_IMAGE_URL = "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/dafny.png?token=ALDCLZTJF3B6EVFT7XYNC526QF32U";
    private static final String FEMALE_IMAGE_URL = "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/daisy.png?token=ALDCLZTBHGVXW27AWFPQNTC6QF36C";

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
            return new FindUserResponse(true, new User("Josh", "Smith", "@test", MALE_IMAGE_URL));
        }
        else{
            return new FindUserResponse(false, userAlias);
        }
    }

}