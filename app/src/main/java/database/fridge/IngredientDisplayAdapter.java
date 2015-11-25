package database.fridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import t4.csc413.smartchef.R;

/**
 * Created by Harjit on 11/24/2015.
 */
public class IngredientDisplayAdapter extends BaseAdapter implements ListAdapter{

    private ArrayList<String> ingredients;
    private FridgeLayout context;
    public IngredientDisplayAdapter(ArrayList<String> list, FridgeLayout context){
        ingredients = list;
        this.context = context;
    }

    @Override
    public int getCount(){
        return ingredients.size();
    }

    @Override
    public String getItem(int pos){
        return ingredients.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fridge_db_listview, null);
        }

        TextView text = (TextView) view.findViewById(R.id.ingredient_name);
        text.setText(getItem(position));

        Button button = (Button)view.findViewById(R.id.delete);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.remove(position);
                context.removeIngredient(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

}
