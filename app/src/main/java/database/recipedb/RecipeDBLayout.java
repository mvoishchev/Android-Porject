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

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private static void openDB() {
        db.open();
    }

    private static void closeDB() {
        db.close();
    }

    // add recipe to db
    public static void addRecipeToDB(Recipe rec){
        openDB();
        db.insertRow(rec.getName(), rec.getRecipeUrl());
        closeDB();
    }

    public static String getRecipeTitle(int position){
        String title;
        Cursor cursor = db.getAllRows();
        cursor.moveToPosition(position);
        title = cursor.getString(1);
        return title;
    }

    // removes recipes from db
    public void removeAllRecipes(View v) {
        db.deleteAll();
        populateListViewDB();
    }

    public void removeRecipe(int position) {
        Cursor cursor = db.getAllRows();
        cursor.move(position);
        db.deleteRow(cursor.getLong(0));
        populateListViewDB();
    }

    public static String getRecipeUrl(int position){

        String url;
        Cursor cursor = db.getAllRows();
        cursor.moveToPosition(position);
        url = cursor.getString(2);
        return url;
    }


    public static ArrayList<String> getRecipes(){
        Cursor cursor = db.getAllRows();

        ArrayList<String> args = new ArrayList<String>();

        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }
        return args;
    }

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
