package database.RecipeDataBase;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;

/**
 * Created by Marc
 */

public class RecipeDBLayout extends NavBaseActivity {
    DBAdapter db;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_db_layout);

        openDB();

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        db = new DBAdapter(this);
        db.open();
    }

    private void closeDB() {
        db.close();
    }

    public void onClick_AddRecipe(View v) {
        displayText("CLICKED ADD");
        long newId = db.insertRow("INSERT URL HERE", "RECIPE NAME HERE", "RECIPE IMAGE URL HERE");

        Cursor cursor = db.getRow(newId);
        displayRecordSet(cursor);
    }

    public void onClick_DeleteAll(View v) {
        displayText("CLICKED DELETE");
        db.deleteAll();
    }

    public void displayRecipes(View v) {
        Cursor cursor = db.getAllRows();
        displayRecordSet(cursor);
    }

    // display recorded list of user recipes
    private void displayRecordSet(Cursor cursor) {
        String message = "";

        if(cursor.moveToFirst()) {
            // process data
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String recipeURL = cursor.getString(DBAdapter.COL_RECIPESRCURL);
                String recipeName = cursor.getString(DBAdapter.COL_RECIPENAME);
                String recipeImageURL = cursor.getString(DBAdapter.COL_RECIPEIMGURL);

                // append data to message
                message += "ID = " + id
                        + " RECIPE URL: " + recipeURL
                        + "\n"
                        + " RECIPE NAME: " + recipeName
                        + "\n"
                        + " RECIPE IMG URL: " + recipeImageURL
                        + "\n";
            } while(cursor.moveToNext());
        }
        cursor.close();
        displayText(message);
    }
}
