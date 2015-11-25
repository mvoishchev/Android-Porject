package database.allergiesDB;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;

/**
 * Created by MG on 11/23/2015.
 */
public class AllergiesDBLayout extends NavBaseActivity {

    // DB
    DBAdapterAllergies db;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allergies_db_layout);

        openDB();
        populateListViewDB();

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        db = new DBAdapterAllergies(this);
        db.open();
    }

    private void closeDB() {
        db.close();
    }

    public void addAllergies(View v) {
        db.insertRow("ALLERGY HERE");
        populateListViewDB();
    }

    // removes allergies from db
    public void removeAllergies(View v) {
        db.deleteAll();
        populateListViewDB();
    }

    // get all allergies from db
    public void getAllergies(View v) {
        db.getAllRows();
    }

    private void populateListViewDB() {
        Cursor cursor = db.getAllRows();

        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {DBAdapterAllergies.KEY_ALLERGYNAME};
        int[] toViewIDs = new int[]
                {R.id.allergy};

        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.allergies_db_listview,
                        cursor,
                        fromFieldNames,
                        toViewIDs);

        // set adapter
        ListView myList = (ListView) findViewById(R.id.listViewAllergiesDB);
        myList.setAdapter(myCursorAdapter);
    }

}
