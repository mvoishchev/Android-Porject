package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import connectors.SearchTools;
import tools.Recipe;

public class ResultsActivity extends NavBaseActivity {

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



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_results);
        // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(policy);

        desc = new ArrayList<String>();
        name = new ArrayList<String>();
        iv = new ArrayList<String>();

        search = getIntent().getExtras().getString("search");
        allergies = getIntent().getExtras().getString("allergies");
        cuisine = getIntent().getExtras().getString("cuisine");

        recipes = SearchTools.GetRecipes(search, allergies, cuisine, null);

        for (int recipe = 0; recipe < recipes.size(); recipe++) {
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
        ListView theListView = (ListView) findViewById(R.id.listView);
        list.setAdapter(theAdapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bundle bundle = new Bundle();
                Intent i = new Intent(ResultsActivity.this, SlideMain.class);
                bundle.putString("api", recipes.get(position).getApi());
                bundle.putString("id", recipes.get(position).getId());
                i.putExtras(bundle);
                startActivity(i);

            }
        });

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

}
