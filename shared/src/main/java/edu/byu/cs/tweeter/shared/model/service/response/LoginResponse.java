package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
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

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
