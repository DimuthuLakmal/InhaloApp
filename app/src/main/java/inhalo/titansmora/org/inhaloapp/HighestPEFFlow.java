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
import android.widget.TextView;
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

public class HighestPEFFlow extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button saveButton;
    Button calculateMeasureButton;

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
        getSupportActionBar().setTitle("Your Highest PEF");

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

        saveButton = (Button)findViewById(R.id.savePEFButton);
        calculateMeasureButton = (Button)findViewById(R.id.calculateMeasureButton);

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

        calculateMeasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = HighestPEFFlow.this.getPackageManager().getLaunchIntentForPackage("com.Titans.InhaloGame");
//                gameIntent.setType("text/plain");
//                gameIntent.putExtra("userId", userId);
                startActivity(gameIntent);
            }
        });

        progressDialog = new ProgressDialog(this);


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

            Intent history = new Intent(HighestPEFFlow.this, DataGatherActivity.class);
            history.putExtra("userId", userId);
            System.out.println(userId);
            startActivity(history);

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(HighestPEFFlow.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(HighestPEFFlow.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
