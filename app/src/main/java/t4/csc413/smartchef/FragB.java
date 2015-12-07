package t4.csc413.smartchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import connectors.SearchTools;
import tools.Recipe;
/**
 *
 * Fragment to display information for instructions
 * Created by Thomas X Mei
 */

public class FragB extends android.support.v4.app.Fragment
{
    static TextView v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_frag_b,container,false);
        SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFB);
        String instructions = m.rr.getInstructions();
        String text = "Instructions:\n\n" +instructions + "\n";
        v.setText(text);
        return  view;
    }
}
