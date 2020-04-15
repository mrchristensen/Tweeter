package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.resources.StatusResultsPage;
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
        System.out.println("getFeed: for " + request.getUser());
        String lastStatusTimestamp = null;

        if (request.getLastStatus() != null) {
            System.out.println("lastStatus: " + request.getLastStatus().getUser() + request.getLastStatus().getMessageBody() + request.getLastStatus().getDate());
            lastStatusTimestamp = request.getLastStatus().getDate();
        }

        if (validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            FeedDAO dao = new FeedDAO();

            StatusResultsPage results = dao.getFeed(request.getUser().getAlias(), request.getLimit(), lastStatusTimestamp);

            System.out.println("hasMorePages = hasLastKey() = " + results.hasLastKey());

            return new FeedResponse(results.getStatuses(), results.hasLastKey());
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
