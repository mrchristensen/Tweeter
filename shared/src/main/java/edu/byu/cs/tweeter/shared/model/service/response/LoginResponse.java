package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class LoginResponse {

    private boolean loginSuccessful;
    private User currentUser;
    private String authTokenString;

    //Default constructor
    public LoginResponse() {
    }

    public LoginResponse(boolean loginSuccessful, User currentUser, String authTokenString) {
        this.loginSuccessful = loginSuccessful;
        this.currentUser = currentUser;
        this.authTokenString = authTokenString;
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

    public String getAuthTokenString() {
        return authTokenString;
    }

    public void setAuthTokenString(String authTokenString) {
        this.authTokenString = authTokenString;
    }
}
