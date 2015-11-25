package database.shoppinglist;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import database.fridge.IngredientDisplayAdapter;
import t4.csc413.smartchef.MainActivity;
import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;
import tools.Ingredient;
import tools.Recipe;


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

public class ShoppingListLayout extends NavBaseActivity {

    // DB
    public static ShoppingListDB db;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private EditText et;
    private Button button;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_layout);

        openDB();
        populateListViewDB();

        et = (EditText)findViewById(R.id.edit);
        button = (Button)findViewById(R.id.addIngredientBtn);
        button.setText("Add to Shopping List");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    public static void init(Context context){
        db = new ShoppingListDB(context);
    }

    public static void addToShoppingList(Recipe rec){
        ArrayList<Ingredient> ing = rec.getIngredients();

        openDB();
        for(int i = 0; i < ing.size(); i++){
            addIngredient(ing.get(i).original_discription);
        }
        closeDB();

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

    private static void addIngredient(String ing){
        db.insertRow(ing);
    }
    private void addIngredient() {
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
        Cursor cursor = db.getAllRows();

        String[] names = cursor.getColumnNames();

        ArrayList<String> args = new ArrayList<String>();

        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }

        return args;
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
