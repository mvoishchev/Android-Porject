package connectors.evernote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import connectors.evernote.LoginActivity;
import connectors.evernote.LoginChecker;
import connectors.evernote.EvernoteActivity;
import t4.csc413.smartchef.R;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;

import static t4.csc413.smartchef.R.*;

/**
 * @author rwondratschek
 */
public class LoginActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(color.tb_text));

        setSupportActionBar(toolbar);

        mButton = (Button) findViewById(id.button_login);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EvernoteSession.getInstance().authenticate(LoginActivity.this);
                mButton.setEnabled(false);
            }
        });

    }

    @Override
    public void onLoginFinished(boolean successful) {
        if (successful) {
            finish();
        } else {
            mButton.setEnabled(true);
        }
    }
}
