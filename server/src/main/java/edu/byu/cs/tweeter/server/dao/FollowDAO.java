package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class FollowDAO {

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

    public FollowingResponse getFollowees(FollowingRequest request) {
        return new FollowingResponse(getNUsers(request.limit), true);

//        assert request.getLimit() > 0;
////        assert request.getFollower() != null;
////
////        if(followeesByFollower == null) {
////            followeesByFollower = initializeFollowees();
////        }
////
////        List<User> allFollowees = followeesByFollower.get(request.getFollower());
////        List<User> responseFollowees = new ArrayList<>(request.getLimit());
////
////        boolean hasMorePages = false;
////
////        if(request.getLimit() > 0) {
////            if (allFollowees != null) {
////                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);
////
////                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
////                    responseFollowees.add(allFollowees.get(followeesIndex));
////                }
////
////                hasMorePages = followeesIndex < allFollowees.size();
////            }
////        }
////
////        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    public FollowResponse getFollow(FollowRequest request){
        return new FollowResponse(true, request.getUser1(), request.getUser2());
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