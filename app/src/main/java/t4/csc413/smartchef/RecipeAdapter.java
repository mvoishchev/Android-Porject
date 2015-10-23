package t4.csc413.smartchef;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas X Mei on 10/14/2015.
 */
class RecipeAdapter extends ArrayAdapter<String>
{
    public RecipeAdapter(Context context, List<String> values)
    {
        super(context, R.layout.single_row,values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.single_row, parent, false);

        String results = getItem(position);
        TextView theTextView = (TextView) theView.findViewById(R.id.textView8);
        theTextView.setText(results);


        ImageView img = (ImageView)  theView.findViewById(R.id.imageView);
        if(position == 0) {
            img.setImageResource(R.drawable.recimg1);
        }else if(position == 1) {
            img.setImageResource(R.drawable.recimg2);
        }else if(position == 2) {
            img.setImageResource(R.drawable.recimg3);
        }else if(position == 3) {
            img.setImageResource(R.drawable.recimg4);
        }else if(position == 4) {
            img.setImageResource(R.drawable.recimg5);
        }else if(position == 5) {
            img.setImageResource(R.drawable.recimg6);
        }else if(position == 6) {
            img.setImageResource(R.drawable.recimg7);
        }else if(position == 7) {
            img.setImageResource(R.drawable.recimg8);
        }else if(position == 8) {
            img.setImageResource(R.drawable.recimg9);
        }else if(position == 9) {
            img.setImageResource(R.drawable.recimg10);
        }

        return theView;

    }
}