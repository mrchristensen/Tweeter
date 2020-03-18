package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.shared.model.service.FeedService;
import edu.byu.cs.tweeter.shared.model.service.FollowerService;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FeedServiceImpl implements FeedService {
    @Override
    public FeedResponse getFeed(FeedRequest request) {
        FeedDAO dao = new FeedDAO();
        return dao.getFeed(request);
    }
}
