package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import connectors.SearchTools;
import tools.Recipe;
/**
 *
 * Fragment to display information for preparation time
 * Created by Thomas X Mei
 * Also worked on by Harjit
 */

public class ResultsActivity extends NavBaseActivity {
    //NavDrawer variables
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    ListView list;
    TextView search_input;

    List<String> name;
    List<String> desc;
    List<String> iv;
    List<Recipe> recipes;
    String search;
    String allergies;
    String cuisine;
    String text_for_results;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_results);

        desc = new ArrayList<String>();
        name = new ArrayList<String>();
        iv = new ArrayList<String>();

        search = getIntent().getExtras().getString("search");
        allergies = getIntent().getExtras().getString("allergies");
        cuisine = getIntent().getExtras().getString("cuisine");

        recipes = SearchTools.GetRecipes(search, allergies, cuisine, null);
        int size = recipes.size();
        Toast t = Toast.makeText(getApplicationContext(), "Sorry but there are no recipes for the searched ingredient(s). " +
                        "Please go back and try again!", Toast.LENGTH_LONG);
        if(size == 0){
            t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            LinearLayout toastLayout = (LinearLayout) t.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            t.show();
        }
        for (int recipe = 0; recipe < recipes.size(); recipe++)
        {
            Recipe temp = recipes.get(recipe);
            name.add(temp.getName());
            desc.add(temp.getId());
            iv.add(temp.getImageUrl());
        }

        text_for_results = "Recipe Search results for: " +search;
        search_input = (TextView) findViewById(R.id.textView17);
        search_input.setText(text_for_results);

        list = (ListView) findViewById(R.id.listView);

        ListAdapter theAdapter = new RecipeAdapter(this, name, recipes);
        list.setAdapter(theAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Bundle bundle = new Bundle();
                Intent i = new Intent(ResultsActivity.this, LoadingActivity.class);
                bundle.putString("api", recipes.get(position).getApi());
                bundle.putString("id", recipes.get(position).getId());
                bundle.putString("name", recipes.get(position).getName());
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        //to make NavDrawer display
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }
}
