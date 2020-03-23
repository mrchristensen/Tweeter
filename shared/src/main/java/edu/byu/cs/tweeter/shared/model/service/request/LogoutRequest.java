package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class LogoutRequest extends AuthorizedRequest {
    private User currentUser;

    public LogoutRequest() {
    }

    public LogoutRequest(User currentUser, String authTokenString) {
        this.currentUser = currentUser;
        this.authTokenString = authTokenString;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
