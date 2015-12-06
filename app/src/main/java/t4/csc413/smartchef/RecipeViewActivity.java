package t4.csc413.smartchef;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import connectors.SearchTools;
import t4.csc413.smartchef.R;
import tools.Ingredient;
import tools.Recipe;

/**
 * Created by Thomas X Mei on 10/21/2015.
 * Also worked on by Harjit
 */
public class RecipeViewActivity extends ActionBarActivity
{
    private static Button next;
    private static TextSwitcher textswitcher;
    // String array to be shown on textSwitcher
    String [] textToShow = new String[5];


    // Total length of the string array
    int messageCount = 5;
    // to keep current Index of text
    int currentIndex = -1;

    static TextView v;
    static TextView name;
    String url;
    RatingBar star;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);
        String id = getIntent().getExtras().getString("id");
        String api = getIntent().getExtras().getString("api");
        String url = getIntent().getExtras().getString("url");

        name = (TextView)findViewById(R.id.textView9001);
        v = (TextView)findViewById(R.id.text);
        Recipe rr;
        try {
            if (api != null && id != null) {
                Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
                rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());
            } else {
                rr = SearchTools.GetRecipeByUrl(url);
            }
        }catch(Exception e){
            rr = new Recipe();
        }
        String title = rr.getName();
        String text =  "Ingredients:\n\n";
        String text1 = "Ingredients:";

        for(Ingredient ingredient: rr.getIngredients())
        {
            text = text.concat(ingredient.original_discription + "\n--");
            text1 = text.concat("--"+ingredient.original_discription + "\n");
        }

        String instructions = rr.getInstructions();
        int prep_Hours = rr.getPrepTime_hours();
        int prep_Minutes = rr.getPrepTime_minutes();
        String cuisine_Type = rr.getCuisine();

        text = text.concat("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tInstructions:\n\n" +instructions + "\n");
        String text2 = "Instructions:\n\n" +instructions + "\n";
        text = text.concat("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tPreperation Time:\n\t\t\t\t\t\t" +
                "\t\t\t\t\t\tHours: " + prep_Hours +
                "\t\t\tMinutes: " + prep_Minutes);
        String text3 = "Preperation Time:" +
                "\n\nHours: " + prep_Hours +
                "\tMinutes: " + prep_Minutes;
        text = text.concat("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCuisine Type:" +
                "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + cuisine_Type);
        String text4 = "Cuisine Type:\n\n" +
                cuisine_Type;

        text = text.concat("Url:\n" + rr.getRecipeUrl());
        String text5 = "Url:\n\n" + rr.getRecipeUrl();

        name.setText(title);
        v.setText(text);
        v.setMovementMethod(new ScrollingMovementMethod());
        url = "https://www.youtube.com/results?search_query=" + rr.getName();

        // Call all the methods
        init();
        loadAnimations();
        setFactory();
        setListener();

        textToShow[0] = text1;
        textToShow[1] = text2;
        textToShow[2] = text3;
        textToShow[3] = text4;
        textToShow[4] = text5;
    }

    /**
     * Method to open up a browser from string input
     * @param view
     */
    public void bowser(View view)
    {
        Intent u = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
        startActivity(u);
    }

    void init() {
        next = (Button) findViewById(R.id.buttonNext);
        textswitcher = (TextSwitcher) findViewById(R.id.textSwitcher);

    }

    void loadAnimations() {

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        // set the animation type of textSwitcher
        textswitcher.setInAnimation(in);
        textswitcher.setOutAnimation(out);
    }

    // Click listener method for button
    void setListener() {

        // ClickListener for NEXT button
        // When clicked on Button TextSwitcher will switch between texts
        // The current Text will go OUT and next text will come in with
        // specified animation
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                currentIndex++;
                // If index reaches maximum reset it
                if (currentIndex == messageCount)
                    currentIndex = 0;

                // Set the textSwitcher text according to current index from
                // string array
                textswitcher.setText(textToShow[currentIndex]);
            }
        });
    }

    // Set Factory for the textSwitcher *Compulsory part
    void setFactory() {
        textswitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {

                // Create run time textView with some attributes like gravity,
                // color, etc.
                TextView myText = new TextView(RecipeViewActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(30);
                myText.setTextColor(Color.BLUE);
                return myText;
            }
        });
    }
}
