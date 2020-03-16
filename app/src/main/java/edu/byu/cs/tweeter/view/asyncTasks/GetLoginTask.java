package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;

/**
 * An {@link AsyncTask} for retrieving followews for a user.
 */
public class GetLoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

    private final LoginPresenter presenter;
    private final GetLoginObserver observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface GetLoginObserver {
        void loginRetrieved(LoginResponse loginResponse);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followews.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetLoginTask(LoginPresenter presenter, GetLoginObserver observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followews.
     *
     * @param loginRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected LoginResponse doInBackground(LoginRequest... loginRequests) {
        LoginResponse response = null;
        try {
            response = presenter.getLogin(loginRequests[0]);
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param loginResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(LoginResponse loginResponse) {

        if(observer != null) {
            observer.loginRetrieved(loginResponse);
        }
    }
}
