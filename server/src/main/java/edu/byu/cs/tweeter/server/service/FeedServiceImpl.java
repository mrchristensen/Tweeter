package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.shared.model.service.FeedService;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FeedServiceImpl implements FeedService {
    @Override
    public FeedResponse getFeed(FeedRequest request) {
        if (validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            StoryDAO dao = new StoryDAO();
            return dao.getFeed(request);
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
