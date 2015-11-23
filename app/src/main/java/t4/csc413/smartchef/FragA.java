package t4.csc413.smartchef;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import connectors.evernote.EvernoteActivity;
import connectors.evernote.EvernoteManager;
import connectors.google.MapsActivity;
import tools.Ingredient;
import database.recipedb.RecipeDBLayout;

import static t4.csc413.smartchef.R.id.SQLButton;
import static t4.csc413.smartchef.R.id.SiteButton;


public class FragA extends android.support.v4.app.Fragment {

    static TextView v;
    View view;
    String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_frag_a_,container,false);

        final SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFA);
       // swipe = (TextView) view.findViewById(R.id.textView5);

        text =  m.rr.getName() + "\n\nIngredients:\n\n";

        for(Ingredient ingredient: m.rr.getIngredients())
        {
            text = text.concat("--"+ingredient.original_discription + "\n");
        }

        v.setText(text);

        Button evernote = (Button)view.findViewById(SiteButton);
        evernote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EvernoteActivity.class);
                //TODO change this so that real values are uploaded insted of "Test" and random()
                //TODO done
                EvernoteManager.getInstance(getActivity().getApplicationContext()).createNewShoppingList(m.rr.getName(), text, getActivity());
            }
        });

        Button SQL = (Button)view.findViewById(SQLButton);
        SQL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeDBLayout.class);
                startActivity(intent);
            }
        });

        return  view;
    }
}
