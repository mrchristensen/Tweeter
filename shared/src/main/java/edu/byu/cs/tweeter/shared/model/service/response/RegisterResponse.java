package edu.byu.cs.tweeter.shared.model.service.response;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest}.
 */
public class RegisterResponse {

    private boolean registerSuccessful;
    private User currentUser;


    public RegisterResponse(boolean registerSuccessful, User currentUser) {
        this.registerSuccessful = registerSuccessful;
        this.currentUser = currentUser;
    }

    //Default constructor
    public RegisterResponse() {
    }

    public boolean isRegisterSuccessful() {
        return registerSuccessful;
    }

    public void setRegisterSuccessful(boolean registerSuccessful) {
        this.registerSuccessful = registerSuccessful;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
