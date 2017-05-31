package inhalo.titansmora.org.inhaloapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class AddAllergies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergies);

        //generate list
        ArrayList<String> list = new ArrayList<String>();;
        list.add("Animal Dander");
        list.add("Cockroaches");
        list.add("Dust Mites");
        list.add("Indoor Mold");
        list.add("Medicines");
        list.add("Pollen and Outdoor Mold");
        list.add("Pollution");
        list.add("Smoke");
        list.add("Strong Odor");
        list.add("Vaccum Cleaning");
        list.add("Viral Illness");
        list.add("Weather");

        //instantiate custom adapter
        AddAlergyAdapter adapter = new AddAlergyAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.add_allergy_list);
        lView.setAdapter(adapter);
    }
}
