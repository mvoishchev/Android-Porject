package connectors.spoonacular;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import retrofit.converter.GsonConverter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonObject;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;

import connectors.AbstractRecipeFactory;
import connectors.spoonacular.SpoonacularModels.*;
import connectors.SearchTools;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import t4.csc413.smartchef.MainActivity;
import tools.Recipe;

/**
 * Created by AGCOSTFU on 10/10/2015.
 */
public class SpoonacularRecipeFactory extends AbstractRecipeFactory{

    public Recipe getRecipePreviewById(String id)
    {
        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        RecipePreviewModel result = connector.getRecipePreview(id);

        Recipe rec = new Recipe();

        rec.setRecipeUrl(result.sourceUrl);
        rec.setName(result.title);
        rec.addAllIngredientsFromModel(result.ingredients);
        rec.setApi("Spoonacular");

        return rec;
    }

    //Works
    public Recipe getRecipeByUrl(String url)
    {
        Recipe rec = new Recipe();
        rec.setRecipeUrl(url);

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        url = prepareUrlForExtraction(url);

        System.err.println("SOMETHING HERE: " + url);
        FullRecipeResultModel result = connector.getRecipe(url);

        System.out.println("Results: " + result);

        rec.setName(result.title);
        rec.addAllIngredientsFromModel(result.ingredients);
        rec.setInstruction(result.instructions);
        rec.addAllImageUrls(result.imageUrls);

        return rec;
    }


    //transform URL to match what API accepts
    private String prepareUrlForExtraction(String _url)
    {
        _url = "?forceExtraction=false&url=" + _url;
        _url.replaceAll(":", "%3A");
        _url.replaceAll("/", "%2f");

        return _url;
    }


    public ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type)
    {
        final ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        String endpoint = SpoonacularAPI.API_URL;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GetRecipeListResult.class, new GetRecipeListDeserializerJson())
                .create();

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        //RestAdapter spoonacularAdapter = new RestAdapter()

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        ingredients = prepareIngredientQuery(ingredients);

        JsonArray results = connector.getRecipeByIngredient(ingredients);
        System.out.println("JsonArray: " + results.size());

        for(JsonElement e: results)
        {
            SearchRecipesResultModel model = new Gson().fromJson(e, SearchRecipesResultModel.class);

            Recipe r = new Recipe();
            r.setName(model.title);
            r.setId(model.id);
            r.setApi("Spoonacular");
            r.addImageUrl(model.image);

            recipes.add(r);
        }

        return recipes;
    }

    private String prepareIngredientQuery(String list)
    {
        ArrayList<String> ingredients = SearchTools.ParseList(list);
        String query = "findByIngredients?ingredients=";
        for(int i = 0; i < ingredients.size() - 1; i++)
        {
            query = query.concat(ingredients.get(i)).concat("%2C");
        }
            query = query.concat(ingredients.get(ingredients.size() - 1));

        query = query.replaceAll(" ", "");

        System.out.println("query: " + query);
        return query;
    }
}
