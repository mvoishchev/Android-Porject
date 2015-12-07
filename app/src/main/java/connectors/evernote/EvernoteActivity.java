package connectors.evernote;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;

import database.shoppinglist.ShoppingListLayout;
import t4.csc413.smartchef.NavBaseActivity;
import t4.csc413.smartchef.R;

import android.text.Html;

/**
 * This class describes the Evernote activity used to setup Evernote settings.
 */
public class EvernoteActivity extends NavBaseActivity {

    //NavBar variables
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    //Setup class variables
    String ingredientList = "";
    TextView ingredientView;
    EvernoteSession mEvernoteSession;
    Note note;
    String mainListGuid;
    boolean previousNoteExists;
    private Intent mCachedIntent;

    //Setup evernote variables
    private static final String CONSUMER_KEY = "jurispuchin";
    private static final String CONSUMER_SECRET = "429acb859dbe6ecb";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    public static final String EVERNOTE_PREF = "EvernotePreferencesFile";
    private static final String MAIN_LIST_NAME = "SmartChef Main Shopping List";

    private static final String PL_LOGIN = "(Please Login to Evernote)";
    private static final String LST_EMPTY = "(Shoping List is Empty)";

    /**
     * This function is clalled when the clar button is pressede.
     *
     * The whopping list is deleted.
     * @param view  Passed from the button click.
     */
    public void clearButtonClick(View view) {

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            DialogFragment dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "LoginDialog");
            return;
        }

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        if (previousNoteExists) {
            try {
                noteStoreClient.deleteNote(mainListGuid);
                mainListGuid = null;
                previousNoteExists = false;

                // We need an Editor object to make preference changes.
                SharedPreferences settings = getSharedPreferences(EVERNOTE_PREF, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("mainListGuid", mainListGuid);
                editor.putBoolean("listExists", previousNoteExists);

                // Commit the edits!
                editor.commit();

                //Update user's view
                ingredientList = LST_EMPTY;
                ingredientView.setText(ingredientList);

                //Toast your sucess
                Context context = getApplicationContext();
                CharSequence text = "Shopping List Cleared";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Shopping List is already Empty!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    /**
     * This function runs when the update button is clicked.
     *
     * It updates the main shopping list.
     * @param view Passed from click.
     */
    public void updateButtonClick(View view) {

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            DialogFragment dialog = new LoginDialog();
            dialog.show(getFragmentManager(), "LoginDialog");
            return;
        }

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        if (previousNoteExists) {
            try {
                noteStoreClient.deleteNote(mainListGuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        note = new Note();
        note.setTitle(MAIN_LIST_NAME);
        final String newSring = ShoppingListLayout.GetShoppingList().toString();
        note.setContent(EvernoteUtil.NOTE_PREFIX + newSring + EvernoteUtil.NOTE_SUFFIX);

        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                note = result;
                mainListGuid = note.getGuid();
                previousNoteExists = true;

                //Update user's view
                ingredientList = newSring;
                ingredientView.setText(ingredientList);

                Context context = getApplicationContext();
                CharSequence text = "Evernote Updated";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);

                Context context = getApplicationContext();
                CharSequence text = "Please Login to Evernote!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    public void loginButtonClick(View view) {
        Button thisButton = (Button) findViewById(R.id.loginLogoutButton);
        if (thisButton.getText().equals("Login")) {
            EvernoteSession.getInstance().authenticate(this);
            thisButton.setText("Logout");
        } else if (thisButton.getText().equals("Logout")) {
            EvernoteSession.getInstance().logOut();
            thisButton.setText("Login");
            ingredientList = PL_LOGIN;

        }
        ingredientView.setText(ingredientList);
    }

    /**
     * Sets up the main activity based on previous saved bundle state.
     * @param savedInstanceState passed from previous activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evernote);

        SharedPreferences settings = getSharedPreferences(EVERNOTE_PREF, 0);
        mainListGuid = settings.getString("mainListGuid", null);
        previousNoteExists = settings.getBoolean("listExists", false);

        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(false)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();

        ingredientView = (TextView) findViewById(R.id.evernote_list_of_ingredients);

//        This text box allows for manually adding ingredients to Evernote. It was removed to simplify the UI in the final version.
//
//        final EditText editText = (EditText) findViewById(R.id.add_ingredient_field);
//
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (ingredientList.equals(LST_EMPTY))
//                    ingredientList = editText.getText().toString();
//                else if (ingredientList.equals(PL_LOGIN)){}
//                    //Do nothing
//                else
//                    ingredientList = editText.getText().toString() + "\n" + ingredientList;
//
//                ingredientView.setText(ingredientList);
//                editText.setText("");
//                return true;
//            }
//        });

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        SharedPreferences settings = getSharedPreferences(EVERNOTE_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("mainListGuid", mainListGuid);
        editor.putBoolean("listExists", previousNoteExists);

        // Commit the edits!
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button loginButton = (Button) findViewById(R.id.loginLogoutButton);

        SystemClock.sleep(300);

        if (EvernoteSession.getInstance().isLoggedIn()) {
            // Check if logged in
            loginButton.setText("Logout");
            if (previousNoteExists) {
                final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
                try {
                    String rawNote = noteStoreClient.getNoteContent(mainListGuid);
                    ingredientList = Html.fromHtml(rawNote).toString();
                } catch (EDAMUserException e) {
                    e.printStackTrace();
                } catch (EDAMSystemException e) {
                    e.printStackTrace();
                } catch (EDAMNotFoundException e) {
                    e.printStackTrace();
                } catch (TException e) {
                    e.printStackTrace();
                }
            } else {
                ingredientList = LST_EMPTY;
            }

        } else {
            //If not logged in
            loginButton.setText("Login");
            ingredientList = PL_LOGIN;
        }

        ingredientView.setText(ingredientList);
    }
}
