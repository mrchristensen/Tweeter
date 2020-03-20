package edu.byu.cs.tweeter.shared.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user (their story).
 */
public class FindUserRequest {
    public String userAlias;

    public FindUserRequest() {
    }

    public FindUserRequest(String useralias) {
        this.userAlias = useralias;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String useralias) {
        this.userAlias = useralias;
    }
}
