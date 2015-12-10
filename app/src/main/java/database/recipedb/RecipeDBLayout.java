package database.recipedb;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;

import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;
import tools.Recipe;


/**
 * Created by Marc
 * Database to handle saved recipes
 * Extends NavBaseActivity in order to have a NavDrawer
 */

public class RecipeDBLayout extends NavBaseActivity {

    // DB
    public static DBAdapter db;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_db_layout);

        openDB();
        populateListViewDB();

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    public static void init(Context context){
        db = new DBAdapter(context);
    }

    //when closing activity, close db
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    //open db for use
    private static void openDB() {
        db.open();
    }

    //close db when not in use
    private static void closeDB() {
        db.close();
    }

    /**
     * Method to handle adding recipes from other activities
     * @param rec adds recipe to DB
     */
    public static void addRecipeToDB(Recipe rec){
        openDB();
        db.insertRow(rec.getName(), rec.getRecipeUrl());
        closeDB();
    }

    /**
     * Gets name of recipe
     * @param position Position of cursor to iterate through rows
     * @return Returns recipe name
     */
    public static String getRecipeTitle(int position){
        String title;
        Cursor cursor = db.getAllRows();
        cursor.moveToPosition(position);
        title = cursor.getString(1);
        return title;
    }

    /**
     * Removes all recipes from DB
     * @param v OnClick(View v)
     */
    public void removeAllRecipes(View v) {
        db.deleteAll();
        populateListViewDB();
    }

    /**
     * Removes recipe from given row
     * @param position Position of cursor to iterate through rows
     */
    public void removeRecipe(int position) {
        Cursor cursor = db.getAllRows();
        cursor.move(position);
        db.deleteRow(cursor.getLong(0));
        populateListViewDB();
    }

    /**
     * Retrieves recipe url
     * @param position Position of cursor to iterate through rows
     * @return Returns string url at given position
     */
    public static String getRecipeUrl(int position){
        String url;
        Cursor cursor = db.getAllRows();
        cursor.moveToPosition(position);
        url = cursor.getString(2);
        return url;
    }

    /**
     *
     * @return List of all recipes stored in Recipe DB
     */
    public static ArrayList<String> getRecipes(){
        Cursor cursor = db.getAllRows();
        ArrayList<String> args = new ArrayList<String>();
        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }
        return args;
    }

    /**
     * Populates database with a listview layout
     * Method to populate database with a list view
     */
    private void populateListViewDB() {
        Cursor cursor = db.getAllRows();
        ArrayList<String> args = new ArrayList<String>();
        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }
        RecipeDisplayAdapter displayAdapter = new RecipeDisplayAdapter(args, this);
        // set adapter
        ListView myList = (ListView) findViewById(R.id.listViewRecipeDB);
        myList.setAdapter(displayAdapter);
    }

}
