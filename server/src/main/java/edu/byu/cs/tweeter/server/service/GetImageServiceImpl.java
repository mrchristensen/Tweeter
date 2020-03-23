package edu.byu.cs.tweeter.server.service;

import java.nio.charset.StandardCharsets;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.LoginDAO;
import edu.byu.cs.tweeter.shared.model.domain.AuthToken;
import edu.byu.cs.tweeter.shared.model.service.GetImageService;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.GetImageResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class GetImageServiceImpl implements GetImageService {
    @Override
    public GetImageResponse getImage(GetImageRequest request) {
        ImageDAO imageDAO = new ImageDAO();
        try {
            return new GetImageResponse(imageDAO.getImage(request));
        } catch (Exception e) {
            e.printStackTrace();
            return new GetImageResponse(null);
        }
    }
}
