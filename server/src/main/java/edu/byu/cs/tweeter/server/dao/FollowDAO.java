package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class FollowDAO {

    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/dafny_duck.png";
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
    public FollowersResponse getFollowers(FollowersRequest request) {
        return new FollowersResponse(getNUsers(request.limit), true);
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
            users.add(new User("fname" + i,
                    "lname" + i,
                    "@tempAlias" + i,
                    FEMALE_IMAGE_URL));
        }
        return users;
    }

    //old code
//
//    /**
//     * Generates the followee data.
//     */
//    private Map<User, List<User>> initializeFollowees() {
//
//        Map<User, List<User>> followeesByFollower = new HashMap<>();
//
//        List<Follow> follows = getFollowGenerator().generateUsersAndFollows(100,
//                0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);
//
//        // Populate a map of followees, keyed by follower so we can easily handle followee requests
//        for(Follow follow : follows) {
//            List<User> followees = followeesByFollower.get(follow.getFollower());
//
//            if(followees == null) {
//                followees = new ArrayList<>();
//                followeesByFollower.put(follow.getFollower(), followees);
//            }
//
//            followees.add(follow.getFollowee());
//        }
//
//        return followeesByFollower;
//    }
//
//    /**
//     * Determines the index for the first followee in the specified 'allFollowees' list that should
//     * be returned in the current request. This will be the index of the next followee after the
//     * specified 'lastFollowee'.
//     *
//     * @param lastFollowee the last followee that was returned in the previous request or null if
//     *                     there was no previous request.
//     * @param allFollowees the generated list of followees from which we are returning paged results.
//     * @return the index of the first followee to be returned.
//     */
//    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {
//
//        int followeesIndex = 0;
//
//        if(lastFollowee != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allFollowees.size(); i++) {
//                if(lastFollowee.equals(allFollowees.get(i))) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    followeesIndex = i + 1;
//                }
//            }
//        }
//
//        return followeesIndex;
//    }
//
//    /**
//     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
//     * written as a separate method to allow mocking of the generator.
//     *
//     * @return the generator.
//     */
//    FollowGenerator getFollowGenerator() {
//        return FollowGenerator.getInstance();
//    }
}