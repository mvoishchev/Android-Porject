package connectors;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

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

    //GetRecipes will take in all parameters that are being used by AbstractRecipeFactory.getRecipes()
    //and call all API Connectors.  GUI will call this function.
    //Pass NULL into parameter function to ignore that parameter
    public static ArrayList<Recipe> GetRecipes(String ingredients, String allergies, String cuisine, INGREDIENT_SEARCH_TYPE search_type)
    {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        //call on each API here and add results to return value for GUI
        recipes.addAll(AbstractRecipeFactory.FactoryProducer(API_1).getRecipes(ingredients, allergies, cuisine, search_type));
        recipes.addAll(AbstractRecipeFactory.FactoryProducer(API_2).getRecipes(ingredients, allergies, cuisine, search_type));

        return recipes;
    }
}
