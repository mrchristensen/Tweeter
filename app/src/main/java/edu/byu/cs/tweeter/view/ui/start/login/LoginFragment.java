package edu.byu.cs.tweeter.view.ui.start.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FollowingRequest;
import edu.byu.cs.tweeter.net.request.LoginRequest;
import edu.byu.cs.tweeter.net.response.LoginResponse;
import edu.byu.cs.tweeter.presenter.LoginPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowingTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetLoginTask;
import edu.byu.cs.tweeter.view.ui.following.FollowingFragment;
import edu.byu.cs.tweeter.view.ui.main.MainActivity;
import edu.byu.cs.tweeter.view.ui.start.StartActivity;
import edu.byu.cs.tweeter.view.ui.storyView.StoryViewActivity;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * The fragment that displays on the 'Following' tab.
 */
public class LoginFragment extends Fragment implements LoginPresenter.View {
    private static final String LOG_TAG = "LoginFragment";

    private LoginPresenter presenter;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        presenter = new LoginPresenter(this);

        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);

        usernameEditText.addTextChangedListener(new FieldWatcher());
        passwordEditText.addTextChangedListener(new FieldWatcher());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Login");

                new LoginViewAdapter().loginCheck(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        return view;
    }

    //TODO: move here down into presenter?
    private class FieldWatcher implements TextWatcher {
        FieldWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(LOG_TAG, "Text changed");
            checkTextFields();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private void checkTextFields() {
        //Check login requirements
        if (!usernameEditText.getText().toString().equals("") &&
                !passwordEditText.getText().toString().equals("")) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    private class LoginViewAdapter implements GetLoginTask.GetLoginObserver {
        void loginCheck(String userAlias, String userPassword){
            GetLoginTask getLoginTask = new GetLoginTask(presenter, this);

            LoginRequest request = new LoginRequest(userAlias, userPassword);
            getLoginTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param loginResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void loginRetrieved(LoginResponse loginResponse) {
            if(loginResponse.loginSuccessful()){
                ((StartActivity) getActivity()).startMainActivity(getView(), loginResponse.getCurrentUser());
            }
            else{
                Snackbar.make(getView(), "Invalid Login", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}
