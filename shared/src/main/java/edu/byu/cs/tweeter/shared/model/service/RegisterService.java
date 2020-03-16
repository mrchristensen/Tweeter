package edu.byu.cs.tweeter.shared.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * Defines the interface for the 'following' service.
 */
public interface RegisterService {

    RegisterResponse getRegister(RegisterRequest request) throws IOException;
}
