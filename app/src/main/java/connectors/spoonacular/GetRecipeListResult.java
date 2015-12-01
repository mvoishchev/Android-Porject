package connectors.spoonacular;

import java.util.ArrayList;
import java.util.List;
import connectors.spoonacular.SpoonacularModels.*;

/**
 * Created by AGCOSTFU on 10/11/2015.
 */
public class GetRecipeListResult
{
    List<SearchRecipesResultModel> recipes;

    /**
     *
     * @return List of Result Models generated from JSONArray
     */
    public List<SearchRecipesResultModel> getResults()
    {
        return recipes;
    }

    /**
     *
     * @param item SearchRecipesResultModel to add into the List of results
     */
    public void add(SearchRecipesResultModel item)
    {
        if(recipes == null)
        {
            recipes = new ArrayList<SearchRecipesResultModel>();
        }
        recipes.add(item);
    }
}
