package edu.byu.cs.tweeter.shared.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * Defines the interface for the 'following' service.
 */
public interface RegisterService {

    RegisterResponse doRegister(RegisterRequest request) throws IOException;
}
