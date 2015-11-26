package connectors.spoonacular;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import connectors.spoonacular.SpoonacularModels.*;

import java.lang.reflect.Type;

/**
 * Created by AGCOSTFU on 10/11/2015.
 */
public class GetRecipeListDeserializerJson implements JsonDeserializer<GetRecipeListResult>
{
    /**
     *
     * @param je Json Element to deserialize
     * @param type Override param
     * @param jdc
     * @return GetRecipeListResult object generated from JSON returned from Spoonacular API
     * @throws JsonParseException
     */
    @Override
    public GetRecipeListResult deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {

       JsonElement content = je.getAsJsonObject();
       return new Gson().fromJson(content, GetRecipeListResult.class);

    }
}
