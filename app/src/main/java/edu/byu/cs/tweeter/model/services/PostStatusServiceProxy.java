package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.PostStatusService;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;

/**
 * A remote-access proxy for accessing the 'following' service.
 */
public class PostStatusServiceProxy implements PostStatusService {

    private static final String URL_PATH = "/poststatus";

    private final ServerFacade serverFacade = new ServerFacade();


    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) throws IOException {
        PostStatusResponse response = serverFacade.postStatus(request, URL_PATH);

        return response;
    }
}
