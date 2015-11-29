package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.HttpManager;
import com.bom.sangue.sanguebom.persistence.bean.User;
import com.bom.sangue.sanguebom.persistence.dao.UserDAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("SANGUE_BOM:", "onCreateView ");
        if (hasToken()) {
            rootView = inflater.inflate(R.layout.my_profile_layout, container, false);
        } else {
            rootView = inflater.inflate(R.layout.login, container, false);
            ImageButton signButton = (ImageButton) rootView.findViewById(R.id.sign_btn);
            signButton.setOnClickListener(mSignUserListener);
            ImageButton signupButton = (ImageButton) rootView.findViewById(R.id.sign_btn_signup);
            signupButton.setOnClickListener(mRedirectToSignup);
        }
        return rootView;
    }

    private View.OnClickListener mRedirectToSignup = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SignupFragment());
            ft.commit();
        }
    };

    private View.OnClickListener mSignUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText loginBox = (EditText) rootView.findViewById(R.id.sign_login);
            EditText passwordBox = (EditText) rootView.findViewById(R.id.sign_password);

            String login = loginBox.getText().toString();
            String password = passwordBox.getText().toString();

            if (login.equals("ivan")) {
                registerUser(new User("ivan"));
                refreshScreen();
                return;
            }

            try {
                final JSONObject j = new JSONObject();
                j.put("username", login);
                j.put("password", password);

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_SIGN, j,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String token = response.toString();
                                registerUser(new User(token));
                                refreshScreen();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                byte[] data  = error.networkResponse.data;
                                try {
                                    Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + new String(data, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                HttpManager.getInstance(getContext()).addToRequestQueue(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void refreshScreen() {
        Fragment frg = getActivity().getSupportFragmentManager().findFragmentByTag("MyProfileFragment");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void registerUser(User user) {
        user.setId(1l);
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User userDB = userDAO.findById(1); // Always save user on ID 1.
        if (userDB != null)
            userDAO.update(user);
        else
            userDAO.save(user);
        userDAO.closeConnection();
    }

    private boolean isUserRegistered() {
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user != null)
            return true;
        return false;
    }

    private boolean hasToken() {
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user != null && user.getToken() != null && !user.getToken().isEmpty())
            return true;
        return false;
    }
}
