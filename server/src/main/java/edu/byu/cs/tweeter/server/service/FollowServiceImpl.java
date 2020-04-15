package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.shared.model.service.FollowService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

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
        System.out.println("User1: " + request.getUser1());
        System.out.println("User2: " + request.getUser1());
        System.out.println("AuthToken: " + request.getAuthTokenString());
        System.out.println("CurrentUser: " + request.getCurrentUserAlias());

        return new FollowResponse(dao.getFollow(request.getUser1(), request.getUser2()), request.getUser1(), request.getUser2());
    }

    @Override
    public FollowResponse removeFollow(FollowRequest request) {
        System.out.println("User1: " + request.getUser1());
        System.out.println("User2: " + request.getUser2());
        System.out.println("AuthToken: " + request.getAuthTokenString());
        System.out.println("CurrentUser: " + request.getCurrentUserAlias());

        if(validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            dao.deleteFollow(request.getUser1(), request.getUser2());
            return new FollowResponse( false, request.getUser1(), request.getUser2());
        }
        else{
            throw new RuntimeException("forbidden");
        }
    }

    @Override
    public FollowResponse addFollow(FollowRequest request) {
        System.out.println("User1: " + request.getUser1());
        System.out.println("User2: " + request.getUser1());
        System.out.println("AuthToken: " + request.getAuthTokenString());
        System.out.println("CurrentUser: " + request.getCurrentUserAlias());

        if(validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())) {
            return new FollowResponse(dao.putFollow(request.getUser1(), request.getUser2()), request.getUser1(), request.getUser2());
        }
        else{
            throw new RuntimeException("forbidden");
        }
    }
}
