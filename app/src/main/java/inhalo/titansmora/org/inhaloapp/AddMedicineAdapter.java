package inhalo.titansmora.org.inhaloapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kjtdi on 5/31/2017.
 */
public class AddMedicineAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private Context context;



    public AddMedicineAdapter(ArrayList<String[]> list, Context context) {
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
            view = inflater.inflate(R.layout.medicine_list_view, null);
        }

        //Handle TextView and display string from your list
        TextView listMedicineName = (TextView)view.findViewById(R.id.list_medicine_name);
        listMedicineName.setText(list.get(position)[0]);

        TextView listMedicineAlterName = (TextView)view.findViewById(R.id.list_medicine_alter_name);
        listMedicineAlterName.setText(list.get(position)[1]);


        return view;
    }
}
