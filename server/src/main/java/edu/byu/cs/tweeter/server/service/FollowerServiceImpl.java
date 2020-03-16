package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.shared.model.service.FollowerService;
import edu.byu.cs.tweeter.shared.model.service.FollowingService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowerServiceImpl implements FollowerService {

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        FollowerDAO dao = new FollowerDAO();
        return dao.getFollowers(request);
    }
}
