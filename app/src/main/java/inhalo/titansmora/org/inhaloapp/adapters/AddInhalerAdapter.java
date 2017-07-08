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

import inhalo.titansmora.org.inhaloapp.AddInhalersActivity;
import inhalo.titansmora.org.inhaloapp.R;

/**
 * Created by kjtdi on 5/31/2017.
 */
public class AddInhalerAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private AddInhalersActivity context;


    public AddInhalerAdapter(ArrayList<String[]> list, AddInhalersActivity context) {
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
            view = inflater.inflate(R.layout.inhaler_list_view, null);
        }

        //Handle TextView and display string from your list
        final TextView listInhalerName = (TextView) view.findViewById(R.id.list_inhaler_name);
        listInhalerName.setText(list.get(position)[0]);

        final CheckBox inhalerCheckBox = (CheckBox)view.findViewById(R.id.inhalerCheckBox);
        if(list.get(position)[1].equals("true")) {
            inhalerCheckBox.setChecked(true);
            context.addInhaler(listInhalerName.getText().toString());
        } else {
            inhalerCheckBox.setChecked(false);
        }

        inhalerCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (inhalerCheckBox.isChecked()) {
                    context.addInhaler(listInhalerName.getText().toString());
                } else {
                    context.deleteInhalerDetails(listInhalerName.getText().toString());
                }
            }
        });

        return view;
    }
}