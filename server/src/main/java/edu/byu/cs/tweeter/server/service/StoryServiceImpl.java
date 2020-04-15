package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.resources.ResultsPage;
import edu.byu.cs.tweeter.server.resources.StatusResultsPage;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.StoryService;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;
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
            StatusDAO dao = new StatusDAO();

            StatusResultsPage results = dao.getStory(request.getUser().getAlias(), PAGE_SIZE, lastStatusTimestamp);

            System.out.println("hasMorePages = hasLastKey() = " + results.hasLastKey());

            return new StoryResponse(results.getStatuses(), results.hasLastKey());
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
