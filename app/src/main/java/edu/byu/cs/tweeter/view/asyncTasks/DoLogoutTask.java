package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class DoLogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> {

    private final LogoutPresenter presenter;
    private final LogoutObserver observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface LogoutObserver {
        void logoutRetrieved(LogoutResponse logoutResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public DoLogoutTask(LogoutPresenter presenter, LogoutObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param logoutRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected LogoutResponse doInBackground(LogoutRequest... logoutRequests) {
        LogoutResponse response = null;
        try {
            response = presenter.doLogout(logoutRequests[0]);
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param logoutResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(LogoutResponse logoutResponse) {

        if(observer != null) {
            observer.logoutRetrieved(logoutResponse);
        }
    }
}
