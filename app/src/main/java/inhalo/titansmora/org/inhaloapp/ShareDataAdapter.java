package inhalo.titansmora.org.inhaloapp;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kjtdi on 5/29/2017.
 */
public class ShareDataAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String[]> list = new ArrayList<String[]>();
    private AddSharingDetails context;



    public ShareDataAdapter(ArrayList<String[]> list, AddSharingDetails context) {
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
                context.deleteContactDetails(list.get(position)[3], position);
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                final View mView = layoutInflaterAndroid.inflate(R.layout.share_person_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                final EditText nameText = (EditText)mView.findViewById(R.id.sharePersonName);
                final EditText emailText = (EditText)mView.findViewById(R.id.shareEmail);
                final EditText mobileText = (EditText)mView.findViewById(R.id.shareMobile);

                nameText.setText(list.get(position)[0]);
                emailText.setText(list.get(position)[1]);
                mobileText.setText(list.get(position)[2]);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                context.updateContactDetails(nameText.getText().toString().trim(), emailText.getText().toString().trim(), mobileText.getText().toString().trim(), list.get(position)[3] ,position);
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

                notifyDataSetChanged();
            }
        });

        return view;
    }
}
