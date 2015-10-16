package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import connectors.SearchTools;
import tools.Recipe;

/**
 * Created by Thomas X Mei on 10/14/2015.
 */
public class The_Results extends Activity {


    ListView list;
    String [] name;
    String [] desc;
    int [] images = {R.drawable.recimg1,R.drawable.recimg2,R.drawable.recimg3,
            R.drawable.recimg4,R.drawable.recimg5,R.drawable.recimg6,R.drawable.recimg7,
            R.drawable.recimg8,R.drawable.recimg9,R.drawable.recimg10,};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_results);

        Resources res = getResources();
        name = res.getStringArray(R.array.titles);
        desc = res.getStringArray(R.array.descriptions);

        list= (ListView) findViewById(R.id.listView);


      //  ArrayList<Recipe> recipes = SearchTools.GetRecipes(Search,null,null,null);




        ListAdapter theAdapter = new coolAdapter(this,name);
        ListView theListView = (ListView) findViewById(R.id.listView);
        list.setAdapter(theAdapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        Intent newActivity = new Intent(The_Results.this, Results_one.class);
                        startActivity(newActivity);
                        break;
                    case 1:
                        Intent newActivity2 = new Intent(The_Results.this, Results_two.class);
                        startActivity(newActivity2);
                        break;
                    case 2:
                        Intent newActivity3 = new Intent(The_Results.this, Results_three.class);
                        startActivity(newActivity3);
                        break;
                    case 3:
                        Intent newActivity4 = new Intent(The_Results.this, Results_four.class);
                        startActivity(newActivity4);
                        break;
                    case 4:
                        Intent newActivity5 = new Intent(The_Results.this, Results_five.class);
                        startActivity(newActivity5);
                        break;
                    case 5:
                        Intent newActivity6 = new Intent(The_Results.this, Results_six.class);
                        startActivity(newActivity6);
                        break;
                    case 6:
                        Intent newActivity7 = new Intent(The_Results.this, Results_seven.class);
                        startActivity(newActivity7);
                        break;
                    case 7:
                        Intent newActivity8 = new Intent(The_Results.this, Results_eight.class);
                        startActivity(newActivity8);
                        break;
                    case 8:
                        Intent newActivity9 = new Intent(The_Results.this, Results_nine.class);
                        startActivity(newActivity9);
                        break;
                    case 9:
                        Intent newActivity10 = new Intent(The_Results.this, Results_ten.class);
                        startActivity(newActivity10);
                        break;


                }


            }

            @SuppressWarnings("unused")
            public void onClick(View v) {
            }

            ;
        });






    }

    //public static String Search = "milk, eggs";





}


