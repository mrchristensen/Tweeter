package edu.byu.cs.tweeter.shared.model.domain;

import java.time.ZonedDateTime;

public class AuthToken {
    private final String userAlias;
    private final String authTokenString;
    private final String timestamp;

    public AuthToken(String userAlias, String authTokenString, String timestamp) {
        this.userAlias = userAlias;
        this.authTokenString = authTokenString;
        this.timestamp = timestamp;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public ZonedDateTime getTimeStamp() {
        return ZonedDateTime.parse(timestamp);
    }
}