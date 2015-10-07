package connectors;

import java.util.ArrayList;

import tools.Recipe;

/**
 * Created by Harjit Randhawa on 10/6/2015.
 */
public abstract class AbstractRecipeFactory
{
    public abstract ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type);

}
