package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the service return the next page of
 * followees for a specified follower.
 */
public class PostStatusRequest extends AuthorizedRequest {

    public Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest() {
    }

    public PostStatusRequest(Status status, String authTokenString, String currentUserAlias) {
        this.status = status;
        this.authTokenString = authTokenString;
        this.currentUserAlias = currentUserAlias;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}