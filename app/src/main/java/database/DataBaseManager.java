package database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;


/**
 * Created by MG on 10/28/2015.
 * This class is responsible for handling the database
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipes.db";
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECIPENAME = "recipename";

    public DataBaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //creating table for db
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_RECIPES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_RECIPENAME + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);  //execute the query
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //delete current table and create a new one
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_RECIPES); //DROP means delete in SQL
        onCreate(sqLiteDatabase);
    }

    //adding new row to database
    public void addRecipe(RecipeDB recipes) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPENAME, recipes.get_recipename());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_RECIPES, null, values);
        sqLiteDatabase.close();
    }

    //deleting recipe from database
    public void deleteRecipe(String recipeName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_RECIPES + " WHERE "
                + COLUMN_RECIPENAME + "=\"" + recipeName + "\";");
    }


    //print out db as a string
    public String dbToString() {
        String dbString = "";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RECIPES + " WHERE 1";

        //cursor point to a location
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //move to the first row
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            if(cursor.getString(cursor.getColumnIndex("recipename"))!=null) {
                dbString += cursor.getString(cursor.getColumnIndex("recipename"));
                dbString += "\n";
            }
            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return dbString;
    }

}
