package inhalo.titansmora.org.inhaloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import inhalo.titansmora.org.inhaloapp.connections.HTTPConstants;
import inhalo.titansmora.org.inhaloapp.connections.RequestHandler;

public class DataGatherActivity extends AppCompatActivity {


    ArrayList<Entry> yAXES = new ArrayList<>();
    ArrayList<Entry> yAXESTwoMonth = new ArrayList<>();
    final ArrayList<String> lastMonthDates = new ArrayList<String>();
    final ArrayList<String> lastTwoMonthDates = new ArrayList<String>();
    final ArrayList<String> lastMonthSums = new ArrayList<String>();
    final ArrayList<String> lastTwoMonthSums = new ArrayList<String>();
    private String userId;
    private int allDataCollected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_gather);

        userId = getIntent().getStringExtra("userId");

        retreiveUserData(HTTPConstants.URL_RETREIVE_PAST_DATA, 0, yAXES, lastMonthDates);
        retreiveUserData(HTTPConstants.URL_RETREIVE_PAST_DATA, 1, yAXESTwoMonth, lastTwoMonthDates);
    }

    private void retreiveUserData(String URL, final int category, final ArrayList<Entry> graphEntries, final ArrayList<String> previousDates ) {

        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String endDate = null;
        String startDate = null;
        if(category == 0) {

            endDate = sdfDate.format(now);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            startDate = sdfDate.format(cal.getTime());

        } else if(category == 1) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            endDate = sdfDate.format(cal.getTime());

            cal.add(Calendar.MONTH, -1);
            startDate = sdfDate.format(cal.getTime());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL+userId+"/enddate/"+endDate+"/startdate/"+startDate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataPefArray = new JSONObject(response).getJSONArray("dataPefs");
                            JSONArray dataSumsArray = new JSONObject(response).getJSONArray("dataSums");


                            Date endDate = null;
                            Date startDate = null;
                            if(category == 0) {
                                allDataCollected++;
                                JSONObject sum = dataSumsArray.getJSONObject(0);
                                lastMonthSums.add(sum.getString("sum(wheeze)"));
                                lastMonthSums.add(sum.getString("sum(cough)"));
                                lastMonthSums.add(sum.getString("sum(sputm)"));
                                lastMonthSums.add(sum.getString("sum(cold)"));
                                lastMonthSums.add(sum.getString("sum(bother_sleep)"));
                                lastMonthSums.add(sum.getString("sum(chest_tightness)"));
                                lastMonthSums.add(sum.getString("sum(short_breath)"));
                                lastMonthSums.add(sum.getString("sum(physical_activity)"));
                                lastMonthSums.add(sum.getString("sum(nebulized)"));
                                lastMonthSums.add(sum.getString("sum(asthma_condition)"));
                                lastMonthSums.add(sum.getString("sum(times_awake)"));
                                lastMonthSums.add(sum.getString("sum(allergy)"));

                                endDate = new Date();

                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.MONTH, -1);
                                startDate = cal.getTime();

                            } else if(category == 1) {
                                allDataCollected++;
                                JSONObject sum = dataSumsArray.getJSONObject(0);
                                lastTwoMonthSums.add(sum.getString("sum(wheeze)"));
                                lastTwoMonthSums.add(sum.getString("sum(cough)"));
                                lastTwoMonthSums.add(sum.getString("sum(sputm)"));
                                lastTwoMonthSums.add(sum.getString("sum(cold)"));
                                lastTwoMonthSums.add(sum.getString("sum(bother_sleep)"));
                                lastTwoMonthSums.add(sum.getString("sum(chest_tightness)"));
                                lastTwoMonthSums.add(sum.getString("sum(short_breath)"));
                                lastTwoMonthSums.add(sum.getString("sum(physical_activity)"));
                                lastTwoMonthSums.add(sum.getString("sum(nebulized)"));
                                lastTwoMonthSums.add(sum.getString("sum(asthma_condition)"));
                                lastTwoMonthSums.add(sum.getString("sum(times_awake)"));
                                lastTwoMonthSums.add(sum.getString("sum(allergy)"));

                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.MONTH, -1);
                                endDate = cal.getTime();

                                cal.add(Calendar.MONTH, -1);
                                startDate = cal.getTime();
                            }

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(startDate);

                            while (calendar.getTime().before(endDate))
                            {
                                Date result = calendar.getTime();
                                previousDates.add(sdfDate.format(result));
                                calendar.add(Calendar.DATE, 1);
                            }
                            if(category == 0) {
                                previousDates.add(sdfDate.format(new Date()));
                            }

                            int index = 0;
                            for(String previousDate: previousDates) {
                                boolean isExistInRecord = false;
                                for(int i=0,size = dataPefArray.length(); i < size; i++) {
                                    JSONObject data = dataPefArray.getJSONObject(i);

                                    if(data.getString("date").split("T")[0].equals(previousDate)) {
                                        isExistInRecord = true;
                                        if(!data.getString("pef").equals("null")) {
                                            graphEntries.add(new Entry(Float.parseFloat(String.valueOf(index)), Float.parseFloat(String.valueOf(data.getString("pef")))));
                                        } else {
                                            graphEntries.add(new Entry(Float.parseFloat(String.valueOf(index)), 0.0f));
                                        }
                                    }
                                }
                                if(!isExistInRecord) {
                                    graphEntries.add(new Entry(Float.parseFloat(String.valueOf(index)), 0.0f));
                                }
                                index++;
                            }

                            if(allDataCollected == 2) {
                                Intent history = new Intent(DataGatherActivity.this, TrackedData.class);
                                history.putExtra("userId", userId);
                                history.putExtra("entries", yAXES);
                                history.putExtra("entriesTwoMonth", yAXESTwoMonth);
                                history.putExtra("lastMonthDates", lastMonthDates);
                                history.putExtra("lastTwoMonthDates", lastTwoMonthDates);
                                history.putExtra("lastMonthSums", lastMonthSums);
                                history.putExtra("lastTwoMonthSums", lastTwoMonthSums);
                                startActivity(history);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
