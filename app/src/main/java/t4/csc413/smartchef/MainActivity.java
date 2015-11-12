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
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.List;

public class MainActivity extends NavBaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    protected CharSequence[] _seasonal = {"a", "b", "c", "d", "e", "f"};
    protected boolean[] _seasonSelection = new boolean[_seasonal.length];
    protected String[] _cuisine = {"American", "Indian", "Italian", "Chinese", "Thai", "French", "Spanish", "Philipino","Korean",};
    protected boolean[] _cuisineSelection = new boolean[_cuisine.length];
    protected CharSequence[] _allergies = {"Allergies"};
    protected boolean[] _allergySelection = new boolean[_allergies.length];
    protected CharSequence[] _cupboard = {"Cutting Board", "Colander", "Grater", "Ladle", "Measuring cup", "Measuring spoon","Mortar and pestle","Peeler","Tong","Wooden spoon","Zester","Knife"};
    protected boolean[] _cupboardSelection = new boolean[_cupboard.length];
    protected CharSequence[] _useFridge = {"UseFridge"};
    protected boolean[] _fridgeSelection = new boolean[_useFridge.length];

    protected Button advanceSearchButton;

    protected Button _seasonalButton;
    protected Button _cuisineButton;
    protected Button _allergiesButton;
    protected Button _cupboardButton;
    protected Button _useFridgeButton;
    EditText et;
    private String cuisine_selection;
    private String seasonal_selection;
    private String allergySelection;
    private String cupboardSelection;
    private String fridgeSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        et = (EditText)findViewById(R.id.EditText01);




        et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        //do something
                        //true because you handle the event
                        return true;
                    }
                return false;
            }
        });


        advanceSearchButton = (Button) findViewById(R.id.advancedbutton);

        _cuisineButton = (Button) findViewById(R.id.cuisinebutton);
        _cuisineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
        _cuisineButton.setVisibility(View.INVISIBLE);

        _seasonalButton = (Button) findViewById(R.id.seasonalbutton);
        _seasonalButton.setVisibility(View.INVISIBLE);
        _seasonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });

        _allergiesButton = (Button) findViewById(R.id.allergiesbutton);
        _allergiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
            }
        });
        _allergiesButton.setVisibility(View.INVISIBLE);

        _cupboardButton = (Button) findViewById(R.id.cupboardbutton);
        _cupboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(3);
            }
        });
        _cupboardButton.setVisibility(View.INVISIBLE);


        _useFridgeButton = (Button) findViewById(R.id.fridgebutton);
        _useFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(4);
            }
        });
        _useFridgeButton.setVisibility(View.INVISIBLE);




        advanceSearchButton.setVisibility(View.VISIBLE);
        advanceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _cuisineButton.setVisibility(View.VISIBLE);
                _seasonalButton.setVisibility(View.VISIBLE);
                _allergiesButton.setVisibility(View.VISIBLE);
                _cupboardButton.setVisibility(View.VISIBLE);
                _useFridgeButton.setVisibility(View.VISIBLE);
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

        //System.out.println("THE ID IS: " + id);
        if (id == 4){
            return new AlertDialog.Builder(this).setTitle("Use Fridge")
                    .setMultiChoiceItems(_useFridge, _fridgeSelection, new DialogSelectionClickHandler("fridge", _useFridge))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        /*
        else if (id == 0) {
            return new AlertDialog.Builder(this).setTitle("Cuisine")
                    .setMultiChoiceItems(_cuisine, _selections, new DialogSelectionClickHandler())
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        */
        else if (id == 3){
            return new AlertDialog.Builder(this).setTitle("Cupboard")
                    .setMultiChoiceItems(_cupboard, _cupboardSelection, new DialogSelectionClickHandler("cupboard", _cupboard))
                    .setPositiveButton("OK", new DialogButtonClickHandler())

                    .create();

        }
        else if
                /*(id == 0) {
            return new AlertDialog.Builder(this).setTitle("Cuisine")
                    .setMultiChoiceItems(_cuisine, _selections, new DialogSelectionClickHandler())
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
*/
            //if
                (id == 2){
            return new AlertDialog.Builder(this).setTitle("Allergies")
                    .setMultiChoiceItems(_allergies, _allergySelection, new DialogSelectionClickHandler("allergies",_allergies))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        else if
                /*(id == 0) {
            return new AlertDialog.Builder(this).setTitle("Cuisine")
                    .setMultiChoiceItems(_cuisine, _selections, new DialogSelectionClickHandler())
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        if
         */
                (id == 1) {
            return new AlertDialog.Builder(this).setTitle("Seasonal")
                    .setMultiChoiceItems(_seasonal, _seasonSelection, new DialogSelectionClickHandler("seasonal",_seasonal))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();
        } else if (id == 0) {
            return new AlertDialog.Builder(this).setTitle("Cuisine")
                    .setMultiChoiceItems(_cuisine, _cuisineSelection, new DialogSelectionClickHandler("cuisine", _cuisine))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        return null;

    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
        CharSequence[] current;
        String id;

        public DialogSelectionClickHandler(String _id, CharSequence[] temp){
            current = temp;
            _id = id;
        }
        public void onClick(DialogInterface dialog, int clicked, boolean selected) {
            Log.i("ME", current[clicked] + " selected: " + selected);

            if(id.equalsIgnoreCase("cuisine")){
                cuisine_selection = (String)current[clicked];
            }else if(id.equalsIgnoreCase("seasonal")){
                seasonal_selection = (String) current[clicked];
            }else if (id.equalsIgnoreCase("allergy")){
                allergySelection = (String) current[clicked];
            }else if (id.equalsIgnoreCase("useFridge")){
                fridgeSelection = (String) current[clicked];
            }else if (id.equalsIgnoreCase("cupboard")) {
                cupboardSelection = (String) current[clicked];
            }

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
                Log.i("ME", _cuisine[i] + " selected: " + _cuisineSelection[i]);
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
        bundle.putString("cuisine", cuisine_selection.toString());
        bundle.putString("seasonal",seasonal_selection.toString());/*use seasonal*/
        bundle.putString("allergies",allergySelection.toString()); /*allergies here**/
        bundle.putString("useFridge",fridgeSelection.toString());/*Use Fridge */
        bundle.putString("cupboard",cupboardSelection.toString());/*Use cupborad */

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
