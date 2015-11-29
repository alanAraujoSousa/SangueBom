package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.HttpManager;
import com.bom.sangue.sanguebom.persistence.bean.User;
import com.bom.sangue.sanguebom.persistence.dao.UserDAO;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

    private static final String HOST = "10.0.2.2";
    private static final String PORT = "8000";
    private static final String URL_SIGNUP = "http://" + HOST+":"+PORT+"/engine/users/";
    private static final String URL_SIGN = "http://"+HOST+":"+PORT+"/api-auth-token/";

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("REFRESH FRAGMENT", "onCreateView ");

        if (isUserRegistered()) {
            if (hasToken()) {
                rootView = inflater.inflate(R.layout.my_profile_layout, container, false);
            } else {
                rootView = inflater.inflate(R.layout.login, container, false);
                ImageButton signButton = (ImageButton) rootView.findViewById(R.id.sign_btn);
                signButton.setOnClickListener(mSignUserListener);
            }
        } else {
            rootView = inflater.inflate(R.layout.signup, container, false);
            ArrayAdapter<CharSequence> adapter =
                    ArrayAdapter.createFromResource(getActivity()
                            , R.array.categoria_sangue,
                            android.R.layout.simple_spinner_item);

            ((Spinner) rootView.findViewById(R.id.signup_blood_type)).setAdapter(adapter);
            ImageButton btn = (ImageButton) rootView.findViewById(R.id.signup_btn);
            btn.setOnClickListener(mSignUpUserListener);
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

           /* DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.signup_birth_date);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            // TODO Validate the birthDate only +18 !

            Date birthDate = new Date();
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(year, month, day, 0, 0, 0);
            String date = cal.getTime().getTime()
*/

            Spinner spinner = (Spinner) rootView.findViewById(R.id.signup_blood_type);
            String bloodType = spinner.getSelectedItem().toString();

            try {
                final JSONObject user = new JSONObject();
                JSONObject profile = new JSONObject();
                user.put("username", login);
                user.put("password", password);
                user.put("email", email);
                user.put("userProfile", profile);
                profile.put("birth_date", "20-11-2002");
                profile.put("blood_type", bloodType);


                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGNUP, user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                refreshScreen();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + error.getMessage());
                            }
                        });

                HttpManager.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

           /* new AsyncHttpClient().preparePost(URL_SIGNUP).setBody(user.toString())
                    .execute(new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {
                            int statusCode = response.getStatusCode();

                            switch (statusCode) {
                                case 201:
//                                  registerUser(new User(login, password, email));
                                    refreshScreen();
                                    break;
                            }
                            return null;
                        }
                    });*/
        }
    };


    private View.OnClickListener mSignUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText loginBox = (EditText) rootView.findViewById(R.id.sign_login);
            EditText passwordBox = (EditText) rootView.findViewById(R.id.sign_password);

            String login = loginBox.getText().toString();
            String password = passwordBox.getText().toString();

            try {
                final JSONObject j = new JSONObject();
                j.put("username", login);
                j.put("password", password);

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_SIGN, j,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            setToken(response.toString());
                            refreshScreen();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                HttpManager.getInstance(getContext()).addToRequestQueue(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*new AsyncHttpClient().preparePost(URL_SIGN).setBody(user.toString())
                    .execute(new AsyncCompletionHandler<Integer>() {
                        @Override
                        public Integer onCompleted(Response response) throws Exception {
                            int statusCode = response.getStatusCode();
                            Log.i("Sign", "onCompleted status:" + statusCode);
                            switch (statusCode) {
                                case 200:
                                    String token = response.getResponseBody();
//                                    setToken(token);
                                    Log.e("Sign", "Token: " + token);
                                    refreshScreen();
                                    break;
                            }
                            return null;
                        }
                    });*/
        }
    };

    private void refreshScreen() {
        Fragment frg = getActivity().getSupportFragmentManager().findFragmentById(0);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void setToken(String token) {
        UserDAO userDAO = UserDAO.getInstance(getContext());
        User user = userDAO.findById(1);
        user.setToken(token);
        userDAO.update(user);
        userDAO.closeConnection();
    }

    private void registerUser(User user) {
        UserDAO userDAO = UserDAO.getInstance(getContext());
        userDAO.save(user);
        userDAO.closeConnection();
    }

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
