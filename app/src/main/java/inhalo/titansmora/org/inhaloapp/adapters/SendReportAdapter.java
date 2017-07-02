package inhalo.titansmora.org.inhaloapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import inhalo.titansmora.org.inhaloapp.R;

/**
 * Created by kjtdi on 5/31/2017.
 */
public class SendReportAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private Context context;



    public SendReportAdapter(ArrayList<String[]> list, Context context) {
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
            view = inflater.inflate(R.layout.send_report_list, null);
        }

        //Handle TextView and display string from your list
        TextView listSharedNameText = (TextView)view.findViewById(R.id.list_share_person_name);
        listSharedNameText.setText(list.get(position)[0]);

        TextView listSharedEmail = (TextView)view.findViewById(R.id.list_share_email);
        listSharedEmail.setText(list.get(position)[1]);

        TextView listSharedMobile = (TextView)view.findViewById(R.id.list_share_mobile);
        listSharedMobile.setText(list.get(position)[2]);

        //Handle buttons and add onClickListeners
        Button shareButton = (Button)view.findViewById(R.id.share_btn);

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
