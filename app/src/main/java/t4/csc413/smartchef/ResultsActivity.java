package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import connectors.SearchTools;
import tools.Recipe;

public class ResultsActivity extends Activity {


    ListView list;
    List<String> name;
    List<String> desc;
    List<Recipe> recipes;
    String search;
    int [] images = {R.drawable.recimg1,R.drawable.recimg2,R.drawable.recimg3,
            R.drawable.recimg4,R.drawable.recimg5,R.drawable.recimg6,R.drawable.recimg7,
            R.drawable.recimg8,R.drawable.recimg9,R.drawable.recimg10,};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_results);

        desc = new ArrayList<String>();
        name = new ArrayList<String>();

        search = getIntent().getExtras().getString("search");
        recipes = SearchTools.GetRecipes(search, null, null, null);

        for(int recipe = 0; recipe < recipes.size(); recipe++)
        {
            Recipe temp = recipes.get(recipe);
            name.add(temp.getName());
            desc.add(temp.getId());

        }
       /* Resources res = getResources();
        name = res.getStringArray(R.array.titles);
        desc = res.getStringArray(R.array.descriptions);*/

        list= (ListView) findViewById(R.id.listView);


        //  ArrayList<Recipe> recipes = SearchTools.GetRecipes(Search,null,null,null);




        ListAdapter theAdapter = new RecipeAdapter(this,name);
        ListView theListView = (ListView) findViewById(R.id.listView);
        list.setAdapter(theAdapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bundle bundle = new Bundle();
                Intent i = new Intent(ResultsActivity.this, RecipeViewActivity.class);
                bundle.putString("api", recipes.get(position).getApi());
                bundle.putString("id", recipes.get(position).getId());
                i.putExtras(bundle);
                startActivity(i);

                /*switch (position) {
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
                        newActivity6.putExtra("api", recipes.get(position).getApi());
                        newActivity6.putExtra("id", recipes.get(position).getId());
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
                        break;*/


                //}


            }

            @SuppressWarnings("unused")
            public void onClick(View v) {
            }

            ;
        });






    }

    //public static String Search = "milk, eggs";





}
