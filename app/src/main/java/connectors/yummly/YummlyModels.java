package connectors.yummly;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satya on 10/15/2015.
 */
public class YummlyModels {

    public class RecipePreviewModel
    {
        @SerializedName("id")
        String title;

        @SerializedName("source")
        SourcesModel sourceUrl;

    }
    public class GetRecipeListResult
    {
        @SerializedName("matches")
        List<SearchRecipesResultModel> recipes;
    }

    public class SourcesModel
    {
        @SerializedName("sourceRecipeUrl")
        String url;
    }




    public class SearchRecipesResultModel
    {
        String id;

        @SerializedName("recipeName")
        String title;

        @SerializedName("smallImageUrls")
        List<String> urls;



    }

    public class ImagesModel
    {
        String hostedLargeUrl;
        String hostedSmallUrl;
    }
    public class IngredientModel
    {
        public String ingredientLine;
    }

}
