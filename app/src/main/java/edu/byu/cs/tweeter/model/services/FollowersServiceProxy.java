package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.FollowersService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class FollowersServiceProxy implements FollowersService {

    private static final String URL_PATH = "/getfollowers";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public FollowersResponse getFollowers(FollowersRequest request) throws IOException {
        return serverFacade.getFollowers(request, URL_PATH);
    }
}
