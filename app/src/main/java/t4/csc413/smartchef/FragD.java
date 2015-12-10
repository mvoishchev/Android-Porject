package t4.csc413.smartchef;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import connectors.evernote.EvernoteActivity;
import connectors.evernote.EvernoteManager;
import connectors.google.MapsActivity;
import tools.Ingredient;

import static t4.csc413.smartchef.R.id.SiteButton;
import static t4.csc413.smartchef.R.id.GMButton;
import static t4.csc413.smartchef.R.id.YT;
/**
 *
 * Fragment to display information for accessing google maps, youtube and website
 * Created by Thomas X Mei
 */
public class FragD extends android.support.v4.app.Fragment
{
    static TextView v;
    String url;
    View view;
    Button newPage;
    String siteUrl;
    String text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_frag_d,container,false);

        final SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        url = "https://www.youtube.com/results?search_query=" + m.rr.getName();
        text =  "\n\nIngredients:\n\n";
        for(Ingredient ingredient: m.rr.getIngredients())
        {
            text = text.concat("--"+ingredient.original_discription + "\n");
        }
        newPage = (Button)view.findViewById(YT);
        newPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent u = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
                startActivity(u);
            }
        });

        siteUrl = m.rr.getRecipeUrl();
        Button newPage = (Button)view.findViewById(SiteButton);
        newPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent u = new Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl) );
                startActivity(u);
            }
        });

        Button Gmaps = (Button)view.findViewById(GMButton);
        Gmaps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }
}
