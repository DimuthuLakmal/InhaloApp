package inhalo.titansmora.org.inhaloapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView dobText;

    private DatePickerDialog dobDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    Button highestPEFButton;
    Button highestFEVButton;
    Button addSharingDetailsButton;
    Button addAllergyDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dobText = (TextView)findViewById(R.id.dobText);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        highestFEVButton = (Button)findViewById(R.id.highestFEVButton);
        highestPEFButton = (Button)findViewById(R.id.highestPeakFlowButton);
        addSharingDetailsButton = (Button)findViewById(R.id.addSharingDetailsButton);
        addAllergyDetailsButton = (Button)findViewById(R.id.addAllergyDetailsButton);


        highestPEFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highesPEFIntent = new Intent(SettingsActivity.this, HighestPEFFlow.class);
                startActivity(highesPEFIntent);
            }
        });

        addSharingDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSharingDetails = new Intent(SettingsActivity.this, AddSharingDetails.class);
                startActivity(addSharingDetails);
            }
        });

        addAllergyDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAllergyDetails = new Intent(SettingsActivity.this, AddAllergies.class);
                startActivity(addAllergyDetails);
            }
        });

        setDateTimeField();
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

}
