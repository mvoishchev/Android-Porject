package connectors;

import android.view.View;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    //ParseList will take in a String which will be a comma separated list (ingredients, allergies, cuisines, etc)
    //and generate and ArrayList<String> with all elements of the list
    public static ArrayList<String> ParseList(String _list)
    {
        ArrayList<String> list = new ArrayList<String>();

        StringTokenizer strtok = new StringTokenizer(_list, ",");

        String item = strtok.nextToken();
        while(item != null)
        {
            list.add(item);
            item = strtok.nextToken();
        }

        return list;

    }

    public static Recipe GetRecipeById(String api, String id)
    {
        return AbstractRecipeFactory.getRecipe(api, id);
    }

    //GetRecipes will take in all parameters that are being used by AbstractRecipeFactory.getRecipes()
    //and call all API Connectors.  GUI will call this function.
    //Pass NULL into parameter function to ignore that parameter
    public static ArrayList<Recipe> GetRecipes(String ingredients, String allergies, String cuisine, INGREDIENT_SEARCH_TYPE search_type)
    {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        //call on each API here and add results to return value for GUI
        recipes.addAll(AbstractRecipeFactory.FactoryProducer(API_1).getRecipes(ingredients, allergies, cuisine, search_type));
        //recipes.addAll(AbstractRecipeFactory.FactoryProducer(API_2).getRecipes(ingredients, allergies, cuisine, search_type));

        //RemoveRedundancies(recipes);

        return recipes;
    }


    public static void println(String string){System.out.println(string);}
    public static Recipe ExtractRecipe(String url)
    {
        Recipe recipe = new Recipe();
        /*String login = v.getContext().getResources().getString(R.string.spoonacular_connector);
        String key = v.getContext().getResources().getString(R.string.spoonacular_key);
*/

        return recipe;
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
