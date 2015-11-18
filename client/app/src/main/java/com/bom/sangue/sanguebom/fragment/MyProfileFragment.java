package com.bom.sangue.sanguebom.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bom.sangue.sanguebom.R;

/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

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
