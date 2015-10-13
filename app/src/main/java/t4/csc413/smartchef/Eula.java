package t4.csc413.smartchef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;



public class Eula {
    private static final String ASSET_EULA = "Eula";
    private static final String PREFERENCE_EULA_ACCEPTED = "Eula.accept";
    private static final String PREFERENCES_EULA = "Eula";

    /*public Eula(Context activity)
    {
     myActivity = activity;
    }
    */
    static interface OnEulaAgreedTo {
        void onEulaAgreedTo();
    }
    static boolean show(final Activity activity) {
        final SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_EULA,
                Activity.MODE_PRIVATE);
        if (!preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.eula_title);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.eula_accept, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    accept(preferences);
                    if (activity instanceof OnEulaAgreedTo) {
                        ((OnEulaAgreedTo) activity).onEulaAgreedTo();
                    }
                }
            });
            builder.setNegativeButton(R.string.eula_refuse, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    refuse(activity);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    refuse(activity);
                }
            });
            builder.setMessage(readEula(activity));
            builder.create().show();
            return false;
        }
        return true;
    }
    private static void accept(SharedPreferences preferences) {
        preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
    }
    private static void refuse(Activity activity) {
        activity.finish();
    }
    private static CharSequence readEula(Activity activity) {
        BufferedReader in = null;
        try {
            System.out.println("Got here");
            in = new BufferedReader(new InputStreamReader(activity.getAssets().open(ASSET_EULA)));
            System.out.println("Do we get here?");
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = in.readLine()) != null) buffer.append(line).append('\n');
            return buffer;
        } catch (IOException e) {
            return "";
        } finally {
            closeStream(in);
        }
    }
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
// Ignore
            }
        }
    }


}
