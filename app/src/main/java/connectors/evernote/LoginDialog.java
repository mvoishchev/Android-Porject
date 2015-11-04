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
 *
 * This dialog tells the user they need to login.
 */
public class LoginDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You need to Login to Evernote first!")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EvernoteSession.getInstance().authenticate(getActivity());
                        Button loginButton = (Button) getActivity().findViewById(R.id.loginLogoutButton);
                        loginButton.setText("Logout");
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
