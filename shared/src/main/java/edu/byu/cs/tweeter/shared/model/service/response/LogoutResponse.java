package edu.byu.cs.tweeter.shared.model.service.response;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class LogoutResponse {

    private boolean logoutSuccessful;
    private String currentUserAlias;


    public LogoutResponse(boolean loginSuccessful, String currentUserAlias) {
        this.logoutSuccessful = loginSuccessful;
        this.currentUserAlias = currentUserAlias;
    }

    public LogoutResponse() {
    }

    public boolean logoutSuccessful() {
        return logoutSuccessful;
    }

    public boolean isLogoutSuccessful() {
        return logoutSuccessful;
    }

    public void setLogoutSuccessful(boolean logoutSuccessful) {
        this.logoutSuccessful = logoutSuccessful;
    }

    public String getCurrentUserAlias() {
        return currentUserAlias;
    }

    public void setCurrentUserAlias(String currentUserAlias) {
        this.currentUserAlias = currentUserAlias;
    }
}
