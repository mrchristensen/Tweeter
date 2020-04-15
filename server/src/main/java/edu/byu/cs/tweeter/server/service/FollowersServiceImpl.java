package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.resources.ResultsPage;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.FollowersService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowersServiceImpl implements FollowersService {
    private final int PAGE_SIZE = 10;

    @Override
    public FollowersResponse getFollowers(FollowersRequest request) {
        System.out.println("getFollowers: for " + request.getFollowee().getAlias());
        String lastFollowerAlias = null;

        if(request.getLastFollower() != null){
            System.out.println("lastFollower: " + request.getLastFollower().getAlias());
            lastFollowerAlias = request.getLastFollower().getAlias();
        }


        if (validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            FollowDAO dao = new FollowDAO();

            ResultsPage results = dao.getFollowers(request.getFollowee().getAlias(), PAGE_SIZE, lastFollowerAlias);

            UserDAO userDAO = new UserDAO();
            List<String> followerAliases = results.getValues();
            List<User> followers = new ArrayList<>();

            for (String followerAlias : followerAliases) {
                System.out.println("Getting user object for: " + followerAlias);
                followers.add(userDAO.getUser(followerAlias));
            }
            System.out.println("hasMorePages = hasLastKey() = " + results.hasLastKey());

            return new FollowersResponse(followers, results.hasLastKey());
        } else {
            throw new RuntimeException("forbidden");
        }
    }
}
