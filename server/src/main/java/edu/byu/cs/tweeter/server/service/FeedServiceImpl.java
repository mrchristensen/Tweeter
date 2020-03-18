package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.shared.model.service.FeedService;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;

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
