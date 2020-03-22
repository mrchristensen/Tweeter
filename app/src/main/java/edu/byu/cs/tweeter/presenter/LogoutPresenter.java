package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.services.LoginServiceProxy;
import edu.byu.cs.tweeter.model.services.LogoutServiceProxy;
import edu.byu.cs.tweeter.shared.model.service.LoginService;
import edu.byu.cs.tweeter.shared.model.service.LogoutService;
import edu.byu.cs.tweeter.shared.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.shared.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.shared.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.shared.model.service.response.LogoutResponse;

/**
 * The presenter for the "following" functionality of the application.
 */
public class LogoutPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LogoutPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public LogoutResponse doLogout(LogoutRequest request) throws IOException {
        LogoutService service = new LogoutServiceProxy();
        return service.doLogout(request);
    }
}
