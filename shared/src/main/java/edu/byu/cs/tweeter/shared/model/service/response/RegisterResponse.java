package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest}.
 */
public class RegisterResponse {

    private boolean registerSuccessful;
    private User currentUser;


    public RegisterResponse(boolean registerSuccessful, User currentUser) {
        this.registerSuccessful = registerSuccessful;
        this.currentUser = currentUser;
    }

    public boolean registerSuccessful() {
        return registerSuccessful;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
