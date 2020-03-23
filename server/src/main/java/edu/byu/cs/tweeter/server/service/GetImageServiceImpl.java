package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.shared.model.service.GetImageService;
import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;
import edu.byu.cs.tweeter.shared.model.service.response.GetImageResponse;

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
            throw new RuntimeException("server error");
        }
    }
}
