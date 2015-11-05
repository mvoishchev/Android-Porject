package t4.csc413.smartchef;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import connectors.evernote.LoginActivity;
import android.widget.Button;
import android.content.DialogInterface;

public class MainActivity extends NavBaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    protected CharSequence[] _seasonal = {"a", "b", "c", "d", "e", "f"};
    protected boolean[] _selection = new boolean[_seasonal.length];
    protected CharSequence[] _cuisine = {"American", "Indian", "Italian", "Chinese", "Thai", "French", "Spanish", "Philipino"};
    protected boolean[] _selections = new boolean[_cuisine.length];
    protected Button advanceSearchButton;
    protected Button _seasonalButton;
    protected Button _cuisineButton;

    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        et = (EditText)findViewById(R.id.EditText01);

        advanceSearchButton = (Button) findViewById(R.id.button);
        _cuisineButton = (Button) findViewById(R.id.button2);
        _cuisineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
        _cuisineButton.setVisibility(View.INVISIBLE);
        _seasonalButton = (Button) findViewById(R.id.button3);
        _seasonalButton.setVisibility(View.INVISIBLE);
        _seasonalButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
        advanceSearchButton.setVisibility(View.VISIBLE);
        advanceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _cuisineButton.setVisibility(View.VISIBLE);
                _seasonalButton.setVisibility(View.VISIBLE);
            }
        });

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            EnGps.displayPromptForEnablingGPS(this);

        Eula.show(this);

        // Nav Drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml
        set(navMenuTitles,navMenuIcons);
    }
    @Override
    public Dialog onCreateDialog(int id) {
        Log.i("output", "MainActivity");

        AlertDialog.Builder menu = new AlertDialog.Builder(this);

        System.out.println("THE ID IS: " + id);
        if (id == 1) {
            return new AlertDialog.Builder(this).setTitle("Seasonal")
                    .setMultiChoiceItems(_seasonal, _selection, new DialogSelectionClickHandler())
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();
        } else if (id == 0) {
            return new AlertDialog.Builder(this).setTitle("Cuisine")
                    .setMultiChoiceItems(_cuisine, _selections, new DialogSelectionClickHandler())
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        return null;

    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
        public void onClick(DialogInterface dialog, int clicked, boolean selected) {
            Log.i("ME", _seasonal[clicked] + " selected: " + selected);
            Log.i("ME", _cuisine[clicked] + " selected: " + selected);
        }
    }


    public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int clicked) {
            switch (clicked) {
                case DialogInterface.BUTTON_POSITIVE:
                    printSelectedCuisine();
                    break;

            }
        }
        protected void printSelectedCuisine() {
            for (int i = 0; i < _cuisine.length; i++) {
                Log.i("ME", _cuisine[i] + " selected: " + _selections[i]);
                //Log.i( "ME", _seasonal[ i ] + " selected: " + _selection[i] );

            }

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void searchByIngredient(View v)
    {
        Bundle bundle = new Bundle();

        bundle.putString("search", et.getText().toString());
        bundle.putString("allergies", ""/*allergies here**/);
        bundle.putString("useFridge", ""/*Use Fridge */);
        bundle.putString("cupboard",""/*Use Fridge */);

        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void onButtonClick(View view)
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
