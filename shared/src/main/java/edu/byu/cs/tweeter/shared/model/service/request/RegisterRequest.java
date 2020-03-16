package edu.byu.cs.tweeter.shared.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class RegisterRequest {
    public String alias;
    public String password;
    public String fistName;
    public String lastName;
    public String profileImageURL;

    /**
     * Creates an instance.
     */
    public RegisterRequest(String alias, String password, String fistName, String lastName, String profileImageURL) {
        this.alias = alias;
        this.password = password;
        this.fistName = fistName;
        this.lastName = lastName;
        this.profileImageURL = profileImageURL;
    }

    public String getAlias() {
        return alias;
    }

    public String getPassword() {
        return password;
    }

    public String getFistName() {
        return fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }
}
