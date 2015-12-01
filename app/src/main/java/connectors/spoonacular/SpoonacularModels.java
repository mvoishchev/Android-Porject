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
 * Created by Harjit Randhawa on 10/9/2015.
 *
 * Class that defines models that are created from Json Objects received from Spoonacular API
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

        @SerializedName("text")
        String instructions;

        @SerializedName("readyInMinutes")
        int minutes;

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

        public String name;

        public float amount;
        public String unitLong;
    }

}


