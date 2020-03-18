package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.Status;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;

/**
 * A DAO for accessing 'follower' data from the database.
 */
public class FeedDAO {

    public FeedResponse getFeed(FeedRequest request){
        return new FeedResponse(getNStatuses(request.getLimit(), request.getUser()), true);
    }

    private List<Status> getNStatuses(int n, User user){
        List<Status> statuses = new ArrayList<>();
        for(int i=0; i < n; i++){
            statuses.add(new Status(user, LocalDateTime.now().toString(),"Fake tweet " + i)); //todo clean up
//            statuses.add(new Status(user, System.currentTimeMillis(),"Fake tweet " + i));
        }
        return statuses;
    }

}