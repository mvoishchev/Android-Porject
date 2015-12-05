package t4.csc413.smartchef;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static t4.csc413.smartchef.R.id.GMButton;
import static t4.csc413.smartchef.R.id.Go_Button;
import static t4.csc413.smartchef.R.id.HourText;
import static t4.csc413.smartchef.R.id.MinutesText;
import static t4.csc413.smartchef.R.id.SecondsText;
import static t4.csc413.smartchef.R.id.StartB;
import static t4.csc413.smartchef.R.id.StopB;
import static t4.csc413.smartchef.R.id.textTimer;
/**
 *
 * Fragment to display information for preparation time
 * Created by Thomas X Mei
 */

public class FragC extends android.support.v4.app.Fragment
{
    static TextView v;
    View view;
    Button start, stop;
    TextView textViewTime,test_view;
    EditText editHours, editMinutes , editeSeconds;
    String hours_String, minutes_String, seconds_String;
    int input_hours, input_minutes,input_seconds;
    int prep_Hours, prep_Minutes;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_frag_c,container,false);

        SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFC);

        prep_Hours = m.rr.getPrepTime_hours();
        prep_Minutes = m.rr.getPrepTime_minutes();

        String text = "This recipe will take a total of " + prep_Hours + " hours and " + prep_Minutes
                + " minutes to prepare.\nPress GO!, or input an alternate time increment.";
        v.setText(text);

        start = (Button) view.findViewById(StartB);
        stop = (Button) view.findViewById(StopB);
        textViewTime = (TextView) view.findViewById(textTimer);
        editHours = (EditText) view.findViewById(HourText);
        editMinutes = (EditText) view.findViewById(MinutesText);
        editeSeconds = (EditText) view.findViewById(SecondsText);


        Button go_button = (Button) view.findViewById(Go_Button);
        go_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hours_String = editHours.getText().toString();
                if (TextUtils.isEmpty(hours_String))
                {
                    input_hours = prep_Hours;
                } else {
                    input_hours = new Integer(Integer.parseInt(hours_String));
                }
               
                if (TextUtils.isEmpty(minutes_String))
                {
                    input_minutes = prep_Minutes;
                } else {
                    input_minutes = new Integer(Integer.parseInt(minutes_String));
                    if (input_minutes > 60)
                    {
                        input_minutes = 60;
                    }
                }

                if (TextUtils.isEmpty(seconds_String))
                {
                    input_seconds = 0;
                } else {
                    input_seconds = new Integer(Integer.parseInt(seconds_String));
                    if (input_seconds > 60) {
                        input_seconds = 60;
                    }
                }


                textViewTime.setText(input_hours + ":" + input_minutes + ":" + input_seconds);

                int hour = input_hours * 3600000;
                int minute = input_minutes * 60000;
                int seconds = input_seconds * 1000;


                int total = minute + hour + seconds;

                final CounterClass timer = new CounterClass(total, 1000);
                start.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        timer.start();
                    }
                });

                stop.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        textViewTime.setText("00:00:00");
                    }
                });
            }
        });
        return  view;
    }

public class CounterClass extends CountDownTimer
{

    public CounterClass(long millisInFuture, long countDownInterval)
    {
        super(millisInFuture, countDownInterval);
     }


    @Override
    public void onTick(long millisUntilFinished)
    {

        long millis = millisUntilFinished;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        System.out.println(hms);
        textViewTime.setText(hms);
    }

    @Override
    public void onFinish()
    {
        textViewTime.setText("Completed.");
    }
    }
}
