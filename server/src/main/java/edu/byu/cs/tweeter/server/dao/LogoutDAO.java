package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class LogoutDAO {

    public LogoutResponse doLogout(LogoutRequest request) {
        //Todo make this invalidate the auth token
        //TODO: Remove once database func is in place
        return new LogoutResponse(true, request.getCurrentUser());
    }

}