package t4.csc413.smartchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import connectors.SearchTools;
import tools.Ingredient;
import tools.Recipe;

public class FragA extends android.support.v4.app.Fragment {

    static TextView v;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_frag_a_,container,false);

       String id = getActivity().getIntent().getExtras().getString("id");
       String api = getActivity().getIntent().getExtras().getString("api");


        v = (TextView) view.findViewById(R.id.TextFA);

        Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
        Recipe rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());


        String title = recipe.getName();
        String text =  title
        + "\n\nIngredients:\n\n";

        for(Ingredient ingredient: rr.getIngredients())
        {
            text = text.concat(ingredient.original_discription + "\n--");
        }
       v.setText(text);


        return  view;
    }
}
