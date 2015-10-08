package tools;

/**
 * Created by Harjit Randhawa on 10/6/2015.
 *
 * Class that will handle information about Ingredients.
 * Made Ingredient its own class so that maybe later we can program other functions in it
 * so that we can derive the measurements and instructions from the original_discription and use that
 * information for our converter tools and cooking tips.
 */
public class Ingredient
{

    public String original_discription;

    public Ingredient(String input_line)
    {
        original_discription = input_line;
    }


}
