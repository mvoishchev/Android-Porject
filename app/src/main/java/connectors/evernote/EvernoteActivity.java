package connectors.evernote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import t4.csc413.smartchef.R;

public class EvernoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evernote);
    }

    EditText editText = (EditText) findViewById(R.id.add_ingredient_field);

    editText.setOnEditorActionListener(new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_SEND) {
                handled = true;
            }
            return true;
        }
    });

}
