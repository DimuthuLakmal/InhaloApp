package inhalo.titansmora.org.inhaloapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    TextView dobText;
    EditText firstNameText;
    EditText lastNameText;
    EditText heightText;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;

    private ProgressDialog progressDialog;

    private DatePickerDialog dobDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    Button highestPEFButton;
    Button highestFEVButton;
    Button addSharingDetailsButton;
    Button addAllergyDetailsButton;
    Button addMedicinesButton;
    Button updateButton;
    Button addDoctorsButton;

    RadioGroup radioGroup;

    private String userId;
    private String bestPEF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userId = getIntent().getStringExtra("userId");

        dobText = (TextView)findViewById(R.id.dobText);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        //highestFEVButton = (Button)findViewById(R.id.highestFEVButton);
        highestPEFButton = (Button)findViewById(R.id.highestPeakFlowButton);
        addSharingDetailsButton = (Button)findViewById(R.id.addSharingDetailsButton);
        addAllergyDetailsButton = (Button)findViewById(R.id.addAllergyDetailsButton);
        addMedicinesButton = (Button)findViewById(R.id.addMedicinesButton);
        updateButton = (Button)findViewById(R.id.updateButton);
        addDoctorsButton = (Button)findViewById(R.id.addDoctorsButton);

        firstNameText = (EditText)findViewById(R.id.firstNameUpdateText);
        lastNameText = (EditText)findViewById(R.id.lastNameUpdateText);
        heightText = (EditText)findViewById(R.id.heightUpdateText);

        maleRadioButton = (RadioButton)findViewById(R.id.maleUpdateRadio);
        femaleRadioButton = (RadioButton)findViewById(R.id.femaleUpdateRadio);
        radioGroup = (RadioGroup)findViewById(R.id.genderGroup);

        highestPEFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highesPEFIntent = new Intent(SettingsActivity.this, HighestPEFFlow.class);
                highesPEFIntent.putExtra("userId",userId);
                highesPEFIntent.putExtra("best_pef",bestPEF);
                startActivity(highesPEFIntent);
            }
        });

        addSharingDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSharingDetails = new Intent(SettingsActivity.this, AddSharingDetails.class);
                addSharingDetails.putExtra("userId",userId);
                startActivity(addSharingDetails);
            }
        });

        addAllergyDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAllergyDetails = new Intent(SettingsActivity.this, AddAllergiesActivity.class);
                addAllergyDetails.putExtra("userId",userId);
                startActivity(addAllergyDetails);
            }
        });

        addMedicinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addInhalerActivity = new Intent(SettingsActivity.this, AddInhalersActivity.class);
                addInhalerActivity.putExtra("userId",userId);
                startActivity(addInhalerActivity);
            }
        });

        addDoctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDoctorsActivity = new Intent(SettingsActivity.this, AddDoctorsActivity.class);
                addDoctorsActivity.putExtra("userId",userId);
                startActivity(addDoctorsActivity);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        setDateTimeField();

        progressDialog = new ProgressDialog(this);
        retreiveUserData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_histroy) {

            Intent history = new Intent(SettingsActivity.this, DataGatherActivity.class);
            history.putExtra("userId", userId);
            System.out.println(userId);
            startActivity(history);
            finish();

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(SettingsActivity.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(SettingsActivity.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    @Override
    public void onClick(View view) {
        if(view == dobText) {
            dobDatePickerDialog.show();
        } else if(view == highestPEFButton) {

        }
    }

    private void retreiveUserData() {

        progressDialog.setMessage("Retreiving user data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                HTTPConstants.URL_REREIVE_USER_DATA+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            firstNameText.setText(jsonObject.getJSONObject("user").getString("first_name"));
                            lastNameText.setText(jsonObject.getJSONObject("user").getString("lastname"));
                            heightText.setText(jsonObject.getJSONObject("user").getString("height"));
                            dobText.setText(jsonObject.getJSONObject("user").getString("dob").split("T")[0]);

                            bestPEF = jsonObject.getJSONObject("user").getString("best_pef");

                            if(jsonObject.getJSONObject("user").getBoolean("gender")){
                                radioGroup.check(maleRadioButton.getId());
                            } else if(!jsonObject.getJSONObject("user").getBoolean("gender")){
                                radioGroup.check(femaleRadioButton.getId());
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

    private void updateUserData() {

        final String firstName = firstNameText.getText().toString().trim();
        final String lastName = lastNameText.getText().toString().trim();
        final String height = heightText.getText().toString().trim();
        final String dob = dobText.getText().toString().trim();

        progressDialog.setMessage("Updating user data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HTTPConstants.URL_UPDATE_BASIC_USER_DATA,
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
                params.put("userId", userId);
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
}
