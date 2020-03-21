package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.model.service.FollowService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowServiceImpl implements FollowService {

    FollowDAO dao;

    public FollowServiceImpl() {
        dao = new FollowDAO();
    }

    @Override
    public FollowResponse getFollow(FollowRequest request) {
        return dao.getFollow(request);
    }

    @Override
    public FollowResponse deleteFollow(FollowRequest request) {
        return dao.removeFollow(request);
    }

    @Override
    public FollowResponse putFollow(FollowRequest request) {
         return dao.addFollow(request);
    }
}
