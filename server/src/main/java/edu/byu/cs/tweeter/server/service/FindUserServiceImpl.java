package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.FindUserService;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FindUserServiceImpl implements FindUserService {
    @Override
    public FindUserResponse findUser(FindUserRequest request) {
        request.setUserAlias(request.getUserAlias().replaceAll("@", "")); //sanitize input (no "@"'s aloud in user aliases)
        UserDAO userDAO = new UserDAO();
        User foundUser = userDAO.getUser(request.getUserAlias());
        if(foundUser == null){
            return new FindUserResponse(false, request.getUserAlias());
        }
        else {
            return new FindUserResponse(true, foundUser);
        }
    }
}
