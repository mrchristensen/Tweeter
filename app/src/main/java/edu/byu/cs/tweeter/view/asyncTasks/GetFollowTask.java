package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;


import edu.byu.cs.tweeter.presenter.StoryViewPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FollowResponse;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class GetFollowTask extends AsyncTask<FollowRequest, Void, FollowResponse> {

    private final StoryViewPresenter presenter;
    private final GetFollowObserver observer;

    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface GetFollowObserver {
        void followRetrieved(FollowResponse followResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowTask(StoryViewPresenter presenter, GetFollowObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param followRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowResponse doInBackground(FollowRequest... followRequests) {
        FollowResponse response = null;
        try {
            response = presenter.getFollow(followRequests[0]);
//            loadImages(response); todo is this needed?
        } catch (IOException e) {
            exception = e;

            e.printStackTrace();
        }
        return response;
    }

//    /**
//     * Loads the image associated with each follower included in the response.
//     *
//     * @param response the response from the follower request.
//     */
//    private void loadImages(FollowResponse response) {
//        for(User user : response.getUsers()) {
//
//            Drawable drawable;
//
//            try {
//                drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
//            } catch (IOException e) {
//                Log.e(this.getClass().getName(), e.toString(), e);
//                drawable = null;
//            }
//
//            ImageCache.getInstance().cacheImage(user, drawable);
//        }
//    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowResponse followResponse) {
        if(observer != null) {
            observer.followRetrieved(followResponse);
        }
    }
}
