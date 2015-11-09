package database;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import database.DataBaseManager;
import database.RecipeDB;
import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;

/**
 * Created by Marc on 11/4/2015.
 */
public class DataBaseLayout extends NavBaseActivity {

    EditText recipeInput;
    TextView recipeText;
    DataBaseManager dbHandler;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_layout);
        recipeInput = (EditText) findViewById(R.id.userInput);
        recipeText = (TextView) findViewById(R.id.recipeText);
        dbHandler = new DataBaseManager(this, null, null, 1);

        // DB
        printDataBase();

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    public void addButtonClicked(View view) {
        RecipeDB recipes = new RecipeDB(recipeInput.getText().toString());
        dbHandler.addRecipe(recipes);
        printDataBase();

    }

    public void deleteButtonClicked(View view) {
        String inputText = recipeInput.getText().toString();
        dbHandler.deleteRecipe(inputText);
        printDataBase();
    }

    public void printDataBase() {
        String dbString = dbHandler.dbToString();
        recipeText.setText(dbString);
        recipeInput.setText("");
    }
}
