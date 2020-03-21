package edu.byu.cs.tweeter.shared.model.service.response;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest}.
 */
public class FollowResponse {

    public boolean follows;
    public String user1;
    public String user2;

    public FollowResponse() {
    }

    public FollowResponse(boolean follows, String user1, String user2) {
        this.follows = follows;
        this.user1 = user1;
        this.user2 = user2;
    }

    public boolean isFollows() {
        return follows;
    }

    public void setFollows(boolean follows) {
        this.follows = follows;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

//    public List<String> getUsers(){
//        return Arrays.asList(user1, user2);
//    }
}
