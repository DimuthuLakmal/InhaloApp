package inhalo.titansmora.org.inhaloapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddSharingDetails extends AppCompatActivity {

    Button openDialogBoxButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sharing_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //generate list
        ArrayList<String[]> list = new ArrayList<String[]>();
        String[] listData = new String[]{"Dr.Deemantha","deemantha12.@gmail.com","+947128484738"};
        String[] listData2 = new String[]{"Mr.Kumarasiri","kumarasiri.@gmail.com","+94712432485"};
        list.add(listData);
        list.add(listData2);

        //instantiate custom adapter
        ShareDataAdapter adapter = new ShareDataAdapter(list, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.shareDetailsListView);
        lView.setAdapter(adapter);

        openDialogBoxButton = (Button) findViewById(R.id.openDialogBoxButton);
        openDialogBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(AddSharingDetails.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.share_person_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AddSharingDetails.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText sharePersonName = (EditText) mView.findViewById(R.id.sharePersonName);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
    }

}
