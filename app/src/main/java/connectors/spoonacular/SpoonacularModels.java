package connectors.spoonacular;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AGCOSTFU on 10/9/2015.
 */
public class SpoonacularModels
{


    public class GetRecipeByIdResultModel
    {

        String title;

        @SerializedName("extendedIngredients")
        List<IngredientModel> ingredients;

        @SerializedName("imageUrls")
        List<String> imageUrls;

    }

/*    public class GetRecipeListResult
    {
       @SerializedName("")
       List<SearchRecipesResultModel> recipes;
    }*/

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
        private String ingredientLine;

        float amount;
        String unitLong;
    }

}

/*

*/

