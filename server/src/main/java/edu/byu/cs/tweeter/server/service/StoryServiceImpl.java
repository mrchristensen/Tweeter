package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.resources.StatusResultsPage;
import edu.byu.cs.tweeter.shared.model.service.StoryService;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class StoryServiceImpl implements StoryService {
    private final int PAGE_SIZE = 10;

    @Override
    public StoryResponse getStory(StoryRequest request) {
        System.out.println("getStory: for " + request.getUser());
        String lastStatusTimestamp = null;

        if(request.getLastStatus() != null){
            System.out.println("lastStatus: " + request.getLastStatus().getUser() + request.getLastStatus().getMessageBody() + request.getLastStatus().getDate());
            lastStatusTimestamp = request.getLastStatus().getDate();
        }


        if (validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            StoryDAO dao = new StoryDAO();

            StatusResultsPage results = dao.getStory(request.getUser().getAlias(), PAGE_SIZE, lastStatusTimestamp);

            System.out.println("hasMorePages = hasLastKey() = " + results.hasLastKey());

            return new StoryResponse(results.getStatuses(), results.hasLastKey());
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
