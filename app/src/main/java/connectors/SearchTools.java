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
    static ArrayList<String> cachedKeys;
    static int MAX_CACHED = 5;
    public static boolean WAITING_API_1 = false;
    public static boolean WAITING_API_2 = false;

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
        cachedKeys = new ArrayList<String>();

        AbstractRecipeFactory.FactoryProducer(API_1).init();
        AbstractRecipeFactory.FactoryProducer(API_2).init();

    }

    public static CharSequence[] getSupportedAllergies()
    {
        CharSequence[] allergies;
        ArrayList<String> raw = new ArrayList<String>();

        raw.addAll(AbstractRecipeFactory.FactoryProducer(API_1).getSupportedAllergies());
        raw.addAll(AbstractRecipeFactory.FactoryProducer(API_2).getSupportedAllergies());

        allergies = new CharSequence[raw.size()];

        for(int i = 0; i < raw.size(); i++)
        {
            allergies[i] = raw.get(i);
        }
        return allergies;
    }

    public static CharSequence[] getSupportedCuisines()
    {
        CharSequence[] cuisines;
        ArrayList<String> raw = new ArrayList<String>();

        raw.addAll(AbstractRecipeFactory.FactoryProducer(API_1).getSupportedCuisines());
        raw.addAll(AbstractRecipeFactory.FactoryProducer(API_2).getSupportedCuisines());

        cuisines = new CharSequence[raw.size()];

        for(int i = 0; i < raw.size(); i++)
        {
            cuisines[i] = raw.get(i);
        }
        return cuisines;
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

    public static void UpdateCacheSearch(String request, ArrayList<Recipe> search)
    {
        ArrayList<Recipe> old = new ArrayList<Recipe>();
        if(cachedSearches.containsKey(request)) {
            old = cachedSearches.remove(request);
            cachedKeys.remove(request);
        }

        old.addAll(search);
        cachedSearches.put(request, old);
        cachedKeys.add(request);

        if(cachedKeys.size() > MAX_CACHED)
        {
            cachedSearches.remove(cachedKeys.get(0));
            cachedKeys.remove(0);
        }


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

    public static boolean isWaiting(){
        System.out.println("Waiting 1: " + WAITING_API_1 + " Waiting 2: " + WAITING_API_2);
        if(!WAITING_API_2 && !WAITING_API_1)
            return false;
        else {
            return true;
        }
    }

    public static String generateCacheKey(String ingredients, String allergies, String cuisine, INGREDIENT_SEARCH_TYPE search_type)
    {
        String key = ingredients;
        if(allergies.length() > 2)
            key = key.concat(", " + allergies);
        if(cuisine != null)
            key = key.concat(", " + cuisine);
        if(search_type!= null)
            key = key.concat(", " + search_type.name());

        return key;
    }

    public static boolean isCached(String key){return cachedSearches.containsKey(key);}

    //GetRecipes will take in all parameters that are being used by AbstractRecipeFactory.getRecipes()
    //and call all API Connectors.  GUI will call this function.
    //Pass NULL into parameter function to ignore that parameter
    public static ArrayList<Recipe> GetRecipes(String ingredients, String allergies, String cuisine, INGREDIENT_SEARCH_TYPE search_type)
    {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        String cacheKey = generateCacheKey(ingredients, allergies, cuisine, search_type);


        if(cachedSearches.containsKey(cacheKey))
        {
            recipes = cachedSearches.get(cacheKey);
            System.out.println("Hit cache: " + cacheKey);
        }else
        {
            //call on each API here and add results to return value for GUI
            //API_1 does not support allergy and cuisine filter with ingredient search
            AbstractRecipeFactory factory1 = AbstractRecipeFactory.FactoryProducer(API_1);
            ArrayList<Recipe> result1 = factory1.getRecipes(ingredients, allergies, cuisine, search_type);


            AbstractRecipeFactory factory2 = AbstractRecipeFactory.FactoryProducer(API_2);
            //handles the requests on seperate threads
            ArrayList<Recipe> result2 = factory2.getRecipes(ingredients, allergies, cuisine, search_type);


            //
            // recipes = RemoveRedundancies(recipes);
        }


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
