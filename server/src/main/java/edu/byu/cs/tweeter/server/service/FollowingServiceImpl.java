package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.FollowingService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowingServiceImpl implements FollowingService {

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) {
        AuthToken authToken = new AuthToken(request.getAuthTokenString());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        if (authTokenDAO.validateAuthToken(authToken)) {
            FollowDAO dao = new FollowDAO();
            return dao.getFollowees(request);
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
