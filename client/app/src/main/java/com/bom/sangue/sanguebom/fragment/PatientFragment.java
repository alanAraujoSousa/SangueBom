package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by alan on 05/12/15.
 */
public class PatientFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list_emergency, container, false);
        fillPatientList();
        return rootView;
    }

    private void fillPatientList() {
        try {
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL_LIST_PATIENT,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONArray patientsData = jsonArray.getJSONArray(1);
                                for (int i=0; i<patientsData.length(); i++) {
                                    JSONObject item = patientsData.getJSONObject(i);
                                    String name = item.getString("name");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
