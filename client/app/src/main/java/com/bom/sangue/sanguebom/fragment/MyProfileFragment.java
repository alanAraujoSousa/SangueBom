package com.bom.sangue.sanguebom.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bom.sangue.sanguebom.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.util.concurrent.Future;


/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

    private static final String URL_SIGNUP = "";
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isUserRegistered()) {
            rootView = inflater.inflate(R.layout.signup, container, false);
            Button signupButton = (Button) rootView.findViewById(R.id.signup_button);
            signupButton.setOnClickListener(mSignUpUserListener);
        } else {
            if (hasToken()) {
                rootView = inflater.inflate(R.layout.my_profile_layout, container, false);
            } else {
                rootView = inflater.inflate(R.layout.login, container, false);
                Button signButton = (Button) rootView.findViewById(R.id.sign_button);
                signButton.setOnClickListener(mSignUserListener);
            }
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
            profile.addProperty("blood_type", "");
            profile.addProperty("birth_day", "");


            AsyncHttpClient client = new AsyncHttpClient();
            Future<Integer> f = client.preparePost(URL_SIGNUP).setBody(user.toString())
                    .execute(new AsyncCompletionHandler<Integer>() {
                @Override
                public Integer onCompleted(Response response) throws Exception {

                    return response.getStatusCode();
                }
            });


//            HttpClient httpclient = new DefaultHttpClient();

        }
    };

    private View.OnClickListener mSignUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText loginBox = (EditText) rootView.findViewById(R.id.sign_login);
            EditText passwordBox = (EditText) rootView.findViewById(R.id.sign_password);

        }
    };

    private boolean isUserRegistered() {
        return false;
    }

    private boolean hasToken() {
        return false;
    }
}
