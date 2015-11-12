package t4.csc413.smartchef;

/**
 * Created by Thomas X Mei on 11/2/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
               FragA f1 = new FragA();
                return f1;
            case 1:
                FragB f2 = new FragB();
                return f2;
            case 2:
                FragC f3 = new FragC();
                return f3;
            case 3:
                FragD f4 = new FragD();
                return f4;

        }
        return null;
    }

    public int getCount()
    {
        return 4;
    }//set the number of tabs


    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "General Information";
            case 1:
                return "Instructions";
            case 2:
                return "Preperation";
            case 3:
                return "Extra Options";
        }
        return null;
    }
}
