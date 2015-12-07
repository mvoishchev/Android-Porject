package connectors.spoonacular;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import connectors.AbstractRecipeFactory;
import connectors.spoonacular.SpoonacularModels.*;
import connectors.SearchTools;
import retrofit.RestAdapter;
import tools.Recipe;

/**
 * Created by AGCOSTFU on 10/10/2015.
 */
public class SpoonacularRecipeFactory extends AbstractRecipeFactory{




    public void init(){

    }


    public ArrayList<String> getSupportedAllergies()
    {
        return new ArrayList<String>();
    }
    public ArrayList<String> getSupportedCuisines()
    {
        return new ArrayList<String>();
    }
    public Recipe getRecipePreviewById(String id)
    {
        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        RecipePreviewModel result = connector.getRecipePreview(id);

        Recipe rec = new Recipe();

        rec.setRecipeUrl(result.sourceUrl);
        rec.setName(result.title);
        rec.addAllIngredientsFromModel(result.ingredients);
        rec.setApi("Spoonacular");


        return rec;
    }

    /**
     *
     * @param url Url of the recipe to extract information from
     * @return Full Recipe object defined by tools.Recipe
     */
    public Recipe getRecipeByUrl(String url)
    {
        final Recipe rec = new Recipe();
        rec.setRecipeUrl(url);

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        url = prepareUrlForExtraction(url);

        SearchTools.setWaitingAPI(1, true);
        connector.getRecipe(url, new Callback<FullRecipeResultModel>() {
            @Override
            public void success(FullRecipeResultModel fullRecipeResultModel, Response response) {
                rec.setName(fullRecipeResultModel.title);
                rec.addAllIngredientsFromModel(fullRecipeResultModel.ingredients);
                rec.setInstruction(fullRecipeResultModel.instructions);
                if (fullRecipeResultModel.imageUrls != null)
                    rec.addAllImageUrls(fullRecipeResultModel.imageUrls);

                rec.setPrepTime(fullRecipeResultModel.minutes / 60, fullRecipeResultModel.minutes % 60);

                SearchTools.setWaitingAPI(1, false);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("SHIT HAPPENENING");
                SearchTools.setWaitingAPI(1, false);
            }
        });

        return rec;
    }


    /**
     *
     * @param _url Original url of a recipe
     * @return Url formatted to Spoonacular HTTP conventions
     */
    private String prepareUrlForExtraction(String _url)
    {
        _url = "?forceExtraction=false&url=" + _url;
        _url.replaceAll(":", "%3A");
        _url.replaceAll("/", "%2f");

        return _url;
    }


    public ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type)
    {
        final ArrayList<Recipe> recipes= new ArrayList<Recipe>();

        if(allergies.length() > 2 || cuisine != null)
        {
            return recipes;
        }
        final String cachekey = SearchTools.generateCacheKey(ingredients, allergies, cuisine, search_type);

        //Spoonacular can't search by allergies and cuisine. Leave that to Yummly and just return out of it if it requests that
        //search_type will just have to be a forced search
        String endpoint = SpoonacularAPI.API_URL;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GetRecipeListResult.class, new GetRecipeListDeserializerJson())
                .create();

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        //RestAdapter spoonacularAdapter = new RestAdapter()

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        ArrayList list = SearchTools.ParseList(ingredients);
        final int listSize = list.size();
        final SearchTools.INGREDIENT_SEARCH_TYPE type = search_type;
        ingredients = prepareIngredientQuery(list);



        setRequesting(true);
        SearchTools.setWaitingAPI(1, true);
        connector.getRecipeByIngredient(ingredients, new Callback<JsonArray>() {
            @Override
            public void success(JsonArray jsonElements, Response response) {
                for(JsonElement e: jsonElements)
                {
                    SearchRecipesResultModel model = new Gson().fromJson(e, SearchRecipesResultModel.class);
                    Recipe r = new Recipe();
                    r.setName(model.title);
                    r.setId(model.id);
                    r.setApi("Spoonacular");
                    r.addImageUrl(model.image);
                    r.setMatchedIngredients(Integer.parseInt(model.usedIngredientCount));
                    r.setMissingIngredients(Integer.parseInt(model.missedIngredientCount));


                    recipes.add(r);
                }
                if(type != null) {
                    if (type == SearchTools.INGREDIENT_SEARCH_TYPE.ALL_INGREDIENTS_PRESENT) {
                        for (Recipe recipe : recipes) {
                            if (recipe.getMatchedIngredientCount() != listSize) {
                                recipes.remove(recipe);
                            }

                        }
                    } else if (type == SearchTools.INGREDIENT_SEARCH_TYPE.ONLY_INGREDIENTS_PRESENT) {
                        for (Recipe recipe : recipes) {
                            if (recipe.getMissingIngredientCount() > 0)
                                recipes.remove(recipe);
                        }
                    }
                }
                SearchTools.UpdateCacheSearch(cachekey, recipes);

                setRequesting(false);
                SearchTools.setWaitingAPI(1, false);
            }

            @Override
            public void failure(RetrofitError error) {
                SearchTools.setWaitingAPI(1, false);
            }
        });



        return recipes;
    }


    /**
     *
     * @param list List of ingredients to search with
     * @return HTTP url formatted to Spoonacular conventions
     */
    private String prepareIngredientQuery(ArrayList<String> list)
    {
        String query = "findByIngredients?ingredients=";
        for(int i = 0; i < list.size() - 1; i++)
        {
            query = query.concat(list.get(i)).concat("%2C");
        }
        query = query.concat(list.get(list.size() - 1));
        query = query.trim();
        query = query.replaceAll(" ", "+");
        return query;
    }
}
