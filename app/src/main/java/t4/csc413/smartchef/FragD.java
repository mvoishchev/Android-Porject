package t4.csc413.smartchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import connectors.SearchTools;
import tools.Recipe;

public class FragD extends android.support.v4.app.Fragment {
    static TextView v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_frag_d,container,false);

        String id = getActivity().getIntent().getExtras().getString("id");
        String api = getActivity().getIntent().getExtras().getString("api");


        v = (TextView) view.findViewById(R.id.TextFD);

        Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
        Recipe rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());

        String cuisine_Type = rr.getCuisine();

        String text ="\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + cuisine_Type;
        v.setText(text);


        return  view;
    }
}