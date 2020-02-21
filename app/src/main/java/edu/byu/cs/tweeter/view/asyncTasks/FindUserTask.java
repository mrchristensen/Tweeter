package edu.byu.cs.tweeter.view.asyncTasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.ServerFacade;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * An {@link AsyncTask} for loading images from a set of URLs.
 */
public class FindUserTask extends AsyncTask<String, Integer, User> {

    private final FindUserObserver observer;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface FindUserObserver {
        void getUser(User user);
    }

    /**
     * Creates an instance.
     *
     * @param observer the observer who wants to be notified when this task completes.
     */
    public FindUserTask(FindUserObserver observer) {
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve images.
     *
     * @param urls the urls from which images should be retrieved.
     * @return the images.
     */
    @Override
    protected User doInBackground(String... aliases) {
        return new ServerFacade().findUser(aliases[0]);
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param user the images that were retrieved by the task.
     */
    @Override
    protected void onPostExecute(User user) {

        if(observer != null) {
            observer.getUser(user);
        }
    }
}
