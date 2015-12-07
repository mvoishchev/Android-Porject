package t4.csc413.smartchef;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import connectors.evernote.EvernoteActivity;
import connectors.evernote.EvernoteManager;
import database.DataBaseManager;
import tools.Ingredient;
import database.recipedb.RecipeDBLayout;

import static t4.csc413.smartchef.R.id.SQLButton;
import static t4.csc413.smartchef.R.id.SiteButton;
import static t4.csc413.smartchef.R.id.shoppinglist;
/**
 *
 * Fragment to display information for General information
 * and buttons for users to upload and update information
 * Created by Thomas X Mei
 */

public class FragA extends android.support.v4.app.Fragment
{
    static TextView v;
    View view;
    String text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_frag_a_,container,false);

        final SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFA);
        TextView swipe = (TextView)view.findViewById(R.id.swipeText);
        TranslateAnimation animation = new TranslateAnimation(-80.0f, 0.0f,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(10);  // animation repeat count
        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        swipe.startAnimation(animation);

        text = "Ingredients:\n\n";
        for(Ingredient ingredient: m.rr.getIngredients())
        {
            text = text.concat("--"+ingredient.original_discription + "\n");
        }
        v.setText(text);

        Button evernote = (Button)view.findViewById(SiteButton);
        evernote.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), EvernoteActivity.class);
                EvernoteManager.getInstance(getActivity().getApplicationContext()).createNewShoppingList(m.rr.getName(), text, getActivity());
            }
        });

        Button SQL = (Button)view.findViewById(SQLButton);
        SQL.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                /*Intent intent = new Intent(getActivity(), RecipeDBLayout.class);
                startActivity(intent);*/
                DataBaseManager.AddRecipeToFavorites(m.rr);
                Toast.makeText(m, "Recipe Saved!", Toast.LENGTH_SHORT).show();
            }
        });

        Button shoppingList = (Button)view.findViewById(shoppinglist);
        shoppingList.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DataBaseManager.UpdateShoppingList(m.rr);
                Toast.makeText(m, "Shopping List updated!", Toast.LENGTH_SHORT).show();
            }
        });
        return  view;
    }
}