package connectors.evernote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;

import t4.csc413.smartchef.R;

public class EvernoteActivity extends AppCompatActivity {

    String ingredientList = "";
    TextView ingredientView;

    private static final String CONSUMER_KEY = "jurispuchin";
    private static final String CONSUMER_SECRET = "429acb859dbe6ecb";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evernote);

        final EditText editText = (EditText) findViewById(R.id.add_ingredient_field);
        ingredientView = (TextView) findViewById(R.id.evernote_list_of_ingredients);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (ingredientList.equals(""))
                    ingredientList = editText.getText().toString();
                else
                    ingredientList = ingredientList + "\n" + editText.getText().toString();

                ingredientView.setText(ingredientList);
                return true;
            }
        });
    }
}
