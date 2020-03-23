package edu.byu.cs.tweeter.shared.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;

/**
 * Defines the interface for the 'following' service.
 */
public interface FindUserService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followers.
     */
    FindUserResponse findUser(FindUserRequest request) throws IOException;
}
