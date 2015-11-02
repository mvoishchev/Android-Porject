package tools;

import java.util.ArrayList;
import java.util.List;

import connectors.spoonacular.SpoonacularModels;

/**
 * Created by Harjit Randhawa on 10/6/2015.
 *
 * Recipe class designed to make Recipes a usable object.
 *
 * Recipe objects will be created when a query is made and the RecipeFactory
 */
public class Recipe
{

    String name, cuisine, recipeUrl, id, api;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> imageUrls;
    String instructions;
    int prepTime_hours, prepTime_minutes;
    String dummy;

    public Recipe()
    {
        init();
    }

    private void init()
    {
        ingredients = new ArrayList<Ingredient>();
        instructions = "";
        imageUrls = new ArrayList<String>();
        prepTime_minutes = -1;
        prepTime_hours = -1;
        dummy = "https://yt3.ggpht.com/--LNjtIfd_Q4/AAAAAAAAAAI/AAAAAAAAAAA/Ab-m2XbhGgI/s100-c-k-no/photo.jpg";
    }

    public void setId(String _id)
    {
        id = _id;
    }

    public void addImageUrl(String _url){imageUrls.add(_url);}

    public ArrayList<String> getImageUrls(){return imageUrls;}

    public String getId(){return id;}

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }

    public void setCuisine(String _cuisine)
    {
        cuisine = _cuisine;
    }

    public String getCuisine()
    {
        return cuisine;
    }

    public void addIngredient(Ingredient _ingredient)
    {
        ingredients.add(_ingredient);
    }

    public ArrayList<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void addAllIngredients(ArrayList<Ingredient> array){ingredients.addAll(array);}


    public void addAllIngredientsFromModel(List<SpoonacularModels.IngredientModel> _list)
    {
        for(SpoonacularModels.IngredientModel model: _list)
        {
            addIngredient(new Ingredient(model.ingredientLine, model.amount, model.unitLong));
        }
    }

    public void setInstruction(String _instruction)
    {
        instructions = _instruction;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public void setPrepTime(int _hours, int _minutes)
    {
        prepTime_hours = _hours;
        prepTime_minutes = _minutes;
    }

    //getPrepTime_hours will return the hours it takes to cook the dish.
    //Returns -1 if no preparation time was found
    public int getPrepTime_hours()
    {
        return prepTime_hours;
    }

    //getPrepTime_minutes will return the hours it takes to cook the dish.
    //Returns -1 if no preparation time was found
    public int getPrepTime_minutes()
    {
        return prepTime_minutes;
    }

    public void setApi(String _url) {api = _url;}

    public String getApi(){return api;}

    public void setRecipeUrl(String _url)
    {recipeUrl = _url;}

    public String getRecipeUrl(){return recipeUrl;}

    public void addAllImageUrls(List<String> _urls){imageUrls.addAll(_urls);}
    public String getImageUrl(){return dummy;} //made just to test

}
