package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.resources.ResultsPage;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.FollowingService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowingServiceImpl implements FollowingService {
    private final int PAGE_SIZE = 10;

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) {
        System.out.println("getFollowers: for " + request.getFollower().getAlias());
        String lastFolloweeAlias = null;

        if(request.getLastFollowee() != null){
            System.out.println("lastFollower: " + request.getLastFollowee().getAlias());
            lastFolloweeAlias = request.getLastFollowee().getAlias();
        }

        if (validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            FollowDAO dao = new FollowDAO();

            ResultsPage results = dao.getFollowees(request.getFollower().getAlias(), PAGE_SIZE, lastFolloweeAlias);

            UserDAO userDAO = new UserDAO();
            List<String> followeeAliases = results.getValues();
            List<User> followees = new ArrayList<>();

            for (String followeeAlias : followeeAliases) {
                System.out.println("Getting user object for: " + followeeAlias);
                followees.add(userDAO.getUser(followeeAlias));
            }
            System.out.println("hasMorePages = hasLastKey() = " + results.hasLastKey());

            return new FollowingResponse(followees, results.hasLastKey());
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
