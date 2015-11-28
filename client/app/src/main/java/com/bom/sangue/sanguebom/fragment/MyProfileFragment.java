package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.persistence.bean.User;
import com.bom.sangue.sanguebom.persistence.dao.UserDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.util.concurrent.Future;


/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

    private static final String URL_SIGNUP = "/user";
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isUserRegistered()) {
            if (hasToken()) {
                rootView = inflater.inflate(R.layout.my_profile_layout, container, false);
            } else {
                rootView = inflater.inflate(R.layout.login, container, false);
                Button signButton = (Button) rootView.findViewById(R.id.sign_button);
                signButton.setOnClickListener(mSignUserListener);
            }
        } else {
            rootView = inflater.inflate(R.layout.signup, container, false);
            ArrayAdapter<CharSequence> adapter =
                    ArrayAdapter.createFromResource(getActivity()
                            , R.array.categoria_sangue,
                            android.R.layout.simple_spinner_item);

            Spinner categoria = (Spinner) rootView.findViewById(R.id.signup_blood_type);
            categoria.setAdapter(adapter);
            ImageButton signupButton = (ImageButton) rootView.findViewById(R.id.signup_button);
            signupButton.setOnClickListener(mSignUpUserListener);
        }
        return rootView;
    }

    private View.OnClickListener mSignUpUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String login = ((EditText) rootView.findViewById(R.id.signup_login)).getText().toString();
            final String password = ((EditText) rootView.findViewById(R.id.signup_password)).getText().toString();
            final String email = ((EditText) rootView.findViewById(R.id.signup_email)).getText().toString();

            // TODO Validade data.
            // TODO create a user to send.

            Gson gson = new Gson();
            JsonObject user = new JsonObject();
            JsonObject profile = new JsonObject();
            user.addProperty("username", login);
            user.addProperty("password", password);
            user.addProperty("email", email);
            user.add("userProfile", profile);

            /*DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.signup_birth_date);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            // TODO Validate the birthDate only +18 !

            Date birthDate = new Date();
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(year, month, day, 0, 0, 0);
            String date = cal.getTime().getTime()
            */

            profile.addProperty("birth_day", "10-20-1993"); // FIXME

            Spinner spinner = (Spinner) rootView.findViewById(R.id.signup_blood_type);
            String bloodType = spinner.getSelectedItem().toString();

            profile.addProperty("blood_type", bloodType);

            AsyncHttpClient client = new AsyncHttpClient();
            client.preparePost(URL_SIGNUP).setBody(user.toString())
                    .execute(new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {
                            int statusCode = response.getStatusCode();
                            switch (statusCode) {
                                case 204:
                                    String token = response.getResponseBody();
                                    registeUser(new User(login,password,email,token));
                                    break;
                            }
                            return null;
                        }
                    });
        }
    };

    private void registeUser(User user) {
        UserDAO userDAO = UserDAO.getInstance(getContext());
        userDAO.save(user);
        userDAO.closeConnection();
    }

    private View.OnClickListener mSignUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText loginBox = (EditText) rootView.findViewById(R.id.sign_login);
            EditText passwordBox = (EditText) rootView.findViewById(R.id.sign_password);

        }
    };

    private boolean isUserRegistered() {
        UserDAO userDAO = UserDAO.getInstance(getContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user != null)
            return true;
        return false;
    }

    private boolean hasToken() {
        UserDAO userDAO = UserDAO.getInstance(getContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user != null && user.getToken() != null && !user.getToken().isEmpty())
            return true;
        return false;
    }
}
