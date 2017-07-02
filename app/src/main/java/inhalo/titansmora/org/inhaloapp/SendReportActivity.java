package inhalo.titansmora.org.inhaloapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import inhalo.titansmora.org.inhaloapp.adapters.SendReportAdapter;

public class SendReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        //generate list
        ArrayList<String[]> list = new ArrayList<String[]>();
        String[] listData = new String[]{"Dr.Deemantha","deemantha12.@gmail.com","+947128484738"};
        String[] listData2 = new String[]{"Mr.Kumarasiri","kumarasiri.@gmail.com","+94712432485"};
        list.add(listData);
        list.add(listData2);

        //instantiate custom adapter
        SendReportAdapter adapter = new SendReportAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.sendReportListView);
        lView.setAdapter(adapter);
    }
}
