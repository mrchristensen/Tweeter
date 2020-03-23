package edu.byu.cs.tweeter.shared.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * Defines the interface for the 'following' service.
 */
public interface LogoutService {
    LogoutResponse doLogout(LogoutRequest request) throws IOException;
}
