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

import connectors.SearchTools;
import connectors.evernote.LoginActivity;
import android.widget.Button;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.text.TextUtils;

public class MainActivity extends NavBaseActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    protected CharSequence[] _cuisine = {"American", "Indian", "Italian", "Chinese", "Thai", "French", "Spanish", "Philipino","Korean",};
    protected boolean[] _selections = new boolean[_cuisine.length];
    protected CharSequence[] _allergies = {"Pecan-free", "Gluten-free", "Seafood", "Lactose-free", };
    protected boolean[] _allergySelection = new boolean[_allergies.length];
    protected CharSequence[] _cupboard = {"Cutting Board", "Colander", "Grater", "Ladle", "Measuring cup", "Measuring spoon","Mortar and pestle","Peeler","Tong","Wooden spoon","Zester","Knife"};
    protected boolean[] _cupboardSelection = new boolean[_cupboard.length];
    protected CharSequence[] _useFridge = {"Frozen mix-Veggies", "Brocolli", "Cabbage", "Beans", "Tomato", "Milk", "ButterNut","Pumpkin", "Green-Chillies", "Jalapeno", "Butter", "Cheese", "Eggplant","Egg"};
    protected boolean[] _fridgeSelection = new boolean[_useFridge.length];

    protected  Button advanceSearchButton;

    protected Button _cuisineButton;
    protected Button _allergiesButton;
    protected Button _cupboardButton;
    protected Button _useFridgeButton;
    EditText et;
    private String selections;
    private String selection;
    private String allergySelection = "";
    private String cupboardSelection;
    private String fridgeSelection;

    private boolean visible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        et = (EditText)findViewById(R.id.EditText01);

        et.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                        String emptyString = et.getText().toString();
                        if(TextUtils.isEmpty(emptyString)) {
                            et.setError("Please enter an ingredient!");
                        }else {
                            searchByIngredient();
                        }

                        //do something
                        //true because you handle the event
                        return true;
                    }
                return false;
            }
        });


        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //Do something
                    
                    searchByIngredient();
                }
                return false;
            }
        });


        _allergies = SearchTools.getSupportedAllergies();
        _cuisine = SearchTools.getSupportedCuisines();

        _selections = new boolean[_cuisine.length];
        _allergySelection = new boolean[_allergies.length];

        advanceSearchButton = (Button) findViewById(R.id.advancedbutton);

        _cuisineButton = (Button) findViewById(R.id.cuisinebutton);
        _cuisineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
        _cuisineButton.setVisibility(View.INVISIBLE);


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
                _allergiesButton.setVisibility(View.VISIBLE);
                _cupboardButton.setVisibility(View.VISIBLE);
                _useFridgeButton.setVisibility(View.VISIBLE);
                Toggle();
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

    private void Toggle(){
        if(visible == true){
            _cuisineButton.setVisibility(View.GONE);
            _allergiesButton.setVisibility(View.GONE);
            _cupboardButton.setVisibility(View.GONE);
            _useFridgeButton.setVisibility(View.GONE);
            visible = false;

        }
        else{
            _cuisineButton.setVisibility(View.VISIBLE);
            _allergiesButton.setVisibility(View.VISIBLE);
            _cupboardButton.setVisibility(View.VISIBLE);
            _useFridgeButton.setVisibility(View.VISIBLE);
            visible = true;
        }
    }


    @Override
    public Dialog onCreateDialog(int id) {
        Log.i("output", "MainActivity");

        AlertDialog.Builder menu = new AlertDialog.Builder(this);

        //System.out.println("THE ID IS: " + id);
        if (id == 4){
            return new AlertDialog.Builder(this).setTitle("Ingredient on Hand")
                    .setMultiChoiceItems(_useFridge, _fridgeSelection, new DialogSelectionClickHandler("fridge",_useFridge))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        else if (id == 3){
            return new AlertDialog.Builder(this).setTitle("Cupboard")
                    .setMultiChoiceItems(_cupboard, _cupboardSelection, new DialogSelectionClickHandler("cupboard",_cupboard))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        else if(id == 2){
            return new AlertDialog.Builder(this).setTitle("My Food Allergies")
                    .setMultiChoiceItems(_allergies, _allergySelection, new DialogSelectionClickHandler("allergies",_allergies))
                    .setPositiveButton("OK", new DialogButtonClickHandler())
                    .create();

        }
        else if (id == 0) {
            return new AlertDialog.Builder(this).setTitle("Enter My Favorite Cuisine")
                    .setMultiChoiceItems(_cuisine, _selections, new DialogSelectionClickHandler("cuisine", _cuisine))
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
            id = _id;
        }
        public void onClick(DialogInterface dialog, int clicked, boolean selected) {

            if(selected) {
                if (id.equalsIgnoreCase("cuisine")) {
                    selection = (String) current[clicked];
                } else if (id.equalsIgnoreCase("seasonal")) {
                    selections = (String) current[clicked];
                } else if (id.equalsIgnoreCase("allergies")) {
                    allergySelection = allergySelection.concat((String) current[clicked]).concat(",");
                }
            }else{
                //if unclicked, remove them from being passed
                if(selection != null) {
                    if (selection.equalsIgnoreCase((String) current[clicked]))
                        selection = null;
                }
                if(selections != null) {
                    if (selections.equalsIgnoreCase((String) current[clicked]))
                        selections = null;
                }
                if(allergySelection != null) {
                    if (allergySelection.contains((String) current[clicked]))
                        allergySelection = allergySelection.replace(((String) current[clicked]).concat(","), "");
                }
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

    public void searchByIngredient()
    {
        Bundle bundle = new Bundle();


        bundle.putString("search", et.getText().toString());
        if(selection != null)
            bundle.putString("cuisine", selection.toString());
        if(selections != null)
            bundle.putString("seasonal",selections.toString());/*use seasonal*/
        if(allergySelection != null)
            bundle.putString("allergies",allergySelection.toString()); /*allergies here**/
        if(fridgeSelection != null)
            bundle.putString("useFridge",fridgeSelection.toString());/*Use Fridge */
        if(cupboardSelection != null)
            bundle.putString("cupboard",cupboardSelection.toString());/*Use cupborad */


            Intent i = new Intent(this, LoadingActivity.class);
        i.putExtras(bundle);
        startActivity(i);

    }

    public void search(View v)


    {
        searchByIngredient();

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


        return super.onOptionsItemSelected(item);
    }
}
