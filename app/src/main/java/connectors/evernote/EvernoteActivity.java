package connectors.evernote;

import android.content.Intent;
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

public class EvernoteActivity extends AppCompatActivity {

    String ingredientList = "";
    TextView ingredientView;
    EvernoteSession mEvernoteSession;
    Note note;
    private Intent mCachedIntent;

    private static final String CONSUMER_KEY = "jurispuchin";
    private static final String CONSUMER_SECRET = "429acb859dbe6ecb";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    public void uploadButtonClick(View view) {

        note = new Note();
        note.setGuid("smartchef");
        note.setTitle("SmartChef Shopping List");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evernote);

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
}
