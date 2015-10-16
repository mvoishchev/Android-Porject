package connectors.yummly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import connectors.SearchTools;
import connectors.spoonacular.SpoonacularModels;
import connectors.yummly.YummlyModels.*;
import connectors.AbstractRecipeFactory;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import tools.Recipe;

/**
 * Created by satya on 10/15/2015.
 */
public class YummlyRecipeFactory extends AbstractRecipeFactory{

    public Recipe getRecipePreviewById(String id)
    {
        RestAdapter yummlyAdapter = new RestAdapter.Builder().setEndpoint(YummlyAPI.API_URL).build();

        String search = id.concat("?_app_id=8f371517&_app_key=0d24ad13bad93da625d68ba50920fe9d&");

        YummlyAPI connector = yummlyAdapter.create(YummlyAPI.class);
        RecipePreviewModel result = connector.getRecipePreview(search);

        Recipe rec = new Recipe();
        rec.setRecipeUrl(result.sourceUrl.url);
        rec.setName(result.title);
        rec.setApi("Yummly");

        return rec;
    }


    public ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type)
    {
        final ArrayList<Recipe> recipes = new ArrayList<Recipe>();


        RestAdapter yummlyAdapter = new RestAdapter.Builder().setEndpoint(YummlyAPI.API_URL)
                .build();
        //RestAdapter yummlyAdapter = new RestAdapter()

        YummlyAPI connector = yummlyAdapter.create(YummlyAPI.class);

        ingredients = prepareIngredientQuery(ingredients);

        System.out.println(ingredients);

        GetRecipeListResult results = connector.getRecipeByIngredient(ingredients);
        System.out.println("JsonArray: " + results.recipes.size());

        for(SearchRecipesResultModel model: results.recipes)
        {

            Recipe r = new Recipe();
            r.setName(model.title);
            r.setId(model.id);
            r.setApi("Yummly");
            recipes.add(r);
        }
        return recipes;
    }

    private String prepareIngredientQuery(String list)
    {
        ArrayList<String> ingredients = SearchTools.ParseList(list);
        String query = "?_app_id=8f371517&_app_key=0d24ad13bad93da625d68ba50920fe9d";
        for(int i = 0; i < ingredients.size(); i++)
        {
            query = query.concat("&allowedIngredient[]="+ingredients.get(i));
        }


        return query;
    }
}

