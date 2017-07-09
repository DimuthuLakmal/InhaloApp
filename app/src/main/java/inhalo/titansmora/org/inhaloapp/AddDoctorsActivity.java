package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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

public class AddDoctorsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;

    private String userId;

    AddDoctorAdapter adapter;
    ListView lView;
    ArrayList<String[]> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Your Doctor");

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

            Intent history = new Intent(AddDoctorsActivity.this, DataGatherActivity.class);
            history.putExtra("userId", userId);
            System.out.println(userId);
            startActivity(history);

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(AddDoctorsActivity.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(AddDoctorsActivity.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        } else if (id == R.id.nav_home) {

            Intent homeIntent = new Intent(AddDoctorsActivity.this, HomeActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);

        } else if (id == R.id.nav_games) {

            Intent homeIntent = new Intent(AddDoctorsActivity.this, GameActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);
        } else if (id == R.id.nav_logout) {

            Intent mainActivity = new Intent(AddDoctorsActivity.this, MainActivity.class);
            startActivity(mainActivity);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
