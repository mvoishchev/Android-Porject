package database.recipedb;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;
import tools.Recipe;


/**
 * Created by Marc
 * NOTE:  other classes(evernote) can use methods
 *          addRecipe
 *          removeRecipe
 *          getRecipe
 *
 * CHECK TO DO.  Small minor problem
 */

public class RecipeDBLayout extends NavBaseActivity implements RecipeInterFace {

    // DB
    DBAdapter db;
    Recipe recipe;

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

    /*
     * TODO: make it so it actually accepts recipe name and recipe url from Harjit's recipe class
     * Recipe.getRecipeName();
     * Recipe.getRecipeUrl();
     */
    public void addRecipe(View v) {
        db.insertRow("RECIPE NAME", "RECIPE URL");
        populateListViewDB();
    }

    // removes recipes from db
    public void removeRecipe(View v) {
        db.deleteAll();
        populateListViewDB();
    }

    // get all recipes from db
    public void getRecipe(View v) {
        db.getAllRows();
    }

    private void populateListViewDB() {
        Cursor cursor = db.getAllRows();

        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_RECIPENAME, DBAdapter.KEY_RECIPESRCURL};
        int[] toViewIDs = new int[]
                {R.id.recipe_name,      R.id.recipe_url};

        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.recipe_db_listview,
                        cursor,
                        fromFieldNames,
                        toViewIDs);

        // set adapter
        ListView myList = (ListView) findViewById(R.id.listViewRecipeDB);
        myList.setAdapter(myCursorAdapter);
    }

}
