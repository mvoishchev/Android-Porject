package connectors.evernote;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.Log;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.google.android.gms.maps.SupportMapFragment;

import t4.csc413.smartchef.R;

/**
 * Created by Juris Puchin on 11/3/15.
 * <p/>
 * This class manages the Evernote calls for SmartChef.
 * <p/>
 * It is a singleton that stores the user settings as well as the important key info.
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
    public Activity currentParent;

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
     * @param listName    Name of the new shopping list.
     * @param listContent String of ingredients.
     * @param parent      Activity which called the method (so that android could get back to it)
     */
    public void createNewShoppingList(String listName, String listContent, Activity parent) {

        currentParent = parent;

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            DialogFragment dialog = new LoginDialog();
            FragmentManager manager = currentParent.getFragmentManager();
            dialog.show(manager, "LoginDialog");
            return;
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
                DialogFragment dialog = new EvernoteSucess();
                FragmentManager manager = currentParent.getFragmentManager();
                dialog.show(manager, "Sucess");

            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

    public void overwriteMainShoppingList(String listContent, Activity parent) {

        currentParent = parent;

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            DialogFragment dialog = new LoginDialog();
            FragmentManager manager = currentParent.getFragmentManager();
            dialog.show(manager, "LoginDialog");
            return;
        }
        
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

                DialogFragment dialog = new EvernoteSucess();
                FragmentManager manager = currentParent.getFragmentManager();
                dialog.show(manager, "Sucess");

            }

            @Override
            public void onException(Exception exception) {
                Log.e("Update Log", "Error updating note", exception);
            }
        });
    }

    /**
     * Use this function to get the current contents of Evernote shopping list.
     * <p/>
     * WARNING: will return null if not logged in, or shopping list does not exist
     *
     * @param parent needed to know where to go back to after login
     * @return the contents of the shopping list as a string
     */
    public String getMainShoppingList(Activity parent) {

        currentParent = parent;

        //Login and setup
        mainListGuid = evernoteSettings.getString("mainListGuid", null);
        previousNoteExists = evernoteSettings.getBoolean("listExists", false);
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        if (!EvernoteSession.getInstance().isLoggedIn()) {
            DialogFragment dialog = new LoginDialog();
            FragmentManager manager = currentParent.getFragmentManager();
            dialog.show(manager, "LoginDialog");
            return null;
        }

        if (previousNoteExists) {
            try {
                String rawNote = noteStoreClient.getNoteContent(mainListGuid);
                return Html.fromHtml(rawNote).toString();
            } catch (EDAMUserException e) {
                e.printStackTrace();
            } catch (EDAMSystemException e) {
                e.printStackTrace();
            } catch (EDAMNotFoundException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
