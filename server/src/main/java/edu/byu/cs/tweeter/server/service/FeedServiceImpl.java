package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.FeedService;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FeedServiceImpl implements FeedService {
    @Override
    public FeedResponse getFeed(FeedRequest request) {
        AuthToken authToken = new AuthToken(request.getAuthTokenString());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        if (authTokenDAO.validateAuthToken(authToken)) {
            StatusDAO dao = new StatusDAO();
            return dao.getFeed(request);
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
