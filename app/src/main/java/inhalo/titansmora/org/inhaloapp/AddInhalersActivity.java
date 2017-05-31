package inhalo.titansmora.org.inhaloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AddInhalersActivity extends AppCompatActivity {

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inhalers);

        nextButton = (Button)findViewById(R.id.inhaler_next_button);
        //generate list
        ArrayList<String> list = new ArrayList<String>();;
        list.add("Metered-Dose Inhaler");
        list.add("MDI with Spacer");
        list.add("Dry-Powder Inhaler");

        //instantiate custom adapter
        AddAlergyAdapter adapter = new AddAlergyAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.inhaler_list);
        lView.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rescueMedicineIntent = new Intent(AddInhalersActivity.this, AddRescueMedicineActivity.class);
                startActivity(rescueMedicineIntent);
            }
        });
    }
}
