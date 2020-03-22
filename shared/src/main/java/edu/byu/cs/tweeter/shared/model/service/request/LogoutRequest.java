package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class LogoutRequest {
    private User currentUser;

    public LogoutRequest(User currentUser) {
        this.currentUser = currentUser;
    }

    public LogoutRequest() {
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
