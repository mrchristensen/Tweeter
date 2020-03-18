package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.model.service.FollowerService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowerServiceImpl implements FollowerService {

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        FollowDAO dao = new FollowDAO();
        return dao.getFollowers(request);
    }
}
