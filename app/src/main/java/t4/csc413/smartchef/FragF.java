package t4.csc413.smartchef;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import connectors.SearchTools;
import connectors.evernote.EvernoteActivity;
import connectors.evernote.EvernoteManager;
import tools.Recipe;

import static t4.csc413.smartchef.R.id.EButton;
import static t4.csc413.smartchef.R.id.GMButton;
import static t4.csc413.smartchef.R.id.YT;

public class FragF extends android.support.v4.app.Fragment {
    static TextView v;
    String url;
    RatingBar star;
    View view;
    ImageView newPage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_frag_f,container,false);
        setStar(4);

        final SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        url = "https://www.youtube.com/results?search_query=" + m.rr.getName();

        newPage = (ImageView)view.findViewById(YT);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent u = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
                startActivity(u);
            }
        });

        //Changed to upload shopping list, instead of opening activity -Juris
        Button evernote = (Button)view.findViewById(EButton);
        evernote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EvernoteActivity.class);
                //TODO change this so that real values are uploaded insted of "Test" and random()
                EvernoteManager.getInstance(getActivity().getApplicationContext()).createNewShoppingList(m.rr.getName(),m.ingredients , getActivity());
            }
        });

        Button Gmaps = (Button)view.findViewById(GMButton);
        Gmaps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });



        return  view;
    }

    public void setStar(float score)
    {
        star = (RatingBar) view.findViewById(R.id.RB);
        star.setRating(score);
    }


}
