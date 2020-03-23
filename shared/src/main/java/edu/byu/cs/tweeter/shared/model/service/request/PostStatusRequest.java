package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the service return the next page of
 * followees for a specified follower.
 */
public class PostStatusRequest {

    public Status status;
    public String authTokenString;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {
        authTokenString = "defaultValue";
    }

    public PostStatusRequest(Status status) {
        this.status = status;
        authTokenString = "defaultValue";
    }

    public PostStatusRequest(Status status, String authTokenString) {
        this.status = status;
        this.authTokenString = authTokenString;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public void setAuthTokenString(String authTokenString) {
        this.authTokenString = authTokenString;
    }
}