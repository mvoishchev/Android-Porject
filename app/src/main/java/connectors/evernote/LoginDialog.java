package connectors.evernote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import com.evernote.client.android.EvernoteSession;

import t4.csc413.smartchef.R;

/**
 * Created by yura on 11/4/15.
 * <p/>
 * This dialog tells the user they need to login.
 */
public class LoginDialog extends DialogFragment {

    //Setup evernote variables
    private static final String CONSUMER_KEY = "jurispuchin";
    private static final String CONSUMER_SECRET = "429acb859dbe6ecb";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You need to Login to Evernote first! Login and try again.")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (EvernoteSession.getInstance() == null) {
                            new EvernoteSession.Builder(getActivity())
                                    .setEvernoteService(EVERNOTE_SERVICE)
                                    .setSupportAppLinkedNotebooks(false)
                                    .build(CONSUMER_KEY, CONSUMER_SECRET)
                                    .asSingleton();
                        }

                        EvernoteSession.getInstance().authenticate(getActivity());

                        Button thisButton = (Button) getActivity().findViewById(R.id.loginLogoutButton);

                        if (thisButton != null) {
                            thisButton.setText("Logout");
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
