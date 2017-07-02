package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import inhalo.titansmora.org.inhaloapp.adapters.AddInhalerAdapter;
import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class AddInhalersActivity extends AppCompatActivity {

    Button nextButton;

    private ProgressDialog progressDialog;

    ArrayList<String[]> list;
    AddInhalerAdapter adapter;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inhalers);

        userId = getIntent().getStringExtra("userId");

        nextButton = (Button)findViewById(R.id.inhaler_next_button);
        //generate list
        list = new ArrayList<String[]>();;
        String[] mdi = new String[]{"Metered Dose Inhaler", "false"};
        String[] mdiSpacer = new String[]{"MDI with Spacer", "false"};
        String[] dpi = new String[]{"Dry Powder Inhaler", "false"};

        list.add(mdi);
        list.add(mdiSpacer);
        list.add(dpi);

        //instantiate custom adapter
        adapter = new AddInhalerAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.inhaler_list);
        lView.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rescueMedicineIntent = new Intent(AddInhalersActivity.this, AddRescueMedicineActivity.class);
                startActivity(rescueMedicineIntent);
            }
        });

        progressDialog = new ProgressDialog(this);

        retreiveInhalers();
    }

    public void addInhalerDetails(final String inhaler) {

        progressDialog.setMessage("Adding Inhaler Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_ADD_INHALER,
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
                params.put("type", inhaler);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void retreiveInhalers() {

        progressDialog.setMessage("Retreiving Inhaler data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                HTTPConstants.URL_RETREIVE_INHALER + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONArray inhalers = new JSONObject(response).getJSONArray("inhalers");

                            for(int i=0,size = inhalers.length(); i < size; i++) {
                                JSONObject selectedInhaler = inhalers.getJSONObject(i);

                                int index = 0;
                                for(String[] inhaler: list) {
                                    if(inhaler[0].equals(selectedInhaler.getString("type"))) {
                                        String[] inhalerChecked = new String[]{inhaler[0],"true"};
                                        list.set(index, inhalerChecked);
                                    }
                                    index++;
                                }
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

    public void deleteInhalerDetails(final String inhaler) {

        progressDialog.setMessage("Deleting Inhaler Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_DELETE_INHALER,
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
                params.put("type", inhaler);
                params.put("userId", userId);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
