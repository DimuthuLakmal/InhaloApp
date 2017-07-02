package inhalo.titansmora.org.inhaloapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import inhalo.titansmora.org.inhaloapp.AddAllergiesActivity;
import inhalo.titansmora.org.inhaloapp.R;

/**
 * Created by kjtdi on 5/31/2017.
 */
public class AddAlergyAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private AddAllergiesActivity context;



    public AddAlergyAdapter(ArrayList<String[]> list, AddAllergiesActivity context) {
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
            view = inflater.inflate(R.layout.allergy_list_view, null);
        }

        //Handle TextView and display string from your list
        final TextView listAllergyName = (TextView)view.findViewById(R.id.list_allergy_name);
        listAllergyName.setText(list.get(position)[0]);

        final CheckBox allergyCheckBox = (CheckBox)view.findViewById(R.id.allergyCheckBox);
        if(list.get(position)[1].equals("true")) {
            allergyCheckBox.setChecked(true);
        } else {
            allergyCheckBox.setChecked(false);
        }

        allergyCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(allergyCheckBox.isChecked()){
                    context.addAllergenDetails(listAllergyName.getText().toString());
                }else{
                    context.deleteAllergyDetails(listAllergyName.getText().toString());
                }
            }
        });
        return view;
    }
}
