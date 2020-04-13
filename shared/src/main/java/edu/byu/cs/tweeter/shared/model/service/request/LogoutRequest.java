package edu.byu.cs.tweeter.shared.model.service.request;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class LogoutRequest extends AuthorizedRequest {
    public LogoutRequest() {
    }

    public LogoutRequest(String currentUserAlias, String authTokenString) {
        this.currentUserAlias = currentUserAlias;
        this.authTokenString = authTokenString;
    }

}
