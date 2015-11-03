package t4.csc413.smartchef;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import connectors.SearchTools;
import connectors.evernote.EvernoteActivity;
import connectors.evernote.EvernoteManager;
import t4.csc413.smartchef.R;
import tools.Ingredient;
import tools.Recipe;


/**
 * Created by AGCOSTFU on 10/21/2015.
 */
public class RecipeViewActivity extends ActionBarActivity
{

    static TextView v;
    String ingredients;
    String name;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);
        String id = getIntent().getExtras().getString("id");
        String api = getIntent().getExtras().getString("api");

        System.out.println("api: " + api);
        System.out.println("id: " + id);

        v = (TextView)findViewById(R.id.text);

        Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
        Recipe rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());

        String text = rr.getName() + "\n\nIngredients:\n\n";
        String name = rr.getName();

        for(
                Ingredient ingredient: rr.getIngredients())
        {
            text = text.concat(ingredient.original_discription + "\n");
            ingredients = ingredient.original_discription + "\n";
        }
        String instructions = rr.getInstructions();
        text = text.concat(instructions + "\n");
        text = text.concat("\n\nUrl: " + rr.getRecipeUrl());

        v.setText(text);
    }

    public void onClickMethod(View view)
    {
        EvernoteManager.getInstance(this.getApplicationContext()).createNewShoppingList("yes", "this is ok", this);
    }

    public void uploadButtonClick(View view) {
        EvernoteManager.getInstance(this.getApplicationContext()).updateMainShoppingList(String.valueOf(Math.random()), this);
    }
}
