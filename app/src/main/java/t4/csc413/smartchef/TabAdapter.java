package t4.csc413.smartchef;

/**
 * Custom Adapter for the tab on the Results information page (SlideMain)
 * Created by Thomas X Mei on 11/2/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

public class TabAdapter extends FragmentPagerAdapter
{
    public TabAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int i)
    {
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
    //set the number of tabs
    public int getCount()
    {
        return 4;
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Ingredients";
            case 1:
                return "Instructions";
            case 2:
                return "PrepTimer";
            case 3:
                return "Google Maps & more";
        }
        return null;
    }
}
