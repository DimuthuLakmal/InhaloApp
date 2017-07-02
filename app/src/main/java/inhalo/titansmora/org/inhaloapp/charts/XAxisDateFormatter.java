package inhalo.titansmora.org.inhaloapp.charts;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by kjtdi on 6/11/2017.
 */
public class XAxisDateFormatter implements IAxisValueFormatter {
    private final static String logTAG = XAxisDateFormatter.class.getName() + ".";

    private ArrayList<String> dates;

    public XAxisDateFormatter(ArrayList<String> dates) {
        this.dates = dates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(value%1 > 0){
            return "";
        }else {
            return dates.get(Integer.parseInt(String.valueOf(Math.round(value))));
        }

    }
}
