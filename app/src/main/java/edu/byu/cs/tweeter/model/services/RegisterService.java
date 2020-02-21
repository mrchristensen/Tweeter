package edu.byu.cs.tweeter.model.services;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.net.response.RegisterResponse;

/**
 * Contains the business logic for login and sign up.
 */
public class RegisterService {

    /**
     * The singleton instance.
     */
    private static RegisterService instance;

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
    public static RegisterService getInstance() {
        if(instance == null) {
            instance = new RegisterService();
        }

        return instance;
    }

    /**
     * A private constructor created to ensure that this class is a singleton (i.e. that it
     * cannot be instantiated by external classes).
     */
    private RegisterService() {
        serverFacade = new ServerFacade();


        // TODO: Remove when the actual login functionality exists.
//        currentUser = new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//        setCurrentUser(currentUser);
    }

    /**
     * Returns the currently logged in user.
     *
     * @return the user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        LoginService.getInstance().setCurrentUser(currentUser);
    }

    public RegisterResponse getRegister(RegisterRequest request) {
        User currentUser = serverFacade.registerUser(request); //Create a new user

        if(currentUser == null){ //Such a user already exists (username taken)
            return new RegisterResponse(false, null);
        }
        setCurrentUser(currentUser); //No such user already exited, return the created user
        return new RegisterResponse(true, currentUser);
    }
}
