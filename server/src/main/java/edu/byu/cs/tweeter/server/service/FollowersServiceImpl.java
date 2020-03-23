package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.FollowersService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowersServiceImpl implements FollowersService {

    @Override
    public FollowersResponse getFollowers(FollowersRequest request) {
        AuthToken authToken = new AuthToken(request.getAuthTokenString());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        if (authTokenDAO.validateAuthToken(authToken)) {
            FollowDAO dao = new FollowDAO();
            return dao.getFollowers(request);
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
