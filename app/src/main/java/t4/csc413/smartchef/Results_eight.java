package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import connectors.AbstractRecipeFactory;
import connectors.SearchTools;
import connectors.spoonacular.SpoonacularRecipeFactory;
import tools.Ingredient;
import tools.Recipe;

/**
 * Created by Thomas X Mei on 10/13/2015.
 */
public class Results_eight extends ActionBarActivity
{

    static TextView v;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_two);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        v = (TextView)findViewById(R.id.text);

        Recipe recipe = SearchTools.GetRecipePreviewById("Spoonacular", "683492");
        Recipe rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());

        String text = rr.getName() + "\n\nIngredients:\n\n";

        for(
                Ingredient ingredient: rr.getIngredients())
        {
            text = text.concat(ingredient.original_discription + "\n");
        }
        String instructions = rr.getInstructions();
        text = text.concat(instructions + "\n");
        text = text.concat("\n\nUrl: " + rr.getRecipeUrl());

        v.setText(text);
    }
}
