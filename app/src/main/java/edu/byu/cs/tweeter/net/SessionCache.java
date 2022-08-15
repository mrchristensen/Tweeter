package edu.byu.cs.tweeter.net;

import edu.byu.cs.tweeter.shared.model.domain.User;

public class SessionCache {
    /**
     * The singleton instance.
     */
    private static SessionCache instance;

    /**
     * The logged in user.
     */
    private User currentUser;

    private String authTokenString;

    /**
     * Return the singleton instance of this class.
     *
     * @return the instance.
     */
    public static SessionCache getInstance() {
        if(instance == null) {
            instance = new SessionCache();
        }

        return instance;
    }

    /**
     * A private constructor created to ensure that this class is a singleton (i.e. that it
     * cannot be instantiated by external classes).
     */
    private SessionCache() {
    }

    /**
     * Returns the currently logged in user.
     *
     * @return the user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public static void setInstance(SessionCache instance) {
        SessionCache.instance = instance;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public void setAuthTokenString(String authTokenString) {
        this.authTokenString = authTokenString;
    }
}
