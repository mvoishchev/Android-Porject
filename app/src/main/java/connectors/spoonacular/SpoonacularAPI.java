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

    /**
     *
     * @param url url of the HTTP request to send to Spoonacular
     * @return Full Recipe including ingredients from Spoonacular
     */
    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/extract{url}")
    public void getRecipe(@Path(value = "url", encode = false) String url, Callback<FullRecipeResultModel> model);

    /**
     *
     * @param id url of the HTTP request to send to Spoonacular
     * @param response Asyncronous Callback for response from Spoonacular
     */
    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/{id}")
    public void getRecipeByIngredient(@Path(value = "id", encode = false) String id, Callback<JsonArray> response);

    /**
     *
     * @param id
     * @return RecipePreviewModel for Search Results from Spoonacular
     */
    @Headers("X-Mashape-Key: WJ0SImVjcAmshThFeMvrbwGTXVHEp12W62TjsnoSo6ixq8GwqR")
    @GET("/recipes/{id}/information")
    public RecipePreviewModel getRecipePreview(@Path("id") String id);

}

