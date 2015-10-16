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
    //Abstract class that must be defined for each API that will retrieve recipes.  Child classes must allow parameters to be passed in as null
    //so make sure to check if(parameter != null) before trying to use it
    public abstract ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type);

    public abstract Recipe getRecipePreviewById(String id);
    //public abstract Recipe getRecipeByUrl(String id);
    //FactoryProducer will take in a String to specify which API will be used.  Is called in SearchTools.GetRecipes as a means of having an abstract way to
    //make sure we call the correct API. Simply change the Strings in SearchTools to change which API will be called
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
    public static Recipe getRecipe(String url)
    {
        System.out.println("Requested Url: " + url);
       return new SpoonacularRecipeFactory().getRecipeByUrl(url);

    }

    protected void setApiName(String _name)
    {
        api = _name;
    }

    //Method that allows GUI to identify which API
    public String getApiName()
    {
        return api;
    }

}
