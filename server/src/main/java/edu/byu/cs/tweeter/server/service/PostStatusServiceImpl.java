package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.shared.model.service.PostStatusService;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;

import static edu.byu.cs.tweeter.server.service.AuthTokenService.validateAuthToken;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class PostStatusServiceImpl implements PostStatusService {
    @Override
    public PostStatusResponse postStatus(PostStatusRequest request) {
        System.out.println("Authtoken String: " + request.getAuthTokenString());

        if(request.getStatus() == null){
            System.out.println("Request status is null");
        }
        System.out.println("Status Message: " + request.getStatus().getMessageBody());

        if(validateAuthToken(request.getCurrentUserAlias(), request.getAuthTokenString())){
            StatusDAO statusDAO = new StatusDAO();
            statusDAO.putStatus(request.getStatus());

            return new PostStatusResponse(request.getStatus());
        }

        //todo update feeds
        else{
            throw new RuntimeException("forbidden");
        }
    }
}
