package t4.csc413.smartchef;

import android.app.Activity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        search = getIntent().getExtras().getString("search");
        System.out.println(search);
        SearchTools.GetRecipes(search, null, null, null);

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
                    Intent i = new Intent(LoadingActivity.this, ResultsActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }else
                {
                    System.out.println("Still waiting");
                    iv.startAnimation(an);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }
}
