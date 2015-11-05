package t4.csc413.smartchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import connectors.SearchTools;
import tools.Recipe;

public class FragC extends android.support.v4.app.Fragment {
    static TextView v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_frag_c,container,false);

        SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFC);

        int prep_Hours = m.rr.getPrepTime_hours();
        int prep_Minutes = m.rr.getPrepTime_minutes();

        String text = "\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tPreperation Time:\n\t\t\t\t\t\t" +
                "\t\t\t\t\t\tHours: " + prep_Hours +
                "\t\t\tMinutes: " + prep_Minutes;
        v.setText(text);


        return  view;
    }
}
