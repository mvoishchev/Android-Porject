package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.content.Intent;

import com.evernote.client.android.asyncclient.EvernoteSearchHelper;

import connectors.SearchTools;
import tools.Recipe;


/*
   Created by poulomirajarshi on 10/5/15.
 */
public class LoadingActivity extends Activity {
    String search;
    String allergies;
    String cuisine;
    String url;
    String name;
    boolean searchingRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        search = getIntent().getExtras().getString("search");
        allergies = getIntent().getExtras().getString("allergies");
        cuisine = getIntent().getExtras().getString("cuisine");
        url = getIntent().getExtras().getString("url");
        name = getIntent().getExtras().getString("name");

        String id = getIntent().getExtras().getString("id");
        String api = getIntent().getExtras().getString("api");

        if (search != null) {
            SearchTools.GetRecipes(search, allergies, cuisine, null);
            searchingRecipes = true;
        } else {
            if(url == null) {
                Recipe rec = SearchTools.GetRecipePreviewById(api, id);
                url = rec.getRecipeUrl();
            }

            SearchTools.GetRecipeByUrl(url);
            searchingRecipes = false;
        }

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!SearchTools.isWaiting()) {
                    if (searchingRecipes) {
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString("search", search);
                        if (cuisine != null)
                            bundle.putString("cuisine", cuisine);
                        if (allergies != null)
                            bundle.putString("allergies", allergies); /*allergies here**/

                        Intent i = new Intent(LoadingActivity.this, ResultsActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    } else {
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        bundle.putString("name", name);
                        Intent i = new Intent(LoadingActivity.this, SlideMain.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                } else {
                    iv.startAnimation(an);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

}
