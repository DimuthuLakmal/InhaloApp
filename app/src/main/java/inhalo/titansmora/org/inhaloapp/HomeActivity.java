package inhalo.titansmora.org.inhaloapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String userId;

    TextView dailyPEFText;

    ArrayList<Entry> yAXES = new ArrayList<>();
    ArrayList<Entry> yAXESTwoMonth = new ArrayList<>();
    final ArrayList<String> lastMonthDates = new ArrayList<String>();
    final ArrayList<String> lastTwoMonthDates = new ArrayList<String>();
    final ArrayList<String> lastMonthSums = new ArrayList<String>();
    final ArrayList<String> lastTwoMonthSums = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dailyPEFText = (TextView)findViewById(R.id.dailypefText);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dailyPEF = prefs.getString("daily_pef", "0.00");
        dailyPEFText.setText(dailyPEF+" L/min");

        userId = getIntent().getStringExtra("userId");

        retreiveUserData(HTTPConstants.URL_RETREIVE_PAST_DATA, 0,yAXES, lastMonthDates);
        retreiveUserData(HTTPConstants.URL_RETREIVE_PAST_DATA, 1,yAXESTwoMonth, lastTwoMonthDates);
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

            Intent history = new Intent(HomeActivity.this, TrackedData.class);
            history.putExtra("userId", userId);
            history.putExtra("entries", yAXES);
            history.putExtra("entriesTwoMonth", yAXESTwoMonth);
            history.putExtra("lastMonthDates", lastMonthDates);
            history.putExtra("lastTwoMonthDates", lastTwoMonthDates);
            history.putExtra("lastMonthSums", lastMonthSums);
            history.putExtra("lastTwoMonthSums", lastTwoMonthSums);
            for(Entry e: yAXES) {
                System.out.println(e.getX());
            }
            startActivity(history);
            finish();

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(HomeActivity.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void retreiveUserData(String URL, final int category, final ArrayList<Entry> graphEntries, final ArrayList<String> previousDates ) {

        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String endDate = null;
        String startDate = null;
        if(category == 0) {

            endDate = sdfDate.format(now);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            startDate = sdfDate.format(cal.getTime());

        } else if(category == 1) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            endDate = sdfDate.format(cal.getTime());

            cal.add(Calendar.MONTH, -1);
            startDate = sdfDate.format(cal.getTime());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL+userId+"/enddate/"+endDate+"/startdate/"+startDate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataPefArray = new JSONObject(response).getJSONArray("dataPefs");
                            JSONArray dataSumsArray = new JSONObject(response).getJSONArray("dataSums");


                            Date endDate = null;
                            Date startDate = null;
                            if(category == 0) {

                                JSONObject sum = dataSumsArray.getJSONObject(0);
                                lastMonthSums.add(sum.getString("sum(wheeze)"));
                                lastMonthSums.add(sum.getString("sum(cough)"));
                                lastMonthSums.add(sum.getString("sum(sputm)"));
                                lastMonthSums.add(sum.getString("sum(cold)"));
                                lastMonthSums.add(sum.getString("sum(bother_sleep)"));
                                lastMonthSums.add(sum.getString("sum(chest_tightness)"));
                                lastMonthSums.add(sum.getString("sum(short_breath)"));
                                lastMonthSums.add(sum.getString("sum(physical_activity)"));
                                lastMonthSums.add(sum.getString("sum(nebulized)"));

                                endDate = new Date();

                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.MONTH, -1);
                                startDate = cal.getTime();

                            } else if(category == 1) {

                                JSONObject sum = dataSumsArray.getJSONObject(0);
                                lastTwoMonthSums.add(sum.getString("sum(wheeze)"));
                                lastTwoMonthSums.add(sum.getString("sum(cough)"));
                                lastTwoMonthSums.add(sum.getString("sum(sputm)"));
                                lastTwoMonthSums.add(sum.getString("sum(cold)"));
                                lastTwoMonthSums.add(sum.getString("sum(bother_sleep)"));
                                lastTwoMonthSums.add(sum.getString("sum(chest_tightness)"));
                                lastTwoMonthSums.add(sum.getString("sum(short_breath)"));
                                lastTwoMonthSums.add(sum.getString("sum(physical_activity)"));
                                lastTwoMonthSums.add(sum.getString("sum(nebulized)"));

                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.MONTH, -1);
                                endDate = cal.getTime();

                                cal.add(Calendar.MONTH, -1);
                                startDate = cal.getTime();
                            }

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(startDate);

                            while (calendar.getTime().before(endDate))
                            {
                                Date result = calendar.getTime();
                                previousDates.add(sdfDate.format(result));
                                calendar.add(Calendar.DATE, 1);
                            }
                            if(category == 0) {
                                previousDates.add(sdfDate.format(new Date()));
                            }

                            int index = 0;
                            for(String previousDate: previousDates) {
                                boolean isExistInRecord = false;
                                for(int i=0,size = dataPefArray.length(); i < size; i++) {
                                    JSONObject data = dataPefArray.getJSONObject(i);

                                    if(data.getString("date").split("T")[0].equals(previousDate)) {
                                        isExistInRecord = true;
                                        graphEntries.add(new Entry(Float.parseFloat(String.valueOf(index)), Float.parseFloat(String.valueOf(data.getString("pef")))));
                                    }
                                }
                                if(!isExistInRecord) {
                                    graphEntries.add(new Entry(Float.parseFloat(String.valueOf(index)), 0.0f));
                                }
                                index++;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
