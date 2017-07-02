package inhalo.titansmora.org.inhaloapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import inhalo.titansmora.org.inhaloapp.AddDoctorsActivity;
import inhalo.titansmora.org.inhaloapp.R;

/**
 * Created by kjtdi on 6/17/2017.
 */
public class AddDoctorAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private AddDoctorsActivity context;



    public AddDoctorAdapter(ArrayList<String[]> list, AddDoctorsActivity context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.share_doctor_details_list_view, null);
        }

        //Handle TextView and display string from your list
        TextView listSharedNameText = (TextView)view.findViewById(R.id.list_share_person_name);
        listSharedNameText.setText(list.get(position)[0]);

        TextView listHospital = (TextView)view.findViewById(R.id.list_share_hospital);
        listHospital.setText(list.get(position)[1]);

        TextView listSharedMobile = (TextView)view.findViewById(R.id.list_share_mobile);
        listSharedMobile.setText(list.get(position)[2]);

        //Handle buttons and add onClickListeners
        Button add_btn = (Button)view.findViewById(R.id.add_doctor_btn);

        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                context.addDoctor(list.get(position)[3]);
            }
        });

        return view;
    }
}
