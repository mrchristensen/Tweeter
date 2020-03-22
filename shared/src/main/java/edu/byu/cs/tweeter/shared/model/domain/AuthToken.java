package edu.byu.cs.tweeter.shared.model.domain;

public class AuthToken {
    private final String authTokenString;

    public AuthToken(String authTokenString) {
        this.authTokenString = authTokenString;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }
}