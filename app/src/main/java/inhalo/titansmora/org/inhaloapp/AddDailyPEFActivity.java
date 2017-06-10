package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDailyPEFActivity extends AppCompatActivity {

    Button saveButton;

    EditText dailyPEFText;

    private ProgressDialog progressDialog;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_pef);

        saveButton = (Button)findViewById(R.id.savePEFButton);

        dailyPEFText = (EditText)findViewById(R.id.dailypefText);

        userId = getIntent().getStringExtra("userId");

        dailyPEFText.setText(getIntent().getStringExtra("daily_pef")+" l/min");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPEFDetails(dailyPEFText.getText().toString().split(" ")[0]);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("daily_pef", dailyPEFText.getText().toString().split(" ")[0]);
                editor.commit();
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    public void addPEFDetails(final String pef) {

        progressDialog.setMessage("Updating Daily PEF Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_ADD_DAILY_DATA_PEF,
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

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
                Date now = new Date();
                String date = sdfDate.format(now);

                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("pef", pef);
                params.put("date", date);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
