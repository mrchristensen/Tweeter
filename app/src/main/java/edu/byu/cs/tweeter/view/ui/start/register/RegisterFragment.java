package edu.byu.cs.tweeter.view.ui.start.register;

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

import com.google.android.material.snackbar.Snackbar;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.net.request.RegisterRequest;
import edu.byu.cs.tweeter.net.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetRegisterTask;
import edu.byu.cs.tweeter.view.ui.start.startActivity.StartActivity;

/**
 * The fragment that displays on the 'Following' tab.
 */
public class RegisterFragment extends Fragment implements RegisterPresenter.View {
    private static final String LOG_TAG = "RegisterFragment";

    private RegisterPresenter presenter;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText profileImageURLEditText;
    private Button registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        presenter = new RegisterPresenter(this);

        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        firstNameEditText = view.findViewById(R.id.first_name);
        lastNameEditText = view.findViewById(R.id.last_name);
        profileImageURLEditText = view.findViewById(R.id.image_url);
        registerButton = view.findViewById(R.id.register_button);

        usernameEditText.addTextChangedListener(new FieldWatcher());
        passwordEditText.addTextChangedListener(new FieldWatcher());
        firstNameEditText.addTextChangedListener(new FieldWatcher());
        lastNameEditText.addTextChangedListener(new FieldWatcher());
        profileImageURLEditText.addTextChangedListener(new FieldWatcher());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Login");

                new RegisterViewAdapter().registerCheck(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        profileImageURLEditText.getText().toString());
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
        //Check register requirements
        if (!usernameEditText.getText().toString().equals("") &&
                !passwordEditText.getText().toString().equals("") &&
                !firstNameEditText.getText().toString().equals("") &&
                !lastNameEditText.getText().toString().equals("") &&
                !profileImageURLEditText.getText().toString().equals("")) {
            registerButton.setEnabled(true);
        } else {
            registerButton.setEnabled(false);
        }
    }

    private class RegisterViewAdapter implements GetRegisterTask.GetRegisterObserver {
        void registerCheck(String userAlias, String userPassword, String firstName, String lastName, String profileImageURL){
            GetRegisterTask getRegisterTask = new GetRegisterTask(presenter, this);

            RegisterRequest request = new RegisterRequest(userAlias, userPassword, firstName, lastName, profileImageURL);
            getRegisterTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param registerResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void registerRetrieved(RegisterResponse registerResponse) {
            if(registerResponse.registerSuccessful()){
                ((StartActivity) getActivity()).startMainActivity(getView(), registerResponse.getCurrentUser());
            }
            else{
                Snackbar.make(getView(), "Invalid Register", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}
