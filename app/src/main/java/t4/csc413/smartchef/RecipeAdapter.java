package t4.csc413.smartchef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tools.Recipe;

/**
 * Created by Thomas X Mei on 10/14/2015.
 */
class RecipeAdapter extends ArrayAdapter<String>
{
    public RecipeAdapter(Context context, List<String> values)
    {
        super(context,R.layout.single_row,values);
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.single_row, parent, false);

        String results = getItem(position);
        TextView theTextView = (TextView) theView.findViewById(R.id.textView8);
        theTextView.setText(results);


        Recipe r = new Recipe();

        String yes = r.getImageUrl();
        ImageView img = (ImageView)  theView.findViewById(R.id.imageView);
        img.setImageBitmap(getBitmapFromURL(yes));

        return theView;

    }
}