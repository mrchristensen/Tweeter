package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.server.dao.LogoutDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.LogoutService;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse doLogout(LogoutRequest request) {
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        authTokenDAO.invalidateAuthToken(new AuthToken(request.getAuthTokenString()));

        LogoutDAO logoutDAO = new LogoutDAO();
        return logoutDAO.doLogout(request);
    }
}
