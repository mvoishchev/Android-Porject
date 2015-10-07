package connectors;

import tools.Recipe;

/**
 * Created by AGCOSTFU on 10/6/2015.
 */
public abstract class AbstractRecipeFactory {

    public abstract Recipe getRecipe(String ingredients, String allergies, String cuisine, SearchTools.SEARCH_TYPE search_type);
}
