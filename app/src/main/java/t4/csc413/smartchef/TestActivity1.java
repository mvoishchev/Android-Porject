package t4.csc413.smartchef;

import android.content.res.TypedArray;
import android.os.Bundle;

/*
 * Created by: MG
 */


/*
 * Activities that want to use the nav drawer must extend NavBaseActivity
 * and must manually add the two private data fields and override
 * onCreate method.  Provides flexibility of changing icons and text for
 * every activity.
 */
public class TestActivity1 extends NavBaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity1);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml

        set(navMenuTitles, navMenuIcons);
    }
}
