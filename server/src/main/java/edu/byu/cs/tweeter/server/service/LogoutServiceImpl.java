package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.shared.model.service.LogoutService;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * Contains the business logic for logging out a user.
 */
public class LogoutServiceImpl implements LogoutService {
    @Override
    public LogoutResponse doLogout(LogoutRequest request) {
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        return new LogoutResponse(authTokenDAO.deleteAuthToken(request.getAuthTokenString()), request.getCurrentUserAlias());
    }
}
