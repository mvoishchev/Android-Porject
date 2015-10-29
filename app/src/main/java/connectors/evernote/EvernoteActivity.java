package connectors.evernote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

import t4.csc413.smartchef.R;

/**
 * This class describes the Evernote activity used to setup Evernote settings.
 */
public class EvernoteActivity extends AppCompatActivity {

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

    public void createNewShoppingList(String listName, String listContent) {

        //Login and setup
        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(false)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // Check if logged in
            mCachedIntent = this.getIntent();
            LoginActivity.launch(this);
        }

        //Make the new note
        Note newList = new Note();
        newList.setTitle(listName);
        newList.setContent(listContent);

        //Upload the note
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                ingredientView.setText("Done!");
                ingredientList = "";
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

    public void updateMainShoppingList(String listContent) {

        //Login and setup
        SharedPreferences settings = getSharedPreferences(EVERNOTE_PREF, 0);
        mainListGuid = settings.getString("mainListGuid", null);
        previousNoteExists = settings.getBoolean("listExists", false);

        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(false)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // Check if logged in
            mCachedIntent = this.getIntent();
            LoginActivity.launch(this);
        }

        //Make the new note
        final Note newList = new Note();
        newList.setTitle(MAIN_LIST_NAME);
        newList.setContent(listContent);

        //Upload the note
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                mainListGuid = result.getGuid();
                previousNoteExists = true;

                // We need an Editor object to make preference changes.
                SharedPreferences settings = getSharedPreferences(EVERNOTE_PREF, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("mainListGuid", mainListGuid);
                editor.putBoolean("listExists", previousNoteExists);

                // Commit the edits!
                editor.commit();

                ingredientView.setText("Done!");
                ingredientList = "";
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

    /**
     * This function is clalled when the upload button is clicked.
     *
     * It uploads a new shopping list.
     * @param view  Passed from the button click.
     */
    public void uploadButtonClick(View view) {

        note = new Note();
        note.setTitle("Temp Shopping List");
        note.setContent(EvernoteUtil.NOTE_PREFIX + ingredientList + EvernoteUtil.NOTE_SUFFIX);

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

//        noteStoreClient.getNoteAsync("smartchef", true, true, true, true, new EvernoteCallback<Note>() {
//            @Override
//            public void onSuccess(Note result) {
//
//            }
//
//            @Override
//            public void onException(Exception exception) {
//
//                noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
//                    @Override
//                    public void onSuccess(Note result) {
//                    }
//
//                    @Override
//                    public void onException(Exception exception) {
//                        Log.e("Creation Flog", "Error creating note", exception);
//                    }
//                });
//            }
//        });

        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                ingredientView.setText("Done!");
                ingredientList = "";
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

    /**
     * This function runs when the update button is clicked.
     *
     * It updates the main shopping list.
     * @param view Passed from click.
     */
    public void updateButtonClick(View view) {

        if (!EvernoteSession.getInstance().isLoggedIn()) {
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
        note.setContent(EvernoteUtil.NOTE_PREFIX + ingredientList + EvernoteUtil.NOTE_SUFFIX);

        noteStoreClient.createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                note = result;
                mainListGuid = note.getGuid();
                previousNoteExists = true;

                ingredientView.setText("Done!");
                ingredientList = "";
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });

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

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // Check if logged in
            mCachedIntent = this.getIntent();
            LoginActivity.launch(this);
        }

        final EditText editText = (EditText) findViewById(R.id.add_ingredient_field);
        ingredientView = (TextView) findViewById(R.id.evernote_list_of_ingredients);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (ingredientList.equals(""))
                    ingredientList = editText.getText().toString();
                else
                    ingredientList = ingredientList + "\n" + editText.getText().toString();

                ingredientView.setText(ingredientList);
                editText.setText("");
                return true;
            }
        });
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
}
