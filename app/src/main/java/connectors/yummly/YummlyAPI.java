package connectors.yummly;

import com.google.gson.JsonArray;

import java.util.List;

import connectors.spoonacular.SpoonacularModels;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import connectors.yummly.YummlyModels.*;

/**
 * Created by satya on 10/15/2015.
 */
public interface YummlyAPI {
    public static final String API_URL = "http://api.yummly.com/v1/api";

    @GET("/recipes{id}")
    public void getRecipeByIngredient(@Path(value = "id", encode = false) String id, Callback<GetRecipeListResult> result);

    @GET("/recipe/{id}")
    public RecipePreviewModel getRecipePreview(@Path(value = "id", encode = false) String id);

}



