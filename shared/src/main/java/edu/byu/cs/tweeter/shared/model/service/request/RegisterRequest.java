package edu.byu.cs.tweeter.shared.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class RegisterRequest {
    private String alias;
    private String password;
    private String fistName;
    private String lastName;
    private String profileImageURL;

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

    public RegisterRequest() {
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

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
