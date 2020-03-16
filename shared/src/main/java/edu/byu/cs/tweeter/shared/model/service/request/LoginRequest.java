package edu.byu.cs.tweeter.shared.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class LoginRequest {

    public String alias;
    public String password;

    /**
     * Creates an instance.
     */
    public LoginRequest(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }


    public String getAlias() {
        return alias;
    }

    public String getPassword() {
        return password;
    }
}
