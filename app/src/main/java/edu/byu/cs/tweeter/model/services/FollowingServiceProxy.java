package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.FollowingService;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class FollowingServiceProxy implements FollowingService {

    private static final String URL_PATH = "/getfollowing";

    private final ServerFacade serverFacade = new ServerFacade();

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) throws IOException {
        return serverFacade.getFollowees(request, URL_PATH);
    }
}
