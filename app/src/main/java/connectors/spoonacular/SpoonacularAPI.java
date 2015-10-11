package connectors.spoonacular;



/**
 * Created by AGCOSTFU on 10/9/2015.
 */

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import connectors.spoonacular.SpoonacularModels.*;
import retrofit.http.Query;
import com.google.gson.JsonArray;


public interface SpoonacularAPI {
    public static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com";

    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/{id}/information")
    public GetRecipeByIdResultModel getRecipe(@Path("id") String id);

    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/findByIngredients?ingredients=")
    public JsonArray getRecipeByIngredient(@Query("search") String search);
}

