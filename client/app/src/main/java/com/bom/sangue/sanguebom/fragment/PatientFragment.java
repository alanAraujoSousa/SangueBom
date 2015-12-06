package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.BloodTypeEnum;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.GenderEnum;
import com.bom.sangue.sanguebom.Utils.HttpManager;
import com.bom.sangue.sanguebom.persistence.bean.Patient;
import com.bom.sangue.sanguebom.persistence.dao.PatientDAO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 05/12/15.
 */
public class PatientFragment extends Fragment {

    View rootView;

    ArrayAdapter<String> patientListAdpt;
    List<String> names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list_emergency, container, false);

        names = new ArrayList<>();
        names.addAll(getPreviousPatientList());

        ListView patientList = (ListView) rootView.findViewById(R.id.patient_list);
        patientListAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
        patientList.setAdapter(patientListAdpt);

        fillPatientList();
        return rootView;
    }

    private void fillPatientList() {
        try {
            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, Constants.URL_LIST_PATIENT,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                names.clear();
                                for (int i=0; i < response.length(); i++) {
                                    JSONObject item = response.getJSONObject(i);
                                    Long id = item.getLong("id");
                                    String firstName = item.getString("first_name");
                                    String lastName = item.getString("last_name");
                                    String name = firstName + " " + lastName;
                                    BloodTypeEnum bloodType = BloodTypeEnum.getTypeEnum(item.getString("blood_type"));
                                    GenderEnum gender = GenderEnum.getGenderEnum(item.getString("gender"));
                                    Patient patient = new Patient();
                                    patient.setId(id);
                                    patient.setName(name);
                                    patient.setBloodType(bloodType);
                                    patient.setGender(gender);
                                    registerPatient(patient);

                                    names.add(name); // fill screen
                                }

                                names = alphabeticalOrdering(names);
                                patientListAdpt.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String message = error.getMessage();
                            Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + message);
                            try {
                                byte[] data  = error.networkResponse.data;
                                Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + new String(data, "UTF-8"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }) {
                // FIXME SECURITY FAILURE, this route has a admin token to catch a list of patient remove this dependence.
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + Constants.ROOT_TOKEN);
                    return headers;
                }
                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        if (response.data.length == 0) {
                            byte[] responseData = "{}".getBytes("UTF8");
                            response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            HttpManager.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getPreviousPatientList() {
        List<String> names = new ArrayList<>();

        PatientDAO patientDAO = PatientDAO.getInstance(getActivity().getApplicationContext());
        List<Patient> patients = patientDAO.listAll();
        patientDAO.closeConnection();

        for (Patient patient : patients) {
            names.add(patient.getName());
        }
        names = alphabeticalOrdering(names);
        return names;
    }

    private List<String> alphabeticalOrdering(List<String> names) {
        // Ordering names.
        Collections.sort(names, new Comparator<String>() {
            public int compare(String str1, String str2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        });

        return names;
    }

    private void registerPatient(Patient patient) {
        PatientDAO patientDAO = PatientDAO.getInstance(getActivity().getApplicationContext());
        patientDAO.save(patient);
        patientDAO.closeConnection();
    }
}
