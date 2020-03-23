package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.FollowService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowServiceImpl implements FollowService {

    FollowDAO dao;

    public FollowServiceImpl() {
        dao = new FollowDAO();
    }

    @Override
    public FollowResponse getFollow(FollowRequest request) {
        return dao.getFollow(request);
    }

    @Override
    public FollowResponse removeFollow(FollowRequest request) {
        System.out.println("AuthToken: " + request.getAuthTokenString());
        System.out.println("User1: " + request.getUser1());
        System.out.println("User2: " + request.getUser2());

        AuthToken authToken = new AuthToken(request.getAuthTokenString());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        if(authTokenDAO.validateAuthToken(authToken)) {
            return dao.removeFollow(request);
        }
        else{
            throw new RuntimeException("forbidden");
        }
    }

    @Override
    public FollowResponse addFollow(FollowRequest request) {
        System.out.println("User1: " + request.getUser1());
        System.out.println("AuthToken: " + request.getAuthTokenString());


        AuthToken authToken = new AuthToken(request.getAuthTokenString());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();

        if(authTokenDAO.validateAuthToken(authToken)) {
            return dao.addFollow(request);
        }
        else{
            throw new RuntimeException("forbidden");
        }
    }
}
