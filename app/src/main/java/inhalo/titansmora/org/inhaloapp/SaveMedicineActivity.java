package inhalo.titansmora.org.inhaloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SaveMedicineActivity extends AppCompatActivity {

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_medicine);

        //generate list
        ArrayList<ArrayList<String>[]> list = new ArrayList<ArrayList<String>[]>();
        ArrayList<String> amountSpinnerDetails = new ArrayList<String>();
        amountSpinnerDetails.add("0.63 mg");
        amountSpinnerDetails.add("0.25 mg");

        ArrayList<String> formSpinnerDetails = new ArrayList<String>();
        formSpinnerDetails.add("Vial");

        ArrayList<String> doseSpinnerDetails = new ArrayList<String>();
        doseSpinnerDetails.add("1.0");
        doseSpinnerDetails.add("2.0");

        ArrayList<String> frequencySpinnerDetails = new ArrayList<String>();
        frequencySpinnerDetails.add("As Need");
        frequencySpinnerDetails.add("1/day");
        frequencySpinnerDetails.add("2/day");

        ArrayList<String>[] a = (ArrayList<String>[])new ArrayList[4];
        a[0] = amountSpinnerDetails;
        a[1] = formSpinnerDetails;
        a[2] = doseSpinnerDetails;
        a[3] = frequencySpinnerDetails;
        list.add(a);

        //instantiate custom adapter
        MedicineDetailsAdpater adapter = new MedicineDetailsAdpater(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.medicineDetailList);
        lView.setAdapter(adapter);

        saveButton = (Button)findViewById(R.id.medicine_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(SaveMedicineActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
}
