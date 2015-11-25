package database;

import android.content.Context;

import java.util.ArrayList;

import database.fridge.FridgeLayout;
import database.recipedb.RecipeDBLayout;
import database.shoppinglist.ShoppingListLayout;
import tools.Recipe;

/**
 * Created by Harjit on 11/25/2015.
 */
public class DataBaseManager {

    public static void init(Context context){
        ShoppingListLayout.init(context);
        FridgeLayout.init(context);
        RecipeDBLayout.init(context);
    }

    public static void AddRecipeToFavorites(Recipe rec){
        RecipeDBLayout.addRecipeToDB(rec);
    }

    public static void UpdateShoppingList(Recipe rec){
        ShoppingListLayout.addToShoppingList(rec);
    }

    public static ArrayList<String> GetShoppingList(){
        return ShoppingListLayout.getIngredients();
    }
}
