package t4.csc413.smartchef;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.content.Intent;

import connectors.SearchTools;
import database.fridge.FridgeDB;
import database.fridge.FridgeLayout;
import database.recipedb.RecipeDBLayout;


/*
   Created by poulomirajarshi on 10/5/15.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        init();

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                Intent i = new Intent(Splash.this,MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

    private void init()
    {
        SearchTools.init();
        RecipeDBLayout.init(this);
        FridgeLayout.init(this);
    }
}
