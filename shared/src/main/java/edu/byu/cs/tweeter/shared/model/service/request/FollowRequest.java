package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * Contains all the information needed to make a request to have the service return the next page of
 * followees for a specified follower.
 */
public class FollowRequest extends AuthorizedRequest {

    public String user1;
    public String user2;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowRequest() {}

    public FollowRequest(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.authTokenString = authTokenString;
    }

    public FollowRequest(String user1, String user2, String authTokenString) {
        this.user1 = user1;
        this.user2 = user2;
        this.authTokenString = authTokenString;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}