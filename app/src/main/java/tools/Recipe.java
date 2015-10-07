package tools;

import java.util.ArrayList;

/**
 * Created by Harjit on 10/6/2015.
 *
 * Recipe class designed to make Recipes a usable object.
 *
 * Recipe objects will be created when a query is made and the RecipeFactory
 */
public class Recipe {

    String name;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> instructions;
    int prepTime_hours, prepTime_minutes;

}
