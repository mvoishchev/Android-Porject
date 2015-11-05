package t4.csc413.smartchef;

/**
 * Created by Thomas X Mei on 11/2/2015.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import connectors.SearchTools;
import tools.Ingredient;
import tools.Recipe;


public class SlideMain extends FragmentActivity {
    ViewPager pager;
    PagerTabStrip tab_strp;
    static TextView v;

   public String id;
   public String api;
    public Recipe rr;
    public String ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidemain);
        TabAdapter mapager=new TabAdapter(getSupportFragmentManager());
        pager=(ViewPager)findViewById(R.id.pager);

        pager.setAdapter(mapager);
        tab_strp=(PagerTabStrip)findViewById(R.id.tab_strip);
        tab_strp.setTextColor(Color.BLACK);

        id = getIntent().getExtras().getString("id");
        api = getIntent().getExtras().getString("api");

        String id1 = id;
        String api1 = api;

        v = (TextView)findViewById(R.id.title);

        Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
        rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());

        String title = rr.getName();
        for(Ingredient ingredient: rr.getIngredients())
        {
            ingredients =ingredient.original_discription + "\n--";
        }
        v.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
