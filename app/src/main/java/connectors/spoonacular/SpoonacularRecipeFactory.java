package connectors.spoonacular;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
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


    public Recipe getRecipeById(String id)
    {
        Recipe rec = new Recipe();

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        SpoonacularModels.GetRecipeByIdResultModel result = connector.getRecipe(id);

        rec.setName(result.title);

        return rec;
    }

    public ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type)
    {
        final ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        JsonArray jstring = connector.getRecipeByIngredient(ingredients);
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        System.out.println("Jstring: " + jstring.toString());

        Type collectionType = new TypeToken<Collection<SpoonacularModels.SearchRecipesResultModel>>(){}.getType();
        Collection<SpoonacularModels.SearchRecipesResultModel> enums = gson.fromJson(jstring, collectionType);

        System.out.println("Here: " + enums.size());


    /*    for(JsonElement obj: jstring)
        {
            SpoonacularModels.SearchRecipesResultModel model = gson.fromJson(obj, SpoonacularModels.SearchRecipesResultModel.class);

            Recipe rec = new Recipe();

            rec.setName(model.title);
            rec.setId(model.id);
            rec.addImageUrl(model.image);


            recipes.add(rec);
        }*/



        SearchTools.println("here");
      /* connector.getRecipeByIngredient(ingredients, new Callback<List<SpoonacularModels.SearchRecipesResultModel>>(){


           @Override
           public void success(List<SpoonacularModels.SearchRecipesResultModel> results, Response r)
           {
               for(SpoonacularModels.SearchRecipesResultModel result : results)
               {
                   Recipe rec = new Recipe();

                   rec.setName(result.title);
                   rec.setId(result.id);
                   rec.addImageUrl(result.image);


                   recipes.add(rec);
               }
           }

           @Override
           public void failure(RetrofitError retrofitError)
           {
               MainActivity.setText("Got here atleast");

               System.out.println("failed");
               switch (retrofitError.getKind()) {
                   case NETWORK:
                       System.err.println("Network Error occured while contacting " + retrofitError.getUrl());
                       break;
                   case HTTP:
                       System.err.println("HTTP Request error: " + retrofitError.getResponse().getStatus() +
                               " while contacting " + retrofitError.getUrl());
                       break;
                   case CONVERSION:
                       System.err.println("Unable to convert response body into json.");

                       break;
                   case UNEXPECTED:
                       System.err.println("Unexpected error has occurred.");
                       break;
               }
           }
       });*/





        System.out.println("Recipes size: " + recipes.size());

        return recipes;
    }
}
