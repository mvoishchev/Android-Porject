package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.content.Intent;

import connectors.SearchTools;


/*
   Created by poulomirajarshi on 10/5/15.
 */
public class LoadingActivity extends Activity {
    String search;
    String allergies;
    String cuisine;
    String url;
    boolean searchingRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        search = getIntent().getExtras().getString("search");
        allergies = getIntent().getExtras().getString("allergies");
        cuisine = getIntent().getExtras().getString("cuisine");
        url = getIntent().getExtras().getString("url");

        if(search != null) {
            SearchTools.GetRecipes(search, allergies, cuisine, null);
            searchingRecipes = true;
        }
        else {
            SearchTools.GetRecipeByUrl(url);

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
                if(!SearchTools.isWaiting()) {
                    finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("search", search);
                    if(cuisine != null)
                        bundle.putString("cuisine", cuisine);
                    if(allergies != null)
                        bundle.putString("allergies",allergies); /*allergies here**/

                    Intent i = new Intent(LoadingActivity.this, ResultsActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }else
                {
                    iv.startAnimation(an);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

}
