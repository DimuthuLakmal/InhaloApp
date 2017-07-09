package inhalo.titansmora.org.inhaloapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button inhalerGameButton;
    Button exerciseGameButton;
    Button pefGameButton;

    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Play Games");

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

        inhalerGameButton = (Button)findViewById(R.id.inhalerGameButton);
        exerciseGameButton = (Button)findViewById(R.id.exerciseGameButton);
        pefGameButton = (Button)findViewById(R.id.pefGameButton);

        inhalerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = GameActivity.this.getPackageManager().getLaunchIntentForPackage("com.Titans.InhaloGame");
                startActivity(gameIntent);
            }
        });

        pefGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = GameActivity.this.getPackageManager().getLaunchIntentForPackage("com.titans.pefhillclimber");
                startActivity(gameIntent);
            }
        });

        exerciseGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = GameActivity.this.getPackageManager().getLaunchIntentForPackage("com.titans.hillclimber");
                startActivity(gameIntent);
            }
        });
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

            Intent history = new Intent(GameActivity.this, DataGatherActivity.class);
            history.putExtra("userId", userId);
            System.out.println(userId);
            startActivity(history);

        } else if (id == R.id.nav_settings) {

            Intent settings = new Intent(GameActivity.this, SettingsActivity.class);
            settings.putExtra("userId", userId);
            startActivity(settings);

        } else if (id == R.id.nav_daily_details) {

            Intent dailyDetails = new Intent(GameActivity.this, DailyQuestionsActivity.class);
            dailyDetails.putExtra("userId", userId);
            startActivity(dailyDetails);

        } else if (id == R.id.nav_home) {

            Intent homeIntent = new Intent(GameActivity.this, HomeActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);
        } else if (id == R.id.nav_games) {

            Intent homeIntent = new Intent(GameActivity.this, GameActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);
        } else if (id == R.id.nav_logout) {

            Intent mainActivity = new Intent(GameActivity.this, MainActivity.class);
            startActivity(mainActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
