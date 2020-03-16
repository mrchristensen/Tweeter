package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.FollowerService;
import edu.byu.cs.tweeter.shared.model.service.FollowingService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class FollowerServiceProxy implements FollowerService {

    private static final String URL_PATH = "/getfollowers";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) throws IOException {
        return serverFacade.getFollowers(request, URL_PATH);
    }
}
