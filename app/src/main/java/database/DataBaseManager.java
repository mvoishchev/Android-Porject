package database;

import android.content.Context;

import java.util.ArrayList;

import database.fridge.FridgeLayout;
import database.recipedb.RecipeDBLayout;
import database.shoppinglist.ShoppingListLayout;
import tools.Recipe;

/**
 * Created by Harjit Randhawa on 11/25/2015.
 */
public class DataBaseManager {

    /**
     *
     * @param context Context to bind the Databases to
     */
    public static void init(Context context){
        ShoppingListLayout.init(context);
        FridgeLayout.init(context);
        RecipeDBLayout.init(context);
    }

    /**
     * Saves the Recipe object into the RecipeDB
     * @param rec Recipe object that needs atleast a URL and name loaded into it
     */
    public static void AddRecipeToFavorites(Recipe rec){
        RecipeDBLayout.addRecipeToDB(rec);
    }

    /**
     * Method that grabs ingredients from Recipe and stores it into ShoppingListDB
     * @param rec Recipe object to extract ingredients from
     */
    public static void UpdateShoppingList(Recipe rec){
        ShoppingListLayout.addToShoppingList(rec);
    }

    /**
     *
     * @return List of Ingredients directly from SQL database on phone
     */
    public static ArrayList<String> GetShoppingList(){
        return ShoppingListLayout.GetShoppingList();
    }

}
