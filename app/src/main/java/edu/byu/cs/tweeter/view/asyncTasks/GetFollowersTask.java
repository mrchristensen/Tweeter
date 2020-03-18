package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.presenter.FollowerPresenter;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class GetFollowersTask extends AsyncTask<FollowerRequest, Void, FollowerResponse> {

    private final FollowerPresenter presenter;
    private final GetFollowersObserver observer;

    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface GetFollowersObserver {
        void followersRetrieved(FollowerResponse followerResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowersTask(FollowerPresenter presenter, GetFollowersObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param followerRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowerResponse doInBackground(FollowerRequest... followerRequests) {
        FollowerResponse response = null;
        try {
            response = presenter.getFollowers(followerRequests[0]);
            loadImages(response);
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Loads the image associated with each follower included in the response.
     *
     * @param response the response from the follower request.
     */
    private void loadImages(FollowerResponse response) {
        for(User user : response.getFollowers()) {

            Drawable drawable;

            try {
                drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.toString(), e);
                drawable = null;
            }

            ImageCache.getInstance().cacheImage(user, drawable);
        }
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followerResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowerResponse followerResponse) {

        if(observer != null) {
            observer.followersRetrieved(followerResponse);
        }
    }
}
