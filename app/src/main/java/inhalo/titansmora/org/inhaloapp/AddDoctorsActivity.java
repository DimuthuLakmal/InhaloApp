package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import inhalo.titansmora.org.inhaloapp.adapters.AddDoctorAdapter;
import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class AddDoctorsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private String userId;

    AddDoctorAdapter adapter;
    ListView lView;
    ArrayList<String[]> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctors);

        userId = getIntent().getStringExtra("userId");

        //generate list
        list = new ArrayList<String[]>();
        //instantiate custom adapter
        adapter = new AddDoctorAdapter(list, AddDoctorsActivity.this);

        //handle listview and assign adapter
        lView = (ListView)findViewById(R.id.shareDetailsListView);
        lView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);

        retreiveDoctorDetails();

    }

    public void addDoctor(final String id) {

        progressDialog.setMessage("Adding Doctor...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_ADD_DOCTOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("id", id);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void retreiveDoctorDetails() {

        progressDialog.setMessage("Retreiving Doctors data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                HTTPConstants.URL_RETREIVE_ALL_DOCTOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONArray doctors = new JSONObject(response).getJSONArray("doctors");

                            for(int i=0,size = doctors.length(); i < size; i++) {
                                JSONObject doctor = doctors.getJSONObject(i);
                                System.out.println(doctor.getString("first_name")+" "+doctor.getString("lastname"));
                                String[] listData = new String[]{doctor.getString("first_name")+" "+doctor.getString("lastname"),doctor.getString("hospital"),doctor.getString("mobile"),doctor.getString("id")};
                                list.add(listData);
                                adapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}
