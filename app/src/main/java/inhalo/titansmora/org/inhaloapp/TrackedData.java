package inhalo.titansmora.org.inhaloapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import inhalo.titansmora.org.inhaloapp.charts.XAxisDateFormatter;

public class TrackedData extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static Context context;

    private ProgressDialog progressDialog;

    private static String userId;

    static ArrayList<Entry> entries;
    static ArrayList<Entry> entriesTwoMonth;
    static ArrayList<String> lastMonthDates;
    static ArrayList<String> lastTwoMonthDates;
    static ArrayList<String> lastTwoMonthSums;
    static ArrayList<String> lastMonthSums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");

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
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        context = TrackedData.this;

        userId = getIntent().getStringExtra("userId");

        progressDialog = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        entries = bundle.getParcelableArrayList("entries");
        entriesTwoMonth = bundle.getParcelableArrayList("entriesTwoMonth");
        lastMonthDates = bundle.getStringArrayList("lastMonthDates");
        lastTwoMonthDates = bundle.getStringArrayList("lastTwoMonthDates");
        lastMonthSums = bundle.getStringArrayList("lastMonthSums");
        lastTwoMonthSums = bundle.getStringArrayList("lastTwoMonthSums");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_histroy) {

            Intent history = new Intent(TrackedData.this, DataGatherActivity.class);
            history.putExtra("userId", userId);
            System.out.println(userId);
            startActivity(history);

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(TrackedData.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(TrackedData.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        } else if (id == R.id.nav_home) {

            Intent homeIntent = new Intent(TrackedData.this, HomeActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);

        } else if (id == R.id.nav_games) {

            Intent homeIntent = new Intent(TrackedData.this, GameActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);
        } else if (id == R.id.nav_logout) {

            Intent mainActivity = new Intent(TrackedData.this, MainActivity.class);
            startActivity(mainActivity);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tracked_data, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView wheezeText = (TextView) rootView.findViewById(R.id.wheezeText);
            TextView coughText = (TextView) rootView.findViewById(R.id.coughText);
            TextView coldText = (TextView) rootView.findViewById(R.id.coldText);
            TextView sputmText = (TextView) rootView.findViewById(R.id.sputumText);
            TextView chestText = (TextView) rootView.findViewById(R.id.chestText);
            TextView physicalActText = (TextView) rootView.findViewById(R.id.physicalAcvitiesText);
            TextView shortBreathText = (TextView) rootView.findViewById(R.id.shortBreathText);
            TextView nebulizedText = (TextView) rootView.findViewById(R.id.nebulizedText);
            TextView sleepText = (TextView) rootView.findViewById(R.id.botherSleepText);
            TextView asthmaAttackText = (TextView) rootView.findViewById(R.id.asthmaAttacksText);
            TextView wakeupText = (TextView) rootView.findViewById(R.id.wakeupText);
            TextView allergiesText = (TextView) rootView.findViewById(R.id.allergiesText);

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                textView.setText(lastMonthDates.get(0).replace('-','/')+" - "+lastMonthDates.get(lastMonthDates.size()-1).replace('-', '/')+" (Last Month)");

                LineChart pefChart = (LineChart)rootView.findViewById(R.id.pefChart);

                LineDataSet dataSet = new LineDataSet(entries, "PEF");
                dataSet.setDrawCircles(false);
                LineData lineData = new LineData(dataSet);

                XAxis xAxis = pefChart.getXAxis();
                xAxis.setLabelRotationAngle(-45);
                xAxis.setValueFormatter(new XAxisDateFormatter(lastMonthDates) {
                });
                pefChart.setData(lineData);
                pefChart.invalidate();

                if(!lastMonthSums.get(0).equals("null")) {
                    wheezeText.setText(lastMonthSums.get(0) + " Days");
                } else {
                    wheezeText.setText("0 Records");
                }
                if(!lastMonthSums.get(1).equals("null")) {
                    coughText.setText(lastMonthSums.get(1) + " Days");
                } else {
                    coughText.setText("0 Records");
                }
                if(!lastMonthSums.get(2).equals("null")) {
                    sputmText.setText(lastMonthSums.get(2) + " Days");
                } else {
                    sputmText.setText("0 Records");
                }
                if(!lastMonthSums.get(3).equals("null")) {
                    coldText.setText(lastMonthSums.get(3) + " Days");
                } else {
                    coldText.setText("0 Records");
                }
                if(!lastMonthSums.get(4).equals("null")) {
                    sleepText.setText(lastMonthSums.get(4) + " Days");
                } else {
                    sleepText.setText("0 Records");
                }
                if(!lastMonthSums.get(5).equals("null")) {
                    chestText.setText(lastMonthSums.get(5) + " Days");
                } else {
                    chestText.setText("0 Records");
                }
                if(!lastMonthSums.get(6).equals("null")) {
                    shortBreathText.setText(lastMonthSums.get(6) + " Days");
                } else {
                    shortBreathText.setText("0 Records");
                }
                if(!lastMonthSums.get(7).equals("null")) {
                    physicalActText.setText(lastMonthSums.get(7) + " Days");
                } else {
                    physicalActText.setText("0 Records");
                }
                if(!lastMonthSums.get(8).equals("null")) {
                    nebulizedText.setText(lastMonthSums.get(8) + " Times");
                } else {
                    nebulizedText.setText("0 Records");
                }
                if(!lastMonthSums.get(9).equals("null")) {
                    asthmaAttackText.setText(lastMonthSums.get(9) + " Times");
                } else {
                    asthmaAttackText.setText("0 Records");
                }
                if(!lastMonthSums.get(10).equals("null")) {
                    wakeupText.setText(lastMonthSums.get(10) + " Times");
                } else {
                    wakeupText.setText("0 Records");
                }
                if(!lastMonthSums.get(11).equals("null")) {
                    allergiesText.setText(lastMonthSums.get(11) + " Days");
                } else {
                    allergiesText.setText("0 Records");
                }


            } else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                textView.setText(lastTwoMonthDates.get(0).replace('-','/')+" - "+lastTwoMonthDates.get(lastTwoMonthDates.size()-1).replace('-', '/')+"");

                LineChart pefChart = (LineChart)rootView.findViewById(R.id.pefChart);

                LineDataSet dataSet = new LineDataSet(entriesTwoMonth, "PEF");
                dataSet.setDrawCircles(false);
                LineData lineData = new LineData(dataSet);

                XAxis xAxis = pefChart.getXAxis();
                xAxis.setLabelRotationAngle(-90);
                xAxis.setValueFormatter(new XAxisDateFormatter(lastTwoMonthDates) {});
                pefChart.setData(lineData);
                pefChart.invalidate();

                if(!lastTwoMonthSums.get(0).equals("null")) {
                    wheezeText.setText(lastTwoMonthSums.get(0) + " Days");
                } else {
                    wheezeText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(1).equals("null")) {
                    coughText.setText(lastTwoMonthSums.get(1) + " Days");
                } else {
                    coughText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(2).equals("null")) {
                    sputmText.setText(lastTwoMonthSums.get(2) + " Days");
                } else {
                    sputmText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(3).equals("null")) {
                    coldText.setText(lastTwoMonthSums.get(3) + " Days");
                } else {
                    coldText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(4).equals("null")) {
                    sleepText.setText(lastTwoMonthSums.get(4) + " Days");
                } else {
                    sleepText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(5).equals("null")) {
                    chestText.setText(lastTwoMonthSums.get(5) + " Days");
                } else {
                    chestText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(6).equals("null")) {
                    shortBreathText.setText(lastTwoMonthSums.get(6) + " Days");
                } else {
                    shortBreathText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(7).equals("null")) {
                    physicalActText.setText(lastTwoMonthSums.get(7) + " Days");
                } else {
                    physicalActText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(8).equals("null")) {
                    nebulizedText.setText(lastTwoMonthSums.get(8) + " Times");
                } else {
                    nebulizedText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(9).equals("null")) {
                    asthmaAttackText.setText(lastTwoMonthSums.get(9) + " Times");
                } else {
                    asthmaAttackText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(10).equals("null")) {
                    wakeupText.setText(lastTwoMonthSums.get(10) + " Times");
                } else {
                    wakeupText.setText("0 Records");
                }
                if(!lastTwoMonthSums.get(11).equals("null")) {
                    allergiesText.setText(lastTwoMonthSums.get(11) + " Days");
                } else {
                    allergiesText.setText("0 Records");
                }


            } else {
                textView.setText("Last Three Months");
            }

            Button sendReportButton = (Button)rootView.findViewById(R.id.send_report_button);
            sendReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendReportIntent = new Intent(context, SendReportActivity.class);
                    sendReportIntent.putExtra("userId",userId);
                    startActivity(sendReportIntent);
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

}
