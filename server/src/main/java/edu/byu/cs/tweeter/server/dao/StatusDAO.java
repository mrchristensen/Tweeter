package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.generators.StatusGenerator;
import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class StatusDAO {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        //TODO: Implement actual functionality once Databases are implemented
        return new PostStatusResponse(request.getStatus());
    }

    public FeedResponse getFeed(FeedRequest request) {
        //TODO: Implement actual functionality once Databases are implemented
        String FEMALE_IMAGE_URL = "https://raw.githubusercontent.com/mrchristensen/Tweeter/master/server/src/main/java/edu/byu/cs/tweeter/server/resources/daisy.png?token=ALDCLZTBHGVXW27AWFPQNTC6QF36C";
        return new FeedResponse(StatusGenerator.getNStatuses(request.getLimit(), new User("fname1", "lname1", "@tempAlias1", FEMALE_IMAGE_URL)), true);
    }

    public StoryResponse getStory(StoryRequest request) {
        //TODO: Implement actual functionality once Databases are implemented
        return new StoryResponse(StatusGenerator.getNStatuses(request.getLimit(), request.getUser()), true);
    }

}