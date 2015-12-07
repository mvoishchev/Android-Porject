package database.recipedb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import database.DataBaseManager;
import t4.csc413.smartchef.LoadingActivity;
import t4.csc413.smartchef.R;
import t4.csc413.smartchef.RecipeViewActivity;
import t4.csc413.smartchef.ResultsActivity;
import t4.csc413.smartchef.SlideMain;


/**
 * Created by Marc based on Harjit's IngredientDisplayAdapter
 * Edited to handle recipes instead of ingredients and fridge
 *
 * Class to handle how Database Activities that are involved with showing a database of ingredients
 * will display the ingredients
 */
public class RecipeDisplayAdapter extends BaseAdapter implements ListAdapter{

    private ArrayList<String> recipes;
    private Context context;
    public RecipeDisplayAdapter(ArrayList<String> list, Context context){
        recipes = list;
        this.context = context;
    }

    @Override
    public int getCount(){
        return recipes.size();
    }

    @Override
    public String getItem(int pos){
        return recipes.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return 0;
    }

    /**
     *
     * @param position position of object on the ListView
     * @param convertView View that is displayed
     * @param parent
     * @return Updated View that reflects changes to a Database
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recipe_db_listview, null);
        }

        TextView text = (TextView) view.findViewById(R.id.recipe_name);
        text.setText(getItem(position));

        // delete button
        Button button = (Button)view.findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipes.remove(position);
                ((RecipeDBLayout) context).removeRecipe(position);
                notifyDataSetChanged();
            }
        });

        // view recipe instructions
        Button viewInstructions = (Button)view.findViewById(R.id.viewInstructions);
        viewInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("url", RecipeDBLayout.getRecipeUrl(position));
                bundle.putString("name", RecipeDBLayout.getRecipeTitle(position));
                Intent intent = new Intent(context, LoadingActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

}

