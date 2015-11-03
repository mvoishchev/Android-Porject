package connectors.evernote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;

/**
 * Created by Juris Puchin on 11/3/15.
 *
 * This class manages the Evernote calls for SmartChef.
 *
 * It is a singleton that stores the user settings as well as the important key info.
 *
 */
public class EvernoteManager {

    //Setup evernote variables
    private static final String CONSUMER_KEY = "jurispuchin";
    private static final String CONSUMER_SECRET = "429acb859dbe6ecb";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    public static final String EVERNOTE_PREF = "EvernotePreferencesFile";
    private static final String MAIN_LIST_NAME = "SmartChef Main Shopping List";

    //Setup manager variables
    EvernoteSession mEvernoteSession;
    String mainListGuid;
    boolean previousNoteExists;
    private static EvernoteManager instance;
    private SharedPreferences evernoteSettings;

    Note note;

    private EvernoteManager(Context thisContext) {
        evernoteSettings = thisContext.getSharedPreferences(EVERNOTE_PREF, 0);
        mainListGuid = evernoteSettings.getString("mainListGuid", null);
        previousNoteExists = evernoteSettings.getBoolean("listExists", false);

        mEvernoteSession = new EvernoteSession.Builder(thisContext)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(false)
                .build(CONSUMER_KEY, CONSUMER_SECRET)
                .asSingleton();
    }

    /**
     * Use this to get an instance of EvernoteManager for your use.
     *
     * @param yourContext Put application context here. Necessary to get global settings.
     * @return
     */
    public static EvernoteManager getInstance(Context yourContext) {
        if (instance == null) {
            instance = new EvernoteManager(yourContext);
        }
        return instance;
    }

    /**
     * Create a new note in the User's Evernote account.
     *
     * @param listName Name of the new shopping list.
     * @param listContent String of ingredients.
     * @param parent Activity which called the method (so that android could get back to it)
     */
    public void createNewShoppingList(String listName, String listContent, Activity parent) {

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // Check if logged in
            //mCachedIntent = this.getIntent(); not sure why this was here
            LoginActivity.launch(parent);
        }

        //Make the new note
        Note newNote = new Note();
        newNote.setTitle(listName);
        newNote.setContent(EvernoteUtil.NOTE_PREFIX + listContent + EvernoteUtil.NOTE_SUFFIX);

        //Upload the note
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        noteStoreClient.createNoteAsync(newNote, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
//                ingredientView.setText("Done!");
//                ingredientList = "";
            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

    public void updateMainShoppingList(String listContent, Activity parent) {

        //Login and setup
        mainListGuid = evernoteSettings.getString("mainListGuid", null);
        previousNoteExists = evernoteSettings.getBoolean("listExists", false);
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        if (previousNoteExists) {
            try {
                noteStoreClient.deleteNote(mainListGuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            // Check if logged in
            // mCachedIntent = this.getIntent();
            LoginActivity.launch(parent);
        }

        //Make the new note
        final Note updatedNote = new Note();
        updatedNote.setTitle(MAIN_LIST_NAME);
        updatedNote.setContent(EvernoteUtil.NOTE_PREFIX + listContent + EvernoteUtil.NOTE_SUFFIX);

        //Upload the note
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        noteStoreClient.createNoteAsync(updatedNote, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                mainListGuid = result.getGuid();
                previousNoteExists = true;

                // We need an Editor object to make preference changes.
                SharedPreferences.Editor editor = evernoteSettings.edit();
                editor.putString("mainListGuid", mainListGuid);
                editor.putBoolean("listExists", previousNoteExists);

                // Commit the edits!
                editor.commit();

            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

}
