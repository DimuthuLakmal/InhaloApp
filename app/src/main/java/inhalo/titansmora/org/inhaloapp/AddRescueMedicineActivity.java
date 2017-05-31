package inhalo.titansmora.org.inhaloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AddRescueMedicineActivity extends AppCompatActivity {

    Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rescue_medicine);

        //generate list
        ArrayList<String[]> list = new ArrayList<String[]>();
        String[] listData = new String[]{"Accuneb","Albuterol"};
        String[] listData2 = new String[]{"Albuterol","Albuterol"};
        String[] listData3 = new String[]{"Alupent","Metaproterenol"};
        list.add(listData);
        list.add(listData2);
        list.add(listData3);

        //instantiate custom adapter
        AddMedicineAdapter adapter = new AddMedicineAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.medicine_list);
        lView.setAdapter(adapter);

        nextButton = (Button)findViewById(R.id.medicine_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent controllerMedicineActivity = new Intent(AddRescueMedicineActivity.this, AddControllerMedicineActivity.class);
                startActivity(controllerMedicineActivity);
            }
        });
    }
}
