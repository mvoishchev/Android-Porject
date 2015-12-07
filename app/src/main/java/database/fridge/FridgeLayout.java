package database.fridge;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import t4.csc413.smartchef.MainActivity;
import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;

public class FridgeLayout extends NavBaseActivity {

    // DB
    public static FridgeDB db;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private EditText et;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_db_layout);

        openDB();
        populateListViewDB();

        et = (EditText)findViewById(R.id.edit);
        et.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String emptyString = et.getText().toString();
                    if(TextUtils.isEmpty(emptyString)) {
                        et.setError("Please enter an ingredient!");
                    }else {
                        addIngredient();
                    }

                    //do something
                    //true because you handle the event
                    return true;
                }
                return false;
            }
        });
        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    /**
     * Initialize FridgeDB
     * @param context Activity to bind the Database to
     */
    public static void init(Context context){
        db = new FridgeDB(context);
    }

    protected void onDestroy() {
        super.onDestroy();
        MainActivity.updateInstance();
        closeDB();
    }

    private static void openDB() {
        db.open();
    }

    private static void closeDB() {
        db.close();
    }

    /**
     * Method to add Ingredient to the database
     * @param v Mandatory argument for OnClick(View v)
     */
    public void addIngredient(View v) {
       addIngredient();
    }

    private void addIngredient(){
        String line = et.getText().toString();
        if(line.length() > 0) {
            db.insertRow(line);
            et.setText("");
        }
        Toast.makeText(this, "Ingredient added to Fridge", Toast.LENGTH_SHORT).show();
        populateListViewDB();
    }

    /**
     *
     * @param position position of ingredient on layout to delete from database
     */
    public void removeIngredient(int position) {
        Cursor cursor = db.getAllRows();
        cursor.move(position);
        db.deleteRow(cursor.getLong(0));
        populateListViewDB();
    }

    /**
     *
     * @return List of all ingredients stored in FridgeDB
     */
    private static ArrayList<String> getIngredients(){
        Cursor cursor = db.getAllRows();

        ArrayList<String> args = new ArrayList<String>();

        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }
        return args;
    }

    /**
     * Method to update Activity to show changes in FridgeDB
     */
    private void populateListViewDB() {
        Cursor cursor = db.getAllRows();

        ArrayList<String> args = new ArrayList<String>();

        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }

        IngredientDisplayAdapter displayAdapter = new IngredientDisplayAdapter(args, this);
        // set adapter
        ListView myList = (ListView) findViewById(R.id.listViewIngredientDB);
        myList.setAdapter(displayAdapter);
    }

}
