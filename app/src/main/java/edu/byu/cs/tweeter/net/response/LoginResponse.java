package edu.byu.cs.tweeter.net.response;

import edu.byu.cs.tweeter.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.net.request.FollowingRequest}.
 */
public class LoginResponse {

    private boolean loginSuccessful;
    private User currentUser;


    public LoginResponse(boolean loginSuccessful, User currentUser) {
        this.loginSuccessful = loginSuccessful;
        this.currentUser = currentUser;
    }

    public boolean loginSuccessful() {
        return loginSuccessful;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
