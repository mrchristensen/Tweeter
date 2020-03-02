package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * Contains the business logic for login and sign up.
 */
public class LoginService {

    /**
     * The singleton instance.
     */
    private static LoginService instance;

    private final ServerFacade serverFacade;

    /**
     * The logged in user.
     */
    private User currentUser;

    /**
     * Return the singleton instance of this class.
     *
     * @return the instance.
     */
    public static LoginService getInstance() {
        if(instance == null) {
            instance = new LoginService();
        }

        return instance;
    }

    /**
     * A private constructor created to ensure that this class is a singleton (i.e. that it
     * cannot be instantiated by external classes).
     */
    private LoginService() {
        serverFacade = new ServerFacade();
    }

    /**
     * Returns the currently logged in user.
     *
     * @return the user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public LoginResponse getLogin(LoginRequest request) {
        User currentUser = serverFacade.findUser(request.getAlias());

        if(currentUser == null){
            return new LoginResponse(false, null);
        }
        setCurrentUser(currentUser);
        return new LoginResponse(true, currentUser);
    }
}
