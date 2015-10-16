package t4.csc413.smartchef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Thomas X Mei on 10/13/2015.
 */
public class Results extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        String[] results = {"Char-grilled Beef Tenderloin with Three-Herb Chimichurri",
                "Dark Chocolate Zucchini cake", "English Toffee",
                "Sweet and Salty No Bake Peanut Butter bars", "Crockpot Chicken and Quinoa Chili",
                "Pumpkin Bars", "Chocolate Avocado Pudding", "Everything Bagel Dogs",
                "Pumpkin Pie Twist","Hiyayakko Chilled Tofu"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.result_listview, results);


        ListView list = (ListView) findViewById(R.id.theListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        Intent newActivity = new Intent(Results.this, Results_one.class);
                        startActivity(newActivity);
                        break;
                    case 1:
                        Intent newActivity2 = new Intent(Results.this, Results_two.class);
                        startActivity(newActivity2);
                        break;
                    case 2:
                        Intent newActivity3 = new Intent(Results.this, Results_three.class);
                        startActivity(newActivity3);
                        break;
                    case 3:
                        Intent newActivity4 = new Intent(Results.this, Results_four.class);
                        startActivity(newActivity4);
                        break;
                    case 4:
                        Intent newActivity5 = new Intent(Results.this, Results_five.class);
                        startActivity(newActivity5);
                        break;
                    case 5:
                        Intent newActivity6 = new Intent(Results.this, Results_six.class);
                        startActivity(newActivity6);
                        break;
                    case 6:
                        Intent newActivity7 = new Intent(Results.this, Results_seven.class);
                        startActivity(newActivity7);
                        break;
                    case 7:
                        Intent newActivity8 = new Intent(Results.this, Results_eight.class);
                        startActivity(newActivity8);
                        break;
                    case 8:
                        Intent newActivity9 = new Intent(Results.this, Results_nine.class);
                        startActivity(newActivity9);
                        break;
                    case 9:
                        Intent newActivity10 = new Intent(Results.this, Results_ten.class);
                        startActivity(newActivity10);
                        break;


                }





            }

            @SuppressWarnings("unused")
            public void onClick(View v) {
            }

            ;
        });






        /*


        String[] results = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
                results);

        ListView theListView = (ListView) findViewById(R.id.theListView);
        theListView.setAdapter(theAdapter);

        AdapterView.OnItemClickListener onItemClickListener = theListView.getOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        switch(position){
                            case 0:
                        }

                    }
                }
        );


*/




    }
}




/*
  String[] results = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
                results);

        ListView theListView = (ListView) findViewById(R.id.theListView);
        theListView.setAdapter(theAdapter);

        AdapterView.OnItemClickListener onItemClickListener = theListView.getOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                        if (position == 1) {
                            Intent i = new Intent(this,Results_one.class);
                            startActivity(i);
                        }
                    }
                }
        );
 */