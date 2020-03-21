package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.FollowService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class FollowServiceProxy implements FollowService {

    private static final String URL_PATH = "/follows";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public FollowResponse getFollow(FollowRequest request) throws IOException {
        String urlPath = "/" + request.getUser1() + URL_PATH + "/" + request.getUser2();

        return serverFacade.getFollow(request, urlPath);
    }

    @Override
    public FollowResponse deleteFollow(FollowRequest request) throws IOException {
        String urlPath = "/" + request.getUser1() + URL_PATH + "/" + request.getUser2();

        return serverFacade.deleteFollow(request, urlPath);
    }

    @Override
    public FollowResponse putFollow(FollowRequest request) {
        return null; //todo add functionality
    }
}
