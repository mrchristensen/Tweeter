package edu.byu.cs.tweeter.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.shared.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowersRequest;
import edu.byu.cs.tweeter.shared.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.shared.model.service.request.GetImageRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.request.PostStatusRequest;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowersResponse;
import edu.byu.cs.tweeter.shared.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.shared.model.service.response.GetImageResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.shared.model.service.response.PostStatusResponse;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.shared.model.service.response.StoryResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {
    private static final String LOG_TAG = "ServerFacade";
    private static final String SERVER_URL = "https://SERVER_URL_API.execute-api.us-west-2.amazonaws.com/dev";

    public FindUserResponse findUser(String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doGet(urlPath, null, FindUserResponse.class);

    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, headers, FollowingResponse.class);
    }

    public FollowersResponse getFollowers(FollowersRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, headers, FollowersResponse.class);
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
    public StoryResponse getStory(StoryRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, headers, StoryResponse.class);
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
    public FeedResponse getFeed(FeedRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, headers, FeedResponse.class);
    }

    public LoginResponse getLogin(LoginRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    public RegisterResponse getRegister(RegisterRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
    }

    public LogoutResponse doLogout(LogoutRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, headers, LogoutResponse.class);
    }

    public FollowResponse getFollow(FollowRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doGet(urlPath, headers, FollowResponse.class);
    }

    public FollowResponse removeFollow(FollowRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doDelete(urlPath, headers, FollowResponse.class);
    }

    public FollowResponse addFollow(FollowRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPut(urlPath, headers, FollowResponse.class);
    }

    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath) throws IOException {
        Map<String, String> headers = new HashMap<>();
        addJsonHeaders(headers);
        addAuthTokenHeaders(headers);

        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, headers, PostStatusResponse.class);
    }

    public GetImageResponse getImage(GetImageRequest request, String urlPath) throws IOException {
        ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
        return clientCommunicator.doPost(urlPath, request, null, GetImageResponse.class);
    }

    private void addJsonHeaders(Map<String, String> headers) {
        headers.put("Content-Type", "application/json");
    }

    private void addAuthTokenHeaders(Map<String, String> headers) {
        headers.put("AuthToken", SessionCache.getInstance().getAuthTokenString());
    }
}

