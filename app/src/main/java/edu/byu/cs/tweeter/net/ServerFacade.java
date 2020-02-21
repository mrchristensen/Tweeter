package edu.byu.cs.tweeter.net;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.FollowerRequest;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.net.response.FollowerResponse;
import edu.byu.cs.tweeter.net.response.FollowingResponse;
import edu.byu.cs.tweeter.net.response.StoryResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {
    private static final String LOG_TAG = "ServerFacade";

    private static List<User> allUsers = new ArrayList<>();
    private static Map<User, List<User>> followeesByFollower; //og
    private static Map<User, List<User>> followersByFollowee;
    private static Map<User, List<Status>> statusesByUser;
    private static User currentUser;


    public User findUser(String userAlias){
        //Hardcoded user:
        User dummyUser = new User("Test", "User", "@test", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        if(!allUsers.contains(dummyUser)){
            allUsers.add(dummyUser);
        }

        for (User user : allUsers) {
            if(user.getAlias().equals(userAlias)){
                return user;
            }
        }
        return null;
    }

    public void postStatus(User user, String statusMessage){
        statusesByUser.get(user).add(new Status(user, LocalDateTime.now(), statusMessage));
    }

    public User registerUser(RegisterRequest request){

        if(findUser(request.getAlias()) != null){
            return null; //There already exists such a user
        }

        User newUser = new User(request.getFistName(), request.getLastName(), "@" + request.getAlias(), request.getProfileImageURL());

        if(!allUsers.contains(newUser)){
            allUsers.add(newUser);
        }
        else{
            return null; //There already exists such a user
        }

        //dfd

        return newUser;
    }


    //
    public boolean userFollows(User follower, User followee){
        if(followeesByFollower.get(follower).contains(followee)){
            return true;
        }
        return false;
    }

    public void removeFollowing(User followee, User follower){
        followeesByFollower.get(follower).remove(followee);
        Log.i("test","test");
    }

    public void addFollowing(User followee, User follower){
        followeesByFollower.get(follower).add(followee);
        Log.i("test","test");
    }



    //Following

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollower() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            initializeFollowees();
        }

        List<User> allFollowees = followeesByFollower.get(request.getFollower());
        Collections.sort(allFollowees);
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the last followee that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Generates the followee data.
     */
    private void initializeFollowees() {

        Map<User, List<User>> followeesByFollower = new HashMap<>();

        List<Follow> follows = getFollowGenerator().generateUsersAndFollowsAndFollowers(100,
                0, 50, currentUser); //todo: Find current user

        // Populate a map of followees, keyed by follower so we can easily handle followee requests
        for(Follow follow : follows) {
            List<User> followees = followeesByFollower.get(follow.getFollower());

            if(followees == null) {
                followees = new ArrayList<>();
                followeesByFollower.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }

        registerUsers(followeesByFollower);

        if(ServerFacade.followeesByFollower == null){
            ServerFacade.followeesByFollower = new HashMap<>();
        }

        ServerFacade.followeesByFollower.putAll(followeesByFollower);

        Map<User, List<User>> followersByFollowee = new HashMap<>();

        // Populate a map of followers, keyed by followee so we can easily handle follower requests
        for(Follow follow : follows) {
            Log.d(LOG_TAG, "Followee (person being followed): " + follow.getFollowee().getFirstName()
                    + " " + follow.getFollowee().getLastName() + " - Follower: " + follow.getFollower().getFirstName()
                    + " " + follow.getFollower().getLastName());

            List<User> followers = followersByFollowee.get(follow.getFollowee());

            if(followers == null) {
                followers = new ArrayList<>();
                followersByFollowee.put(follow.getFollowee(), followers);
            }

            followers.add(follow.getFollower());
        }

        registerUsers(followersByFollowee);
        ServerFacade.followersByFollowee = followersByFollowee;
    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();
    }

    //Followers

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public FollowerResponse getFollowers(FollowerRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollowee() == null) {
                throw new AssertionError();
            }
        }

//        if(followersByFollowee == null) {
//            initializeFollowees();
//        }
//
//        List<User> allFollowers = followersByFollowee.get(request.getFollowee());
//        List<User> responseFollowers = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowers != null) {
//                int followersIndex = getFollowersStartingIndex(request.getLastFollower(), allFollowers);
//
//                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
//                    responseFollowers.add(allFollowers.get(followersIndex));
//                }
//
//                hasMorePages = followersIndex < allFollowers.size();
//            }
//        }

        if(followeesByFollower == null) {
            initializeFollowees();
        }

        List<User> allFollowers = new ArrayList<>();
        for (User follower : followeesByFollower.keySet()) {
            for (User followee : followeesByFollower.get(follower)) {
                if(followee.equals(request.getFollowee())){
                    if(!allFollowers.contains(follower)) {
                        allFollowers.add(follower);
                    }
                    break;
                }
            }
        }
        Collections.sort(allFollowers);

        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followersIndex = getFollowersStartingIndex(request.getLastFollower(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    /**
     * Determines the index for the first follower in the specified 'allFollowers' list that should
     * be returned in the current request. This will be the index of the next follower after the
     * specified 'lastFollower'.
     *
     * @param lastFollower the last follower that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowers the generated list of followers from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFollowersStartingIndex(User lastFollower, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFollower != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollower.equals(allFollowers.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                }
            }
        }

        return followersIndex;
    }

    //TODO Clean up this codes comments
    /**
     * Generates the follower data.
     */
//    private Map<User, List<User>> initializeFollowers() {
//
//        Map<User, List<User>> followersByFollowee = new HashMap<>();
//
//        List<Follow> follows = getFollowGenerator().generateUsersAndFollowers(100,
//                0, 50, FollowGenerator.Sort.FOLLOWEE_FOLLOWER);
//
//        // Populate a map of followers, keyed by followee so we can easily handle follower requests
//        for(Follow follow : follows) {
//            Log.d(LOG_TAG, "Followee (person being followed): " + follow.getFollowee().getFirstName()
//                    + " " + follow.getFollowee().getLastName() + " - Follower: " + follow.getFollower().getFirstName()
//                    + " " + follow.getFollower().getLastName());
//
//            List<User> followers = followersByFollowee.get(follow.getFollowee());
//
//            if(followers == null) {
//                followers = new ArrayList<>();
//                followersByFollowee.put(follow.getFollowee(), followers);
//            }
//
//            followers.add(follow.getFollower());
//        }
//
//        registerUsers(followersByFollowee);
//        return followersByFollowee;
//    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFolleeGenerator() {
        return FollowGenerator.getInstance();
    }

    private void registerUsers(Map<User, List<User>> map) {
        for (User user : map.keySet()) {
            if(!allUsers.contains(user)){
                allUsers.add(user);
            }
        }
        for (List<User> users : map.values()) {
            for (User user : users) {
                if(!allUsers.contains(user)){
                    allUsers.add(user);
                }
            }
        }
    }

    //Statuses

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public StoryResponse getStory(StoryRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUser() == null) {
                throw new AssertionError();
            }
        }

        //TODO from here down
        if(statusesByUser == null){
            statusesByUser = new HashMap<>();
        }

        if(statusesByUser.get(request.getUser()) == null) {
            statusesByUser.putAll(initializeStatuses(request.getUser()));
        }

        List<Status> allStatuses = statusesByUser.get(request.getUser());
        if (allStatuses != null) {
            Collections.sort(allStatuses);
        }
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusesIndex = getStatusesStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusesIndex));
                }

                hasMorePages = statusesIndex < allStatuses.size();
            }
        }

        return new StoryResponse(responseStatuses, hasMorePages);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FeedResponse getFeed(FeedRequest request) {

        // Used in place of assert statements because Android does not support them
        if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUser() == null) {
                throw new AssertionError();
            }
        }

        //Find all the people the user follows (find the followees of the user)
        if(followeesByFollower == null) {
            initializeFollowees();
        }
        List<User> allFollowees = followeesByFollower.get(request.getUser());

        if(allFollowees == null){ //For generating fake data for the second user
            initializeFollowees();
            allFollowees = followeesByFollower.get(request.getUser());
        }

        Log.i(LOG_TAG, request.getUser().toString());

        //Find all the followee's statuses
        if(statusesByUser == null){
            statusesByUser = new HashMap<>();
        }
        List<Status> allStatuses = new ArrayList<>();
        for (User user : allFollowees) {
            if (statusesByUser.get(user) == null) {
                statusesByUser.putAll(initializeStatuses(user));
            }

            allStatuses.addAll(Objects.requireNonNull(statusesByUser.get(user)));
        }




        if (allStatuses != null) {
            Collections.sort(allStatuses);
        }
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusesIndex = getStatusesStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusesIndex));
                }

                hasMorePages = statusesIndex < allStatuses.size();
            }
        }

        return new FeedResponse(responseStatuses, hasMorePages);
    }

    /**
     * Determines the index for the first status in the specified 'allStatuses' list that should
     * be returned in the current request. This will be the index of the next status after the
     * specified 'lastStatus'.
     *
     * @param lastStatus the last status that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allStatuses the generated list of statuses from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getStatusesStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int statusIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                }
            }
        }

        return statusIndex;
    }

    /**
     * Generates the followee data.
     */
    private Map<User, List<Status>> initializeStatuses(User user) {

        Map<User, List<Status>> statusesByUser = new HashMap<>();

        List<Status> statuses = getStatusGenerator().generateStatuses(20, 45, user);
        statusesByUser.put(user, statuses);

        return statusesByUser;
    }

    public static void setCurrentUser(User currentUser) {
        ServerFacade.currentUser = currentUser;
    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    StatusGenerator getStatusGenerator() {
        return StatusGenerator.getInstance();
    }
}

