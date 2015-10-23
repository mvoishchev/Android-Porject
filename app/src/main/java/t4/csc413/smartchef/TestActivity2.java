package t4.csc413.smartchef;

import android.content.res.TypedArray;
import android.os.Bundle;

/*
 * Created by: MG
 */

public class TestActivity2 extends NavBaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity2);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml

        set(navMenuTitles, navMenuIcons);
    }
}
