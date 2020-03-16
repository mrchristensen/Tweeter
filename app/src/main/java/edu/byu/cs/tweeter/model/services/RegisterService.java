//package edu.byu.cs.tweeter.model.services;
//
//import edu.byu.cs.tweeter.shared.model.domain.User;
//import edu.byu.cs.tweeter.net.ServerFacade;
//import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
//import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;
//
///**
// * Contains the business logic for login and sign up.
// */
//public class RegisterService {
//
//    /**
//     * The singleton instance.
//     */
//    private static RegisterService instance;
//
//    private final ServerFacade serverFacade;
//
//    /**
//     * The logged in user.
//     */
//    private User currentUser;
//
//    /**
//     * Return the singleton instance of this class.
//     *
//     * @return the instance.
//     */
//    public static RegisterService getInstance() {
//        if(instance == null) {
//            instance = new RegisterService();
//        }
//
//        return instance;
//    }
//
//    /**
//     * A private constructor created to ensure that this class is a singleton (i.e. that it
//     * cannot be instantiated by external classes).
//     */
//    private RegisterService() {
//        serverFacade = new ServerFacade();
//    }
//
//    /**
//     * Returns the currently logged in user.
//     *
//     * @return the user.
//     */
//    public User getCurrentUser() {
//        return currentUser;
//    }
//
//    private void setCurrentUser(User currentUser) {
//        this.currentUser = currentUser;
////        LoginService.getInstance().setCurrentUser(currentUser); todo is the needed
//    }
//
//    public RegisterResponse getRegister(RegisterRequest request) {
//        User currentUser = serverFacade.registerUser(request); //Create a new user
//
//        if(currentUser == null){ //Such a user already exists (username taken)
//            return new RegisterResponse(false, null);
//        }
//        setCurrentUser(currentUser); //No such user already exited, return the created user
//        return new RegisterResponse(true, currentUser);
//    }
//}
