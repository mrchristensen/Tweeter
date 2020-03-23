package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.presenter.FindUserPresenter;
import edu.byu.cs.tweeter.shared.model.domain.User;
import edu.byu.cs.tweeter.shared.model.service.request.FindUserRequest;
import edu.byu.cs.tweeter.shared.model.service.response.FindUserResponse;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * An {@link AsyncTask} for loading images from a set of URLs.
 */
public class FindUserTask extends AsyncTask<FindUserRequest, Integer, FindUserResponse> {

    private final FindUserObserver observer;
    private final FindUserPresenter presenter;


    private Exception exception;


    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface FindUserObserver {
        void getUser(FindUserResponse response);
    }

    /**
     * Creates an instance.
     *
     * @param observer the observer who wants to be notified when this task completes.
     */
    public FindUserTask(FindUserPresenter presenter, FindUserObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve images.
     *
     * @param requests the urls from which images should be retrieved.
     * @return the images.
     */
    @Override
    protected FindUserResponse doInBackground(FindUserRequest... requests) {
        FindUserResponse response = null;
        try {
            response = presenter.findUser(requests[0]);
            if (response.isSuccessful()){
                loadImages(response);
            }
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
    private void loadImages(FindUserResponse response) {
        User user = response.getUser();

        Drawable drawable;

        try {
            drawable = ImageUtils.drawableFromUrl(user.getImageUrl());
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString(), e);
            drawable = null;
        }

        ImageCache.getInstance().cacheImage(user, drawable);

    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param response the images that were retrieved by the task.
     */
    @Override
    protected void onPostExecute(FindUserResponse response) {

        if(observer != null) {
            observer.getUser(response);
        }
    }
}
