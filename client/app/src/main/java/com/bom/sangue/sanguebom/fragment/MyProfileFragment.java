package com.bom.sangue.sanguebom.fragment;

import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.persistence.bean.User;
import com.bom.sangue.sanguebom.persistence.dao.UserDAO;
import com.bom.sangue.sanguebom.provider.DBProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.Future;


/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

    private static final String URL_SIGNUP = "";
    View rootView;

    @Override
    public void onResume() {

    }

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
            Button signupButton = (Button) rootView.findViewById(R.id.signup_button);
            signupButton.setOnClickListener(mSignUpUserListener);
        }
        return rootView;
    }

    private View.OnClickListener mSignUpUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText loginBox = (EditText) rootView.findViewById(R.id.signup_login);
            EditText passwordBox = (EditText) rootView.findViewById(R.id.signup_password);
            EditText emailBox = (EditText) rootView.findViewById(R.id.signup_email);

            // TODO Validade data.
            // TODO create a user to send.

            Gson gson = new Gson();
            JsonObject user = new JsonObject();
            JsonObject profile = new JsonObject();
            user.addProperty("username", "");
            user.addProperty("password", "");
            user.addProperty("email", "");
            user.add("userProfile", profile);

            DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.signup_birth_date);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            // TODO Validate the birthDate only +18 !

            Date birthDate = new Date();
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(year, month, day, 0, 0, 0);

            profile.addProperty("birth_day", cal.getTime().getTime());

            RadioGroup rG = (RadioGroup) rootView.findViewById(R.id.signup_blood_type);

            Integer radioID = rG.getCheckedRadioButtonId();
            String bloodType = "";
            switch (radioID) {
                case R.id.signup_blood_o_plus:
                    bloodType = "o+";
                    break;
                case R.id.signup_blood_o_minus:
                    bloodType = "o-";
                    break;
            }
            profile.addProperty("blood_type", bloodType);

            AsyncHttpClient client = new AsyncHttpClient();
            Future<Integer> f = client.preparePost(URL_SIGNUP).setBody(user.toString())
                    .execute(new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {

                            return response.getStatusCode();
                        }
                    });
        }
    };

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
        if (user!=null)
                return true;
        return false;
    }

    private boolean hasToken() {
        UserDAO userDAO = UserDAO.getInstance(getContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user!=null && user.getToken() != null && !user.getToken().isEmpty())
            return true;
        return false;
    }
}
