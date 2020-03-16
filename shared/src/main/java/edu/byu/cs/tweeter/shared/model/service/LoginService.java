package edu.byu.cs.tweeter.shared.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * Defines the interface for the 'following' service.
 */
public interface LoginService {

    LoginResponse getLogin(LoginRequest request) throws IOException;
}