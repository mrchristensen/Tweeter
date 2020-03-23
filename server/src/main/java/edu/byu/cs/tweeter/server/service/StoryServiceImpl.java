package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.StoryService;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class StoryServiceImpl implements StoryService {
    @Override
    public StoryResponse getStory(StoryRequest request) {

        AuthToken authToken = new AuthToken(request.getAuthTokenString());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        if (authTokenDAO.validateAuthToken(authToken)) {
            StatusDAO dao = new StatusDAO();
            return dao.getStory(request);
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
