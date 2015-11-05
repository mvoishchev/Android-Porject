package connectors;

import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import t4.csc413.smartchef.R;
import tools.Recipe;

/**
 *
 * Class to hold all enumerations for searches.  Ensures that filters are used properly
 * Created by Harjit Randhawa on 10/6/2015.
 */
public class SearchTools
{
    static String API_1 = "Spoonacular";
    static String API_2 = "Yummly";

    static ArrayList<Recipe> cachedRecipes;
    static HashMap<String, ArrayList<Recipe>> cachedSearches;
    static int MAX_CACHED = 5;

    public static enum INGREDIENT_SEARCH_TYPE
    {
        ANY_INGREDIENT_IS_PRESENT,
        ALL_INGREDIENTS_PRESENT,
        ONLY_INGREDIENTS_PRESENT;
    }

    public static enum MEASUREMENTS
    {
        CUP,
        TABLESPOON,
        TEASPOON,
        GRAMS,
        OUNCES,
        POUNDS,
        GALLON,
        QUARTS,
        DASH,
        PINCH;
    }


    public static void init()
    {
        cachedRecipes = new ArrayList<Recipe>();
        cachedSearches = new HashMap<String, ArrayList<Recipe>>();

    }

    //ParseList will take in a String which will be a comma separated list (ingredients, allergies, cuisines, etc)
    //and generate and ArrayList<String> with all elements of the list
    public static ArrayList<String> ParseList(String _list)
    {
        ArrayList<String> list = new ArrayList<String>();

        StringTokenizer strtok = new StringTokenizer(_list, ",");

        String item;
        while(strtok.hasMoreTokens())
        {
            item = strtok.nextToken();
            item.trim();
            list.add(item);
        }

        return list;

    }

    private static void cacheRecipe(Recipe recipe)
    {
        cachedRecipes.add(recipe);

        if(cachedRecipes.size() > MAX_CACHED)
        {
            cachedRecipes.remove(0);
        }
    }

    private static void cacheSearch(ArrayList<Recipe> search)
    {
        //cachedSearches.
    }

    public static Recipe GetRecipeByUrl(String url)
    {
        //checks if recipe is cached, if not make the search
        for(int recipe = 0; recipe < cachedRecipes.size(); recipe++)
        {
            if(cachedRecipes.get(recipe).getRecipeUrl().equalsIgnoreCase(url))
                return cachedRecipes.get(recipe);
        }

        Recipe recipe = AbstractRecipeFactory.getRecipe(url);
        cacheRecipe(recipe);

        return recipe;
    }

    //GetRecipes will take in all parameters that are being used by AbstractRecipeFactory.getRecipes()
    //and call all API Connectors.  GUI will call this function.
    //Pass NULL into parameter function to ignore that parameter
    public static ArrayList<Recipe> GetRecipes(String ingredients, String allergies, String cuisine, INGREDIENT_SEARCH_TYPE search_type)
    {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        //call on each API here and add results to return value for GUI
        recipes.addAll(AbstractRecipeFactory.FactoryProducer(API_1).getRecipes(ingredients, allergies, cuisine, search_type));
        recipes.addAll(AbstractRecipeFactory.FactoryProducer(API_2).getRecipes(ingredients, allergies, cuisine, search_type));

        //RemoveRedundancies(recipes);

        return recipes;
    }


    public static void println(String string){System.out.println(string);}

    public static Recipe GetRecipePreviewById(String api, String id)
    {
        AbstractRecipeFactory factory = AbstractRecipeFactory.FactoryProducer(api);
        return factory.getRecipePreviewById(id);
    }

    //Method to ensure that any recipes were returned from both APIs are removed from the set of
    //returned Recipes
    private static ArrayList<Recipe> RemoveRedundancies(ArrayList<Recipe> _recipes)
    {
        ArrayList<String> existingRecipeNames = new ArrayList<String>();
        for(int i = 0; i < _recipes.size(); i++)
        {
            if(existingRecipeNames.contains(_recipes.get(i).getName()))
            {
                _recipes.remove(i);
            }else
            {
                existingRecipeNames.add(_recipes.get(i).getName());
            }

        }

        return _recipes;
    }

}
