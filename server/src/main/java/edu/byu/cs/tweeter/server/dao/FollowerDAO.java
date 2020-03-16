package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.shared.model.domain.Follow;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class FollowerDAO {

    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    /**
     * Gets the users from the database that the user specified in the request is follower. Uses
     * information in the request object to limit the number of followers returned and to return the
     * next set of followers after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public FollowerResponse getFollowers(FollowerRequest request) {
        return new FollowerResponse(getNUsers(request.limit), true);
    }

    private List<User> getNUsers(int n){
        List<User> users = new ArrayList<>();
        for(int i=0; i < n; i++){
            users.add(new User("fname" + Integer.toString(i),
                    "lname" + Integer.toString(i),
                    "@tempAlias" + Integer.toString(i),
                    FEMALE_IMAGE_URL));
        }
        return users;
    }
}