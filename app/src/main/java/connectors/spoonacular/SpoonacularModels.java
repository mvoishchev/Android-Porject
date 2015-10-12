package connectors.spoonacular;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AGCOSTFU on 10/9/2015.
 */
public class SpoonacularModels
{


    public class FullRecipeResultModel
    {

        String title;

        @SerializedName("extendedIngredients")
        List<IngredientModel> ingredients;

        @SerializedName("imageUrls")
        List<String> imageUrls;

        @SerializedName("sourceUrl")
        String sourceUrl;

        @SerializedName("instructions")
        String instructions;

    }

    public class RecipePreviewModel
    {
        String title;

        @SerializedName("imageUrls")
        List<String> imageUrls;

        @SerializedName("extendedIngredients")
        List<IngredientModel> ingredients;

        @SerializedName("sourceUrl")
        String sourceUrl;
    }




    public class SearchRecipesResultModel
    {
        String id;
        String title;
        String image;
        String usedIngredientCount;
        String missedIngredientCount;
    }
    public class IngredientModel
    {

        @SerializedName("originalString")
        @Expose
        public String ingredientLine;

        public float amount;
        public String unitLong;
    }

}


