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

    //Works
    public Recipe getRecipeByUrl(String url)
    {
        Recipe rec = new Recipe();
        rec.setRecipeUrl(url);

        RestAdapter spoonacularAdapter = new RestAdapter.Builder().setEndpoint(SpoonacularAPI.API_URL).build();

        SpoonacularAPI connector = spoonacularAdapter.create(SpoonacularAPI.class);

        url = prepareUrlForExtraction(url);

        System.err.println("SOMETHING HERE: " + url);
        FullRecipeResultModel result = connector.getRecipe(url);

        System.out.println("Results: " + result);

        rec.setName(result.title);
        rec.addAllIngredientsFromModel(result.ingredients);
        rec.setInstruction(result.instructions);
        rec.addAllImageUrls(result.imageUrls);
        rec.setPrepTime(result.minutes/60,result.minutes%60);

        System.out.println("recipe instructions: " + result.instructions);

        return rec;
    }


    //transform URL to match what API accepts
    private String prepareUrlForExtraction(String _url)
    {
        _url = "?forceExtraction=false&url=" + _url;
        _url.replaceAll(":", "%3A");
        _url.replaceAll("/", "%2f");

        return _url;
    }


    public ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type)
    {
        final ArrayList<Recipe> recipes= new ArrayList<Recipe>();;
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
        SearchTools.WAITING_API_1 = true;
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

                System.out.println("Recipes added: " + recipes.size());
                System.out.println("Spoonacular here JsonArray size: " + jsonElements.size());

                SearchTools.UpdateCacheSearch(cachekey, recipes);

                setRequesting(false);
                SearchTools.WAITING_API_1 = false;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        System.out.println("Recipes: " + recipes.size());



        return recipes;
    }



    private String prepareIngredientQuery(ArrayList<String> list)
    {
        String query = "findByIngredients?ingredients=";
        for(int i = 0; i < list.size() - 1; i++)
        {
            query = query.concat(list.get(i)).concat("%2C");
        }
        query = query.concat(list.get(list.size() - 1));

        query = query.replaceAll(" ", "");

        System.out.println("query: " + query);
        return query;
    }
}
