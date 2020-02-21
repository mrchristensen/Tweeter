package edu.byu.cs.tweeter.net.response;

import edu.byu.cs.tweeter.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.net.request.RegisterRequest}.
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
