package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.FollowService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

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
    public FollowResponse removeFollow(FollowRequest request) throws IOException {
        String urlPath = "/" + request.getUser1() + URL_PATH + "/" + request.getUser2();

        return serverFacade.removeFollow(request, urlPath);
    }

    @Override
    public FollowResponse addFollow(FollowRequest request) throws IOException {
        String urlPath = "/" + request.getUser1() + URL_PATH + "/" + request.getUser2();

        return serverFacade.addFollow(request, urlPath);
    }
}
