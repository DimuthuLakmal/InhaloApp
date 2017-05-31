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
 * Created by kjtdi on 5/29/2017.
 */
public class ShareDataAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private Context context;



    public ShareDataAdapter(ArrayList<String[]> list, Context context) {
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
            view = inflater.inflate(R.layout.share_list_view, null);
        }

        //Handle TextView and display string from your list
        TextView listSharedNameText = (TextView)view.findViewById(R.id.list_share_person_name);
        listSharedNameText.setText(list.get(position)[0]);

        TextView listSharedEmail = (TextView)view.findViewById(R.id.list_share_email);
        listSharedEmail.setText(list.get(position)[1]);

        TextView listSharedMobile = (TextView)view.findViewById(R.id.list_share_mobile);
        listSharedMobile.setText(list.get(position)[2]);

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button update_btn = (Button)view.findViewById(R.id.update_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
