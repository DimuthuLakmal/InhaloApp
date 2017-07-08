package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class DailyQuestionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RadioButton yesWheezeRadio;
    RadioButton noWheezeRadio;

    RadioButton yesCoughRadio;
    RadioButton noCoughRadio;

    RadioButton yesSputumRadio;
    RadioButton noSputumRadio;

    RadioButton yesColdRadio;
    RadioButton noColdRadio;

    RadioButton yesSleepRadio;
    RadioButton noSleepRadio;

    RadioButton yesChestRadio;
    RadioButton noChestRadio;

    RadioButton yesPhysicalActivityRadio;
    RadioButton noPhysicalActivityRadio;

    RadioButton yesShortBreath;
    RadioButton noShortBreath;

    RadioButton yesAsthmaRadio;
    RadioButton noAsthmaRadio;

    RadioButton yesAllergyRadio;
    RadioButton noAllergyRadio;

    RadioGroup wheezeRadioGroup;
    RadioGroup coldRadioGroup;
    RadioGroup coughRadioGroup;
    RadioGroup shortBreathRadioGroup;
    RadioGroup sputumRadioGroup;
    RadioGroup physicalActivityRadioGroup;
    RadioGroup chestRadioGroup;
    RadioGroup slepRadioGroup;
    RadioGroup asthmaRadioGroup;
    RadioGroup allergyGroup;

    EditText numberOfNebulizationText;
    EditText numberOfPuffsText;
    EditText numberOfWakeupsText;

    Button nextButton;

    private ProgressDialog progressDialog;

    String userId;
    String daily_pef;

    Boolean isNewRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daily Info");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        TextView usernameNavText = (TextView)header.findViewById(R.id.usernameNavText);
        SharedPreferences prefs_ = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = prefs_.getString("username", "User");
        usernameNavText.setText(username);

        yesWheezeRadio = (RadioButton)findViewById(R.id.yesWheeze);
        noWheezeRadio = (RadioButton)findViewById(R.id.noWheeze);

        yesCoughRadio = (RadioButton)findViewById(R.id.yesCough);
        noCoughRadio = (RadioButton)findViewById(R.id.noCough);

        yesSputumRadio = (RadioButton)findViewById(R.id.yesSputum);
        noSputumRadio = (RadioButton)findViewById(R.id.noSputum);

        yesColdRadio = (RadioButton)findViewById(R.id.yesCold);
        noColdRadio = (RadioButton)findViewById(R.id.noCold);

        yesChestRadio = (RadioButton)findViewById(R.id.yesChestTightness);
        noChestRadio = (RadioButton)findViewById(R.id.noChestTightness);

        yesSleepRadio = (RadioButton)findViewById(R.id.yesSleep);
        noSleepRadio = (RadioButton)findViewById(R.id.noSleep);

        yesShortBreath = (RadioButton)findViewById(R.id.yesShortBreath);
        noShortBreath = (RadioButton)findViewById(R.id.noShortBreath);

        yesAsthmaRadio = (RadioButton)findViewById(R.id.yesAsthma);
        noAsthmaRadio = (RadioButton)findViewById(R.id.noAsthma);

        yesAllergyRadio = (RadioButton)findViewById(R.id.yesAllergy);
        noAllergyRadio = (RadioButton)findViewById(R.id.noAllergy);

        yesPhysicalActivityRadio = (RadioButton)findViewById(R.id.yesPhysicalActivity);
        noPhysicalActivityRadio = (RadioButton)findViewById(R.id.noPhysicalActivity);

        wheezeRadioGroup = (RadioGroup)findViewById(R.id.wheezeRadioGroup);
        chestRadioGroup = (RadioGroup)findViewById(R.id.chestRadioGroup);
        coldRadioGroup = (RadioGroup)findViewById(R.id.coldRadioGroup);
        sputumRadioGroup = (RadioGroup)findViewById(R.id.sputumRadioGroup);
        physicalActivityRadioGroup = (RadioGroup)findViewById(R.id.physicalActivityRadioGroup);
        slepRadioGroup = (RadioGroup)findViewById(R.id.sleepRadioGroup);
        shortBreathRadioGroup = (RadioGroup)findViewById(R.id.shortBreathRadioGroup);
        coughRadioGroup = (RadioGroup)findViewById(R.id.coughRadioGroup);
        asthmaRadioGroup = (RadioGroup)findViewById(R.id.asthamRadioGroup);
        allergyGroup = (RadioGroup)findViewById(R.id.allergy_exposureRadioGroup);

        numberOfNebulizationText = (EditText)findViewById(R.id.nebulizationText);
        numberOfPuffsText = (EditText)findViewById(R.id.puffsText);
        numberOfWakeupsText = (EditText)findViewById(R.id.awakeText);

        userId = getIntent().getStringExtra("userId");

        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDailyDataBasic();
            }
        });

        progressDialog = new ProgressDialog(this);

        isNewRecord = true;

        retreiveDailyData();
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

            Intent history = new Intent(DailyQuestionsActivity.this, DataGatherActivity.class);
            history.putExtra("userId", userId);
            System.out.println(userId);
            startActivity(history);

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(DailyQuestionsActivity.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(DailyQuestionsActivity.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void retreiveDailyData() {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String date = sdfDate.format(now);

        progressDialog.setMessage("Retreiving user data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                HTTPConstants.URL_RETREIVE_DAILY_DATA+userId+"/date/"+date,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");

                            if(jsonObject != null) {

                                if(!jsonObject.getString("nebulized").equals("null")) {
                                    numberOfNebulizationText.setText(jsonObject.getString("nebulized"));
                                }
                                if(!jsonObject.getString("puffs").equals("null")) {
                                    numberOfPuffsText.setText(jsonObject.getString("puffs"));
                                }
                                if(!jsonObject.getString("times_awake").equals("null")) {
                                    numberOfWakeupsText.setText(jsonObject.getString("times_awake"));
                                }

                                daily_pef = jsonObject.getString("pef");

                                if(jsonObject.getBoolean("wheeze")){
                                    wheezeRadioGroup.check(yesWheezeRadio.getId());
                                } else if(!jsonObject.getBoolean("wheeze")){
                                    wheezeRadioGroup.check(noWheezeRadio.getId());
                                }

                                if(jsonObject.getBoolean("cough")){
                                    coughRadioGroup.check(yesCoughRadio.getId());
                                } else if(!jsonObject.getBoolean("cough")){
                                    coughRadioGroup.check(noCoughRadio.getId());
                                }

                                if(jsonObject.getBoolean("cold")){
                                    coldRadioGroup.check(yesColdRadio.getId());
                                } else if(!jsonObject.getBoolean("cold")){
                                    coldRadioGroup.check(noColdRadio.getId());
                                }

                                if(jsonObject.getBoolean("sputm")){
                                    sputumRadioGroup.check(yesSputumRadio.getId());
                                } else if(!jsonObject.getBoolean("sputm")){
                                    sputumRadioGroup.check(noSputumRadio.getId());
                                }

                                if(jsonObject.getBoolean("chest_tightness")){
                                    chestRadioGroup.check(yesChestRadio.getId());
                                } else if(!jsonObject.getBoolean("chest_tightness")){
                                    chestRadioGroup.check(noChestRadio.getId());
                                }

                                if(jsonObject.getBoolean("short_breath")){
                                    shortBreathRadioGroup.check(yesShortBreath.getId());
                                } else if(!jsonObject.getBoolean("short_breath")){
                                    shortBreathRadioGroup.check(noShortBreath.getId());
                                }

                                if(jsonObject.getBoolean("physical_activity")){
                                    physicalActivityRadioGroup.check(yesPhysicalActivityRadio.getId());
                                } else if(!jsonObject.getBoolean("physical_activity")){
                                    physicalActivityRadioGroup.check(noPhysicalActivityRadio.getId());
                                }

                                if(jsonObject.getBoolean("bother_sleep")){
                                    slepRadioGroup.check(yesSleepRadio.getId());
                                } else if(!jsonObject.getBoolean("bother_sleep")){
                                    slepRadioGroup.check(noSleepRadio.getId());
                                }

                                if(jsonObject.getBoolean("asthma_condition")){
                                    asthmaRadioGroup.check(yesAsthmaRadio.getId());
                                } else if(!jsonObject.getBoolean("asthma_condition")){
                                    asthmaRadioGroup.check(noAsthmaRadio.getId());
                                }

                                if(jsonObject.getBoolean("allergy")){
                                    allergyGroup.check(yesAllergyRadio.getId());
                                } else if(!jsonObject.getBoolean("allergy")){
                                    allergyGroup.check(noAllergyRadio.getId());
                                }

                                isNewRecord = false;
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

    public void addDailyDataBasic() {

        progressDialog.setMessage("Updating Daily Data...");
        progressDialog.show();

        String finalURL = HTTPConstants.URL_ADD_DAILY_DATA_BASIC;

        if(!isNewRecord) {
            finalURL = HTTPConstants.URL_UPDATE_DATA_BASIC;
            System.out.println("Oh! Yeah");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                finalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            Intent dailyPEFIntent = new Intent(DailyQuestionsActivity.this, AddDailyPEFActivity.class);
                            dailyPEFIntent.putExtra("userId", userId);
                            dailyPEFIntent.putExtra("daily_pef", daily_pef);
                            startActivity(dailyPEFIntent);

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

                String wheeze = "0";
                if(yesWheezeRadio.isChecked()) {
                    wheeze = "1";
                } else if(noWheezeRadio.isChecked()){
                    wheeze = "0";
                }

                String cough = "0";
                if(yesCoughRadio.isChecked()) {
                    cough = "1";
                } else if(noChestRadio.isChecked()){
                    cough = "0";
                }

                String cold = "0";
                if(yesColdRadio.isChecked()) {
                    cold = "1";
                } else if(noColdRadio.isChecked()){
                    cold = "0";
                }

                String sleep = "0";
                if(yesSleepRadio.isChecked()) {
                    sleep = "1";
                } else if(noSleepRadio.isChecked()){
                    sleep = "0";
                }

                String shortBreath = "0";
                if(yesShortBreath.isChecked()) {
                    shortBreath = "1";
                } else if(noShortBreath.isChecked()){
                    shortBreath = "0";
                }

                String sputum = "0";
                if(yesSputumRadio.isChecked()) {
                    sputum = "1";
                } else if(noSputumRadio.isChecked()){
                    sputum = "0";
                }

                String chest = "0";
                if(yesChestRadio.isChecked()) {
                    chest = "1";
                } else if(noChestRadio.isChecked()){
                    chest = "0";
                }

                String physicalActivity = "0";
                if(yesPhysicalActivityRadio.isChecked()) {
                    physicalActivity = "1";
                } else if(noPhysicalActivityRadio.isChecked()){
                    physicalActivity = "0";
                }

                String asthma = "0";
                if(yesAsthmaRadio.isChecked()) {
                    asthma = "1";
                } else if(noAsthmaRadio.isChecked()){
                    asthma = "0";
                }

                String allergy = "0";
                if(yesAllergyRadio.isChecked()) {
                    allergy = "1";
                } else if(noAllergyRadio.isChecked()){
                    allergy = "0";
                }

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
                Date now = new Date();
                String date = sdfDate.format(now);
                String nebulizations = numberOfNebulizationText.getText().toString();
                String wakeUps = numberOfWakeupsText.getText().toString();
                String puffs = numberOfPuffsText.getText().toString();

                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("wheeze", wheeze);
                params.put("cough", cough);
                params.put("sputum", sputum);
                params.put("cold", cold);
                params.put("chest_tightness", chest);
                params.put("short_breath", shortBreath);
                params.put("physical_activity", physicalActivity);
                params.put("bother_sleep", sleep);
                params.put("asthma_condition", asthma);
                params.put("date", date);
                params.put("nebulizations", nebulizations);
                params.put("times_awake", wakeUps);
                params.put("puffs", puffs);
                params.put("allergy", allergy);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

}
