package t4.csc413.smartchef;

/**
 * Created by poulomirajarshi on 10/12/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

 public class EnGps {

     public static void displayPromptForEnablingGPS(final Activity activity)
    {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);

        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

        final String message = "Do you want to enable either GPS or other location?"
                + " Click OK to go to location services settings";

        //Go to service location setting, if user clicks OK
        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {

                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

}

