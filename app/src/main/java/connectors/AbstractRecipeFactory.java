package connectors;

import java.util.ArrayList;

import connectors.spoonacular.SpoonacularRecipeFactory;
import connectors.yummly.YummlyRecipeFactory;
import tools.Recipe;

/**
 * Created by Harjit Randhawa on 10/6/2015.
 */
public abstract class AbstractRecipeFactory
{

    private String api;

    boolean requesting = false;
    //Abstract class that must be defined for each API that will retrieve recipes.  Child classes must allow parameters to be passed in as null
    //so make sure to check if(parameter != null) before trying to use it

    /**
     *
     * @param ingredients Comma seperated list of ingredients
     * @param allergies Comma seperated list of allergies, can be null
     * @param cuisine Desired cuisine, can be null
     * @param search_type Desired type of search defined by SearchTools.INGREDIENT_SEARCH_TYPE
     * @return List of Recipes that are returned from API
     */
    public abstract ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type);

    /**
     * Initialization method for any Allergies or Cuisines supported
     */
    public abstract void init();

    /**
     *
     * @return List of Allergies the API will support. Returns empty if no allergies are supported
     */
    public abstract ArrayList<String> getSupportedAllergies();

    /**
     *
     * @return List of Cuisines that API will support. Returns empty if no cuisines are supported
     */
    public abstract ArrayList<String> getSupportedCuisines();

    /**
     *
     * @param id ID of Recipe for API to search for
     * @return Recipe object with Url
     */
    public abstract Recipe getRecipePreviewById(String id);

    /**
     *
     * @param api Defines which API is being requested
     * @return AbstractRecipeFactory that has implemented required abstract functions
     */
    public static AbstractRecipeFactory FactoryProducer(String api)
    {
        AbstractRecipeFactory factory;

        if(api.contains("Yummly"))
        {
            return new YummlyRecipeFactory();
        }
        else if(api.contains("Spoonacular"))
        {
            return new SpoonacularRecipeFactory();
        }

        //returns null if API is not supported yet
        return null;
    }
    //Only API that provides instructions is Spoonacular and they have an extraction tool for any website's recipes
    //So automatically call on that when user decides which recipe he wants to look at

    /**
     *
     * @param url Url of Recipe that needs all information extracted from website
     * @return Complete Recipe object from Spoonacular API
     */
    public static Recipe getRecipe(String url)
    {
        return new SpoonacularRecipeFactory().getRecipeByUrl(url);
    }

    /**
     *
     * @param _name API name to define where the recipe came from in the results
     */
    protected void setApiName(String _name)
    {
        api = _name;
    }

    //Method that allows GUI to identify which API
    public String getApiName()
    {
        return api;
    }

    protected void setRequesting(boolean _requesting){requesting = _requesting;}

    public boolean isRequesting(){return requesting;}

}
