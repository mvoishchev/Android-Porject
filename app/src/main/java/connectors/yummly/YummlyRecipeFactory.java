package connectors.yummly;

import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

        allergies.put("Diary", "396%5EDairy-Free");
        allergies.put("Egg", "397%5EEgg-Free");
        allergies.put("Gluten", "393%5EGluten-Free");
        allergies.put("Peanut", "394%5EPeanut-Free");
        allergies.put("Seafood", "398%5ESeafood-Free");
        allergies.put("Sesame", "399%5ESesame-Free");
        allergies.put("Soy", "400%5ESoy-Free");
        allergies.put("Sulfite", "401%5ESulfite-Free");
        allergies.put("Wheat", "392%5EWheat-Free");

        cuisines = new HashMap<String, String>();
        cuisines.put("American", "cuisine%5Ecuisine-american");
        cuisines.put("Kid-Friendly", "cuisine%5Ecuisine-kid-friendly");
        cuisines.put("Italian", "cuisine%5Ecuisine-italian");
        cuisines.put("Asian", "cuisine%5Ecuisine-asian");
        cuisines.put("Mexican", "cuisine%5Ecuisine-mexican");
        cuisines.put("Southern & Soul", "cuisine%5Ecuisine-southern");
        cuisines.put("BBQ", "cuisine%5Ecuisine-barbecue-bbq");
        cuisines.put("Indian", "cuisine%5Ecuisine-indian");
        cuisines.put("Chinese", "cuisine%5Ecuisine-chinese");
        cuisines.put("Cajun", "cuisine%5Ecuisine-cajun");
        cuisines.put("Mediterranean", "cuisine%5Ecuisine-mediterranean");
        cuisines.put("Greek", "cuisine%5Ecuisine-greek");
        cuisines.put("English", "cuisine%5Ecuisine-english");
        cuisines.put("Spanish", "cuisine%5Ecuisine-spanish");
        cuisines.put("Thai", "cuisine%5Ecuisine-thai");
        cuisines.put("German", "cuisine%5Ecuisine-german");
        cuisines.put("Morrocan", "cuisine%5Ecuisine-morrocan");
        cuisines.put("Irish", "cuisine%5Ecuisine-irish");
        cuisines.put("Japanese", "cuisine%5Ecuisine-japanese");
        cuisines.put("Cuban", "cuisine%5Ecuisine-cuban");
        cuisines.put("Portuguese", "cuisine%5Ecuisine-portuguese");
        cuisines.put("Swedish", "cuisine%5Ecuisine-swedish");
        cuisines.put("Hungarian", "cuisine%5Ecuisine-hungarian");
        cuisines.put("Hawaiian", "cuisine%5Ecuisine-hawaiian");
    }

    public ArrayList<String> getSupportedAllergies()
    {

        ArrayList<String> list = new ArrayList<String>();
        if(allergies != null) {
            Set<String> set = allergies.keySet();

            list.addAll(set);
        }
        return list;
    }

    public ArrayList<String> getSupportedCuisines()
    {
        ArrayList<String> list = new ArrayList<String>();

        if(cuisines != null) {
            Set<String> set = cuisines.keySet();

            list.addAll(set);
        }
        return list;
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

        if(allergies.length() > 2)
            search = appendAllergies(search, allergies);

        if(cuisine != null)
            search = appendCuisine(search, cuisine);

        System.out.println("Yummly Search: " + search);

        setRequesting(true);
        SearchTools.setWaitingAPI(2, true);
        connector.getRecipeByIngredient(search, new Callback<GetRecipeListResult>() {
            @Override
            public void success(GetRecipeListResult getRecipeListResult, Response response) {
                for(SearchRecipesResultModel model: getRecipeListResult.recipes)
                {

                    Recipe r = new Recipe();
                    r.setName(model.title);
                    r.setId(model.id);
                    r.setApi("Yummly");

                    if(model.urls != null && model.urls.size() > 0) {
                        r.addImageUrl(model.urls.get(0));
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
                setRequesting(false);
                SearchTools.UpdateCacheSearch(cachekey, recipes);
                SearchTools.setWaitingAPI(2, false);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Error");
                error.printStackTrace();
                SearchTools.setWaitingAPI(2, false);
            }
        });


        return recipes;
    }

    private String appendAllergies(String original, String allergies)
    {
        ArrayList<String> list = SearchTools.ParseList(allergies);


        for(String al: list)
        {
            original = original.concat("&allowedAllergy[]=" + YummlyRecipeFactory.allergies.get(al));
        }


        return original;
    }

    private String appendCuisine(String original, String cuisine)
    {
        return original.concat("&allowedCuisine[]=" + YummlyRecipeFactory.cuisines.get(cuisine));
    }

    private String prepareIngredientQuery(String list)
    {
        ArrayList<String> ingredients = SearchTools.ParseList(list);
        String query = "?_app_id=8f371517&_app_key=0d24ad13bad93da625d68ba50920fe9d";
        for(int i = 0; i < ingredients.size(); i++)
        {
            String line = ingredients.get(i).trim();
            query = query.concat("&allowedIngredient[]="+line);
        }


        query = query.replaceAll(" ", "+");
        return query;
    }
}

