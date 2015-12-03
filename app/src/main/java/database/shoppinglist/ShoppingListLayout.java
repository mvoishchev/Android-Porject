package database.shoppinglist;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import connectors.evernote.EvernoteManager;
import database.fridge.FridgeDB;
import database.fridge.FridgeLayout;
import database.fridge.IngredientDisplayAdapter;
import t4.csc413.smartchef.MainActivity;
import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;
import tools.Ingredient;
import tools.Recipe;


/**
 * Created by Harjt Randhawa based on Marc's RecipeDB
 *
 */

public class ShoppingListLayout extends NavBaseActivity {

    // DB
    public static ShoppingListDB db;

    // NavBar
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private EditText et;
    private Button button, evernote;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_layout);

        openDB();
        populateListViewDB();

        et = (EditText)findViewById(R.id.edit);
        et.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String emptyString = et.getText().toString();
                    if (TextUtils.isEmpty(emptyString)) {
                        et.setError("Please enter an ingredient!");
                    } else {
                        addIngredient();
                    }

                    //do something
                    //true because you handle the event
                    return true;
                }
                return false;
            }
        });

        button = (Button)findViewById(R.id.addIngredientBtn);
        button.setText("Add to Shopping List");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });


        evernote = (Button)findViewById(R.id.evernote);
        evernote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToEvernote();

            }
        });
        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    //TODO Connector to Evernote
    private void uploadToEvernote(){
        ArrayList<String> list = getIngredients();
        EvernoteManager instance = EvernoteManager.getInstance(this);
        instance.overwriteMainShoppingList(list.toString(), this);
        Toast.makeText(this, "Uploaded to Evernote", Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialize Database for lifetime of application
     * @param context
     */
    public static void init(Context context){
        db = new ShoppingListDB(context);
    }

    /**
     *
     * @param rec Recipe object to add to the database
     */
    public static void addToShoppingList(Recipe rec){
        ArrayList<Ingredient> ing = rec.getIngredients();
        ArrayList<String> owned = FridgeDB.GetIngredientsInFridge();

        openDB();
        for(int i = 0; i < ing.size(); i++){
            boolean have = false;
            String curr = ing.get(i).name;
            for(int o = 0; o < owned.size(); o++){
                String currOwned = owned.get(o);
                if(curr.contains(currOwned.toLowerCase()))
                    have = true;
            }
            if(!have)
                addIngredient(ing.get(i).name);
        }
        closeDB();

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

    /**
     *
     * @param ing Ingredient to add the database from another activity
     */
    private static void addIngredient(String ing){
        db.insertRow(ing);
    }

    /**
     * Method to add ingredient to database from current Activity
     */
    private void addIngredient() {
        String line = et.getText().toString();
        if(line.length() > 0) {
            db.insertRow(line);
            et.setText("");
        }

        Toast.makeText(this, "Ingredient added to Shopping list", Toast.LENGTH_SHORT).show();
        populateListViewDB();
    }

    /**
     * Method that checks the fridge for ingredients that user already has and generates a list of
     * missing ingredients
     * @return List of Ingredients that are not in the FridgeDB
     */
    public static ArrayList<String> GetShoppingList(){
        db.open();
        ArrayList<String> shopping = new ArrayList<String>();

        Cursor cursor = db.getAllRows();

        while(!cursor.isAfterLast()){
            String curr = cursor.getString(1);
            shopping.add(curr);
            cursor.moveToNext();
        }
        db.close();

        return shopping;
    }
    // removes recipes from db
    public void removeIngredient(int position) {
        Cursor cursor = db.getAllRows();
        cursor.move(position);
        db.deleteRow(cursor.getLong(0));
        populateListViewDB();
    }

    private ArrayList<String> getIngredients(){
        Cursor cursor = db.getAllRows();

        ArrayList<String> args = new ArrayList<String>();

        while(!cursor.isAfterLast()){
            args.add(cursor.getString(1));
            cursor.moveToNext();
        }

        return args;
    }

    /**
     * Method to reflect changes to the Database
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
