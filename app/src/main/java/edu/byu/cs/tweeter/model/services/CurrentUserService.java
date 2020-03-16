package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.shared.model.domain.User;

public class CurrentUserService {
    /**
     * The singleton instance.
     */
    private static CurrentUserService instance;

    /**
     * The logged in user.
     */
    private User currentUser;

    /**
     * Return the singleton instance of this class.
     *
     * @return the instance.
     */
    public static CurrentUserService getInstance() {
        if(instance == null) {
            instance = new CurrentUserService();
        }

        return instance;
    }

    /**
     * A private constructor created to ensure that this class is a singleton (i.e. that it
     * cannot be instantiated by external classes).
     */
    private CurrentUserService() {
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
}
