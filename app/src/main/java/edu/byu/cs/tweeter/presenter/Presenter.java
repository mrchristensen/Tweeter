package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.CurrentUserService;
import edu.byu.cs.tweeter.shared.model.domain.User;
//import edu.byu.cs.tweeter.model.services.LoginService; todo is this needed?

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
        return CurrentUserService.getInstance().getCurrentUser();
    }
}