package t4.csc413.smartchef;

/**
 * Created by Thomas X Mei on 11/2/2015.
 */

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import connectors.SearchTools;
import tools.Ingredient;
import tools.Recipe;


public class SlideMain extends NavBaseActivity {
    ViewPager pager;
    PagerTabStrip tab_strp;
    static TextView v;

    public String id;
    public String api;
    public Recipe rr;
    public String ingredients;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidemain);
        TabAdapter mapager=new TabAdapter(getSupportFragmentManager());
        pager=(ViewPager)findViewById(R.id.pager);

        pager.setAdapter(mapager);
        tab_strp=(PagerTabStrip)findViewById(R.id.tab_strip);
        tab_strp.setTextColor(Color.WHITE);
        tab_strp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        id = getIntent().getExtras().getString("id");
        api = getIntent().getExtras().getString("api");

        String id1 = id;
        String api1 = api;

        v = (TextView)findViewById(R.id.title);
        i = (ImageView)findViewById(R.id.imageView3);

        Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
        rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());

        //String title = rr.getName();
        for(Ingredient ingredient: rr.getIngredients())
        {
            ingredients =ingredient.original_discription + "\n--";
        }
        //v.setText(title);



        Bitmap bitmap = getBitmapFromURL(rr.getImageUrl());
        i.setImageBitmap(bitmap);


        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }
}
