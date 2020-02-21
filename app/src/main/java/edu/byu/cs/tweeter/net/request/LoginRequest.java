package edu.byu.cs.tweeter.net.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class LoginRequest {

    private final String alias;
    private final String password;

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
