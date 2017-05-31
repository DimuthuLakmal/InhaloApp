package inhalo.titansmora.org.inhaloapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kjtdi on 5/31/2017.
 */
public class MedicineDetailsAdpater extends BaseAdapter implements ListAdapter {
    private ArrayList<ArrayList<String>[]> list = new ArrayList<ArrayList<String>[]>();
    private Context context;


    public MedicineDetailsAdpater(ArrayList<ArrayList<String>[]> list, Context context) {
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
            view = inflater.inflate(R.layout.medicine_details_list_view, null);
        }

        //Handle TextView and display string from your list
        Spinner amountSpinner = (Spinner) view.findViewById(R.id.amount_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext() , android.R.layout.simple_spinner_item, list.get(position)[0]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountSpinner.setAdapter(adapter);

        //Handle TextView and display string from your list
        Spinner formSpinner = (Spinner) view.findViewById(R.id.form_spinner);
        ArrayAdapter<String> formAdapter = new ArrayAdapter<String>(view.getContext() , android.R.layout.simple_spinner_item, list.get(position)[1]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formSpinner.setAdapter(formAdapter);

        //Handle TextView and display string from your list
        Spinner doseSpinner = (Spinner) view.findViewById(R.id.dose_spinner);
        ArrayAdapter<String> doseAdapter = new ArrayAdapter<String>(view.getContext() , android.R.layout.simple_spinner_item, list.get(position)[2]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doseSpinner.setAdapter(doseAdapter);

        //Handle TextView and display string from your list
        Spinner frequencySpinner = (Spinner) view.findViewById(R.id.frequency_spinner);
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(view.getContext() , android.R.layout.simple_spinner_item, list.get(position)[3]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);

        return view;
    }

}