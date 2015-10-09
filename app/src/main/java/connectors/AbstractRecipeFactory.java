package connectors;

import java.util.ArrayList;

import tools.Recipe;

/**
 * Created by Harjit Randhawa on 10/6/2015.
 */
public abstract class AbstractRecipeFactory
{
    //Abstract class that must be defined for each API that will retrieve recipes.  Child classes must allow parameters to be passed in as null
    //so make sure to check if(parameter != null) before trying to use it
    public abstract ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type);

    //FactoryProducer will take in a String to specify which API will be used.  Is called in SearchTools.GetRecipes as a means of having an abstract way to
    //make sure we call the correct API. Simply change the Strings in SearchTools to change which API will be called
    public static AbstractRecipeFactory FactoryProducer(String api)
    {
        AbstractRecipeFactory factory;

        if(api == "Yummly")
        {

        }
        else if(api == "Spoonacular")
        {

        }

        //returns null if API is not supported yet
        return null;
    }

}
