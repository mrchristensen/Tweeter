package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.net.SessionCache;
import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A common base class for all presenters in the application.
 */
public abstract class Presenter {

    /**
     * Returns the currently logged in user.
     *
     * @return the user.
     */
    public User getCurrentUser() {
        return SessionCache.getInstance().getCurrentUser();
    }
}