package connectors.yummly;

import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

import connectors.SearchTools;
import connectors.spoonacular.SpoonacularModels;
import connectors.yummly.YummlyModels.*;
import connectors.AbstractRecipeFactory;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import tools.Recipe;

/**
 * Created by satya on 10/15/2015.
 */
public class YummlyRecipeFactory extends AbstractRecipeFactory{
    static HashMap<String, String> allergies;
    static HashMap<String, String> cuisines;

    public Recipe getRecipePreviewById(String id)
    {
        RestAdapter yummlyAdapter = new RestAdapter.Builder().setEndpoint(YummlyAPI.API_URL).build();

        String search = id.concat("?_app_id=8f371517&_app_key=0d24ad13bad93da625d68ba50920fe9d&");

        YummlyAPI connector = yummlyAdapter.create(YummlyAPI.class);
        RecipePreviewModel result = connector.getRecipePreview(search);

        Recipe rec = new Recipe();
        rec.setRecipeUrl(result.sourceUrl.url);
        rec.setName(result.title);
        rec.setApi("Yummly");

        return rec;
    }

    public void init()
    {
        allergies = new HashMap<String, String>();

        allergies.put("Diary", "396^Dairy-Free");
        allergies.put("Egg", "397^Egg-Free");
        allergies.put("Gluten", "393^Gluten-Free");
        allergies.put("Peanut", "394^Peanut-Free");
        allergies.put("Seafood", "398^Seafood-Free");
        allergies.put("Sesame", "399^Sesame-Free");
        allergies.put("Soy", "400^Soy-Free");
        allergies.put("Sulfite", "401^Sulfite-Free");
        allergies.put("Wheat", "392^Wheat-Free");

        cuisines = new HashMap<String, String>();
        cuisines.put("American", "cuisine^cuisine-american");
        cuisines.put("Kid-Friendly", "cuisine^cuisine-kid-friendly");
        cuisines.put("Italian", "cuisine^cuisine-italian");
        cuisines.put("Asian", "cuisine^cuisine-asian");
        cuisines.put("Mexican", "cuisine^cuisine-mexican");
        cuisines.put("Southern & Soul", "cuisine^cuisine-southern");
        cuisines.put("BBQ", "cuisine^cuisine-barbecue-bbq");
        cuisines.put("Indian", "cuisine^cuisine-indian");
        cuisines.put("Chinese", "cuisine^cuisine-chinese");
        cuisines.put("Cajun", "cuisine^cuisine-cajun");
        cuisines.put("Mediterranean", "cuisine^cuisine-mediterranean");
        cuisines.put("Greek", "cuisine^cuisine-greek");
        cuisines.put("English", "cuisine^cuisine-english");
        cuisines.put("Spanish", "cuisine^cuisine-spanish");
        cuisines.put("Thai", "cuisine^cuisine-thai");
        cuisines.put("German", "cuisine^cuisine-german");
        cuisines.put("Morrocan", "cuisine^cuisine-morrocan");
        cuisines.put("Irish", "cuisine^cuisine-irish");
        cuisines.put("Japanese", "cuisine^cuisine-japanese");
        cuisines.put("Cuban", "cuisine^cuisine-cuban");
        cuisines.put("Portuguese", "cuisine^cuisine-portuguese");
        cuisines.put("Swedish", "cuisine^cuisine-swedish");
        cuisines.put("Hungarian", "cuisine^cuisine-hungarian");
        cuisines.put("Hawaiian", "cuisine^cuisine-hawaiian");
    }

    public ArrayList<String> getSupportedAllergies()
    {
        return (ArrayList) allergies.keySet();
    }

    public ArrayList<String> getSupportedCuisines()
    {
        return (ArrayList) cuisines.keySet();
    }


    public ArrayList<Recipe> getRecipes(String ingredients, String allergies, String cuisine, SearchTools.INGREDIENT_SEARCH_TYPE search_type)
    {
        final ArrayList<Recipe> recipes= new ArrayList<Recipe>();
        final String cachekey = SearchTools.generateCacheKey(ingredients, allergies, cuisine, search_type);
        final SearchTools.INGREDIENT_SEARCH_TYPE type = search_type;

        final int listSize = SearchTools.ParseList(ingredients).size();
        String search;
        RestAdapter yummlyAdapter = new RestAdapter.Builder().setEndpoint(YummlyAPI.API_URL)
                .build();
        //RestAdapter yummlyAdapter = new RestAdapter()

        YummlyAPI connector = yummlyAdapter.create(YummlyAPI.class);

        search = prepareIngredientQuery(ingredients);

        if(allergies != null)
            search = appendAllergies(search, allergies);

        if(cuisine != null)
            search = appendCuisine(search, cuisine);

        setRequesting(true);
        SearchTools.WAITING_API_2 = true;
        connector.getRecipeByIngredient(search, new Callback<GetRecipeListResult>() {
            @Override
            public void success(GetRecipeListResult getRecipeListResult, Response response) {
                for(SearchRecipesResultModel model: getRecipeListResult.recipes)
                {

                    Recipe r = new Recipe();
                    r.setName(model.title);
                    r.setId(model.id);
                    r.setApi("Yummly");

                    if(model.urls.size() > 0) {
                        r.addImageUrl(model.urls.get(0));
                        System.out.println("url added");
                    }
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
                System.out.println("Yummly here, Array Size: " + recipes.size());
                setRequesting(false);
                SearchTools.UpdateCacheSearch(cachekey, recipes);
                SearchTools.WAITING_API_2 = false;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        return recipes;
    }

    private String appendAllergies(String original, String allergies)
    {
        ArrayList<String> list = SearchTools.ParseList(allergies);

        for(String al: list)
        {
            original = original.concat("&allowedAllergy[]=").concat(YummlyRecipeFactory.allergies.get(al));
        }

        return original;
    }

    private String appendCuisine(String original, String cuisine)
    {
        return original.concat("allowedCuisine[]=").concat(YummlyRecipeFactory.cuisines.get(cuisine));
    }

    private String prepareIngredientQuery(String list)
    {
        ArrayList<String> ingredients = SearchTools.ParseList(list);
        String query = "?_app_id=8f371517&_app_key=0d24ad13bad93da625d68ba50920fe9d";
        for(int i = 0; i < ingredients.size(); i++)
        {
            query = query.concat("&allowedIngredient[]="+ingredients.get(i));
        }


        query = query.replaceAll(" ", "");
        return query;
    }
}

