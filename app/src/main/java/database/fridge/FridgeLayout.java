package database.fridge;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;


/**
 * Created by Marc
 * NOTE:  other classes(evernote) can use methods
 *          addRecipe - manually insert recipe to db.insertRow()
 *                      1) instantiate DBAdapter
 *                      2) use object to access DBAdapter's insertRow function
 *          removeRecipe
 *          getRecipe
 *
 * CHECK TO DO.  Small minor problem
 */

public class FridgeLayout extends NavBaseActivity implements FridgeInterface {

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
        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    public static void init(Context context){
        db = new FridgeDB(context);
    }

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private static void openDB() {
        //db = new DBAdapter(this);
        db.open();
    }

    private static void closeDB() {
        db.close();
    }

    /*
     * TODO: make it so it actually accepts recipe name and recipe url from Harjit's recipe class
     * Recipe.getRecipeName();
     * Recipe.getRecipeUrl();
     */
    public void addIngredient(View v) {
        String line = et.getText().toString();
        if(line.length() > 0)
            db.insertRow(line);

        populateListViewDB();
    }

    // removes recipes from db
    public void removeIngredient(int position) {
        Cursor cursor = db.getAllRows();
        cursor.move(position);
        db.deleteRow(cursor.getLong(0));
        populateListViewDB();
    }

    public static ArrayList<String> getIngredients(){
        ArrayList<String> ingredients = new ArrayList<String>();
        String[] data = db.getAllRows().getColumnNames();
        for(int ingredient = 0; ingredient < data.length; ingredient++){
            ingredients.add(data[ingredient]);
        }
        return ingredients;
    }

    // get all recipes from db
    public void getRecipe(View v) {
        db.getAllRows();
    }

    private void populateListViewDB() {
        Cursor cursor = db.getAllRows();

        String[] names = cursor.getColumnNames();

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
