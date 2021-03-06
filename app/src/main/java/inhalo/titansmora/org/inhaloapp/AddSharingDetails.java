package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import inhalo.titansmora.org.inhaloapp.adapters.ShareDataAdapter;
import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class AddSharingDetails extends AppCompatActivity {

    Button openDialogBoxButton;

    private ProgressDialog progressDialog;

    private String userId;

    ShareDataAdapter adapter;
    ListView lView;
    ArrayList<String[]> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sharing_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getStringExtra("userId");

        //generate list
        list = new ArrayList<String[]>();
        //instantiate custom adapter
        adapter = new ShareDataAdapter(list, AddSharingDetails.this);

        //handle listview and assign adapter
        lView = (ListView)findViewById(R.id.shareDetailsListView);
        lView.setAdapter(adapter);

        openDialogBoxButton = (Button) findViewById(R.id.openDialogBoxButton);
        openDialogBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(AddSharingDetails.this);
                final View mView = layoutInflaterAndroid.inflate(R.layout.share_person_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddSharingDetails.this);
                alertDialogBuilderUserInput.setView(mView);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                EditText nameText = (EditText)mView.findViewById(R.id.sharePersonName);
                                EditText emailText = (EditText)mView.findViewById(R.id.shareEmail);
                                EditText mobileText = (EditText)mView.findViewById(R.id.shareMobile);

                                addContactDetails(nameText.getText().toString().trim(), emailText.getText().toString().trim(), mobileText.getText().toString().trim());
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

        progressDialog = new ProgressDialog(this);

        retreiveContacts();
    }

    public void addContactDetails(final String name, final String email, final String mobile) {

        progressDialog.setMessage("Adding Contact Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_ADD_CONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            JSONObject contact = jsonObject.getJSONObject("contact");
                            String[] listData = new String[]{contact.getString("name"),contact.getString("email"),contact.getString("mobile")};
                            list.add(listData);
                            adapter.notifyDataSetChanged();

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
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void updateContactDetails(final String name, final String email, final String mobile, final String id, final int position) {

        progressDialog.setMessage("Adding Contact Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_UPDATE_CONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            String[] updateValues = new String[]{name, email, mobile, id};
                            list.set(position, updateValues);
                            adapter.notifyDataSetChanged();

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
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void deleteContactDetails(final String id, final int position) {

        progressDialog.setMessage("Deleting Contact Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_DELETE_CONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            list.remove(position); //or some other task
                            adapter.notifyDataSetChanged();

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
                params.put("id", id);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void retreiveContacts() {

        progressDialog.setMessage("Retreiving Contact data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                HTTPConstants.URL_RETREIVE_CONTACTS+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONArray contacts = new JSONObject(response).getJSONArray("contacts");

                            for(int i=0,size = contacts.length(); i < size; i++) {
                                JSONObject contact = contacts.getJSONObject(i);
                                String[] listData = new String[]{contact.getString("name"),contact.getString("email"),contact.getString("mobile"),contact.getString("id")};
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
