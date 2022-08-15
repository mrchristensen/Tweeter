package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class FindUserResponse {

    public boolean successful;
    public User user;
    public String userAlias;


    public FindUserResponse() {
    }

    public FindUserResponse(boolean successful, User user) {
        this.successful = successful;
        this.user = user;
    }

    public FindUserResponse(boolean successful, String userAlias) {
        this.successful = successful;
        this.userAlias = userAlias;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
