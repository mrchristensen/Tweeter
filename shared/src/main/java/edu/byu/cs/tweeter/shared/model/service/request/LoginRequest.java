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

    public LoginRequest() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
