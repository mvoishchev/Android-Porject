package database.fridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 *  Created by Harjit Randhawa
 *  DB to handle Fridge
 */

public class FridgeDB {

    private static final String TAG = "DBAdapterIngredients";

    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    public static final String KEY_INGREDIENTNAME = "ingredientname";

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_INGREDIENTNAME};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "IngredientDataBase";
    public static final String DATABASE_TABLE = "ingredientTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_INGREDIENTNAME + " string UNIQUE"
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public FridgeDB(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    /**
     *
     * @return List of Ingredients stored in fridge
     */
    public static ArrayList<String> GetIngredientsInFridge(){
        ArrayList<String> args = new ArrayList<String>();
        if(FridgeLayout.db != null) {
            FridgeLayout.db.open();
            Cursor cursor = FridgeLayout.db.getAllRows();

            while (!cursor.isAfterLast()) {
                args.add(cursor.getString(1));
                cursor.moveToNext();
            }

            FridgeLayout.db.close();
        }
        return args;
    }

    /**
     *
     * @return CharSequence[] of Ingredients stored in the FridgeDB
     */
    public static CharSequence[] GetIngredientsForFridgeButton(){
        ArrayList<String> lines = GetIngredientsInFridge();
        CharSequence[] ingredients = new CharSequence[lines.size() + 1];
        ingredients[0] = "Use Entire Fridge";
        for(int line = 1; line <= lines.size(); line++){
            ingredients[line] = lines.get(line - 1);
        }

        return ingredients;
    }

    /**
     *
     * @return FridgeDB opened for writing
     */
    public FridgeDB open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close current open database
     */
    public void close() {
        myDBHelper.close();
    }

    /**
     *
     * @param ingredientName Ingredient to be added to database
     * @return Location in database of Ingredient stored
     */
    public long insertRow(String ingredientName) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_INGREDIENTNAME, ingredientName);
        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     *
     * @param rowId RowID of the Ingredient to delete
     * @return True if the deletion was successful
     */
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    /**
     *
     * @return Cursor object placed at the first row in database
     */
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /**
     *
     * @param rowId RowID of the desired location in database
     * @return Cursor positioned at the location of desired Ingredient
     */
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

     // Private class from Marc
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
