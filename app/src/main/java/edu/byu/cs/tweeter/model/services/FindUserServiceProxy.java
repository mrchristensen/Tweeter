package edu.byu.cs.tweeter.model.services;

import java.io.IOException;

import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.shared.model.service.FindUserService;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FindUserServiceProxy implements FindUserService {

    /**
     * The singleton instance.
     */
    private static FindUserServiceProxy instance;

    private final ServerFacade serverFacade;

    private static final String URL_PATH = "/getuser";

    /**
     * Return the singleton instance of this class.
     *
     * @return the instance.
     */
    public static FindUserServiceProxy getInstance() {
        if(instance == null) {
            instance = new FindUserServiceProxy();
        }

        return instance;
    }

    /**
     * A private constructor created to ensure that this class is a singleton (i.e. that it
     * cannot be instantiated by external classes).
     */
    private FindUserServiceProxy() {
        serverFacade = new ServerFacade();
    }



    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the followees from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    @Override
    public FindUserResponse findUser(FindUserRequest request) throws IOException {
        return serverFacade.findUser(URL_PATH + "/" + request.getUserAlias());
    }
}
