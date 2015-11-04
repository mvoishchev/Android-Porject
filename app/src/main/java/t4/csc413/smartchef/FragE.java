package t4.csc413.smartchef;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import connectors.SearchTools;
import tools.Recipe;

import static t4.csc413.smartchef.R.id.SiteButton;

public class FragE extends android.support.v4.app.Fragment {
    static TextView v;
    String url;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_frag_e,container,false);

        String id = getActivity().getIntent().getExtras().getString("id");
        String api = getActivity().getIntent().getExtras().getString("api");


        v = (TextView) view.findViewById(R.id.TextFE);

        Recipe recipe = SearchTools.GetRecipePreviewById(api, id);
        Recipe rr = SearchTools.GetRecipeByUrl(recipe.getRecipeUrl());

        String text = rr.getRecipeUrl();
        v.setText(text);
        url = rr.getRecipeUrl();

        Button newPage = (Button)view.findViewById(SiteButton);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent u = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
                startActivity(u);
            }
        });


        return  view;




    }


}
