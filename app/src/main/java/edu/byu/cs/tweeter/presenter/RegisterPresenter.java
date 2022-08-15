package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.services.RegisterServiceProxy;
import edu.byu.cs.tweeter.shared.model.service.RegisterService;
import edu.byu.cs.tweeter.shared.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.shared.model.service.response.RegisterResponse;

/**
 * The presenter for the "following" functionality of the application.
 */
public class RegisterPresenter extends Presenter {

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
    public RegisterPresenter(View view) {
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
    public RegisterResponse doRegister(RegisterRequest request) throws IOException {
        RegisterService service = new RegisterServiceProxy();
        return service.doRegister(request);
    }
}
