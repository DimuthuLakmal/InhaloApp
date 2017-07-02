package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class HighestPEFFlow extends AppCompatActivity {

    Button saveButton;

    EditText bestPEFText;

    private ProgressDialog progressDialog;

    String userId;
    String bestPEF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_pef_flow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveButton = (Button)findViewById(R.id.savePEFButton);

        bestPEFText = (EditText)findViewById(R.id.highestpefText);

        userId = getIntent().getStringExtra("userId");
        bestPEF = getIntent().getStringExtra("best_pef");

        bestPEFText.setText(bestPEF+" l/min");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPEFDetails(bestPEFText.getText().toString().split(" ")[0]);
            }
        });

        progressDialog = new ProgressDialog(this);


    }

    public void addPEFDetails(final String pef) {

        progressDialog.setMessage("Updating PEF Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_UPDATE_HIGHESTPEF_USER_DATA,
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
                params.put("best_pef", pef);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

}
