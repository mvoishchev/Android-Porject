package connectors.spoonacular;



/**
 * Created by AGCOSTFU on 10/9/2015.
 */

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import connectors.spoonacular.SpoonacularModels.*;
import retrofit.http.Query;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public interface SpoonacularAPI {
    public static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com";

    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/extract{url}")
    public FullRecipeResultModel getRecipe(@Path(value = "url", encode = false) String url);

    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/{id}")
    public void getRecipeByIngredient(@Path(value = "id", encode = false) String id, Callback<JsonArray> response);

    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/{id}/information")
    public RecipePreviewModel getRecipePreview(@Path("id") String id);

}

