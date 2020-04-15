package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.shared.model.service.request.UpdateFeedsRequest;

public class UpdateFeedsService {

    public void updateFeeds(UpdateFeedsRequest request){
        System.out.println("updateFeedsRequest received: " + request);

        FeedDAO feedDAO = new FeedDAO();
        feedDAO.feedStatusBatchWrite(request.getStatus(), request.getFollowerAliases());
    }

}
