package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class LogoutResponse {

    private boolean logoutSuccessful;
    private User currentUser;


    public LogoutResponse(boolean loginSuccessful, User currentUser) {
        this.logoutSuccessful = loginSuccessful;
        this.currentUser = currentUser;
    }

    public LogoutResponse() {
    }

    public boolean logoutSuccessful() {
        return logoutSuccessful;
    }

    public boolean isLogoutSuccessful() {
        return logoutSuccessful;
    }

    public void setLogoutSuccessful(boolean logoutSuccessful) {
        this.logoutSuccessful = logoutSuccessful;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
