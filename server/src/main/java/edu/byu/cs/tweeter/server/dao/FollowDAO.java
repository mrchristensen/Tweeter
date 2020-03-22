package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.generators.UserGenerator;
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
        return new FollowersResponse(UserGenerator.getNUsers(request.limit), true);
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        return new FollowingResponse(UserGenerator.getNUsers(request.limit), true);
    }

    public FollowResponse getFollow(FollowRequest request){
        //TODO: get the database and see if user1 follows user2
        return new FollowResponse(true, request.getUser1(), request.getUser2());
    }

    public FollowResponse removeFollow(FollowRequest request){
        //TODO: get the database and remove user1 following user2
        return new FollowResponse(false, request.getUser1(), request.getUser2());
    }

    public FollowResponse addFollow(FollowRequest request){
        //TODO: get the database and add that user1 follows user2
        return new FollowResponse(true, request.getUser1(), request.getUser2());
    }

}