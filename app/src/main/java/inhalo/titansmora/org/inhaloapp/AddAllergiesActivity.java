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

import inhalo.titansmora.org.inhaloapp.adapters.AddAlergyAdapter;
import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class AddAllergiesActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    ArrayList<String[]> list;
    AddAlergyAdapter adapter;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergies);

        userId = getIntent().getStringExtra("userId");

        //generate list
        list = new ArrayList<String[]>();
        String[] animalDander = new String[]{"Animal Dander","false"};
        String[] cockroaches = new String[]{"Cockroaches","false"};
        String[] dustMites = new String[]{"Dust Mites","false"};
        String[] indoorMolds = new String[]{"Indoor Mold","false"};
        String[] medicines = new String[]{"Medicines","false"};
        String[] pollen = new String[]{"Pollen and Outdoor Mold","false"};
        String[] pollution = new String[]{"Pollution","false"};
        String[] smoke = new String[]{"Smoke","false"};
        String[] strongOrdor = new String[]{"Strong Odor","false"};
        String[] vaccumCleaning = new String[]{"Vaccum Cleaning","false"};
        String[] viralIllness = new String[]{"Viral Illness","false"};
        String[] weather = new String[]{"Weather","false"};

        list.add(animalDander);
        list.add(cockroaches);
        list.add(dustMites);
        list.add(indoorMolds);
        list.add(medicines);
        list.add(pollen);
        list.add(pollution);
        list.add(smoke);
        list.add(strongOrdor);
        list.add(vaccumCleaning);
        list.add(viralIllness);
        list.add(weather);

        //instantiate custom adapter
        adapter = new AddAlergyAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.add_allergy_list);
        lView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);

        retreiveAllergies();
    }

    public void addAllergenDetails(final String allergen) {

        progressDialog.setMessage("Adding Allergy Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_ADD_ALLERGENS,
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
                params.put("allergen", allergen);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void retreiveAllergies() {

        progressDialog.setMessage("Retreiving Allergy data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                HTTPConstants.URL_RETREIVE_ALLERGENS + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONArray allergens = new JSONObject(response).getJSONArray("allergens");

                            for(int i=0,size = allergens.length(); i < size; i++) {
                                JSONObject selectedAllergen = allergens.getJSONObject(i);

                                int index = 0;
                                for(String[] allergen: list) {
                                    if(allergen[0].equals(selectedAllergen.getString("allergen"))) {
                                        String[] allergyChecked = new String[]{allergen[0],"true"};
                                        list.set(index, allergyChecked);
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

    public void deleteAllergyDetails(final String allergen) {

        progressDialog.setMessage("Deleting Allergy Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_DELETE_ALLERGENS,
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
                params.put("allergen", allergen);
                params.put("userId", userId);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
