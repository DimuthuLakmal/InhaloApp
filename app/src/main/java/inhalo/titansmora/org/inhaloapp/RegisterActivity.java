package inhalo.titansmora.org.inhaloapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText dobText;
    EditText userNameText;
    EditText passwordText;
    EditText firstNameText;
    EditText lastNameText;
    EditText heightText;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;

    private DatePickerDialog dobDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ProgressDialog progressDialog;

    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dobText = (EditText)findViewById(R.id.dobText);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        registerButton = (Button)findViewById(R.id.registerButton);

        userNameText = (EditText)findViewById(R.id.usernameRegText);
        passwordText = (EditText)findViewById(R.id.passwordRegText);
        firstNameText = (EditText)findViewById(R.id.firstNameRegText);
        lastNameText = (EditText)findViewById(R.id.lastNameRegText);
        heightText = (EditText)findViewById(R.id.heightRegText);

        maleRadioButton = (RadioButton)findViewById(R.id.maleRegRadio);
        femaleRadioButton = (RadioButton)findViewById(R.id.femaleRegRadio);

        progressDialog = new ProgressDialog(this);

        setDateTimeField();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void setDateTimeField() {
        dobText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dobText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void registerUser() {
        final String username = userNameText.getText().toString().trim();
        final String password = passwordText.getText().toString();
        final String firstName = firstNameText.getText().toString().trim();
        final String lastName = lastNameText.getText().toString().trim();
        final String height = heightText.getText().toString().trim();
        final String dob = dobText.getText().toString().trim();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_REGISTER,
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

                String gender = "1";
                if(maleRadioButton.isChecked()) {
                    gender = "1";
                } else if(femaleRadioButton.isChecked()){
                    gender = "0";
                }

                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("firstname", firstName);
                params.put("lastname", lastName);
                params.put("dob", dob);
                params.put("height", height);
                params.put("gender", gender);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if(view == dobText) {
            dobDatePickerDialog.show();
        } else if(view == registerButton) {
            registerUser();
        }
    }
}
