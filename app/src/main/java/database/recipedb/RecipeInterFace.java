package database.recipedb;

import android.view.View;

import tools.Recipe;

/**
 * Created by MG on 11/22/2015.
 */
public interface RecipeInterFace {

    void addRecipe(View v, Recipe recipe);
    void removeRecipe(View v);
    void getRecipe(View v);
}
