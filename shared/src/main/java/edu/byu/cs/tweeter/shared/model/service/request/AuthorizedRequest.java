package edu.byu.cs.tweeter.shared.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user (their story).
 */
public class AuthorizedRequest {

    public String authTokenString;
    public String currentUserAlias;

    public AuthorizedRequest() {
    }

    public AuthorizedRequest(String authTokenString, String currentUserAlias) {
        this.authTokenString = authTokenString;
        this.currentUserAlias = currentUserAlias;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public void setAuthTokenString(String authTokenString) {
        this.authTokenString = authTokenString;
    }

    public String getCurrentUserAlias() {
        return currentUserAlias;
    }

    public void setCurrentUserAlias(String currentUserAlias) {
        this.currentUserAlias = currentUserAlias;
    }
}
