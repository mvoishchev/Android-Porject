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
import static t4.csc413.smartchef.R.id.HourText;
import static t4.csc413.smartchef.R.id.MinutesText;
import static t4.csc413.smartchef.R.id.Pause_Button;
import static t4.csc413.smartchef.R.id.Resume_Button;
import static t4.csc413.smartchef.R.id.SecondsText;
import static t4.csc413.smartchef.R.id.StartB;
import static t4.csc413.smartchef.R.id.StopB;
import static t4.csc413.smartchef.R.id.textTimer;
/**
 *
 * Fragment to display information for preparation time
 * Created by Thomas X Mei
 */

public class FragC extends android.support.v4.app.Fragment {
    static TextView v;
    View view;
    Button start, stop, resume, pause;
    TextView textViewTime, test_view;
    EditText editHours, editMinutes, editeSeconds;
    String hours_String, minutes_String, seconds_String;
    int input_hours, input_minutes, input_seconds;
    int prep_Hours, prep_Minutes;
    int total;
    //CountDownTimer timer;
    CounterClass timer;
    long millisUntilDoned;


    //Declare a variable to hold count down timer's paused status
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
    //Declare a variable to hold CountDownTimer remaining time
    private long timeRemaining = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_c, container, false);

        SlideMain m = (SlideMain) getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFC);

        prep_Hours = m.rr.getPrepTime_hours();
        prep_Minutes = m.rr.getPrepTime_minutes();

        String text = "This recipe will take a total of " + prep_Hours + " hours and " + prep_Minutes
                + " minutes to prepare.\nPress Start!, or input an alternate time increment.";
        v.setText(text);


        editHours = (EditText) view.findViewById(HourText);
        editMinutes = (EditText) view.findViewById(MinutesText);
        editeSeconds = (EditText) view.findViewById(SecondsText);



        textViewTime = (TextView) view.findViewById(textTimer);
        start = (Button) view.findViewById(StartB);
        stop = (Button) view.findViewById(StopB);
        resume = (Button) view.findViewById(Resume_Button);
        pause = (Button) view.findViewById(Pause_Button);

        pause.setEnabled(false);
        resume.setEnabled(false);
        stop.setEnabled(false);

       /*
        //Initially disabled the pause, resume and cancel button
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnCancel.setEnabled(false);
        */


        //Set a Click Listener for start button
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //get input from user and set it to variables for timer
                hours_String = editHours.getText().toString();
                if (TextUtils.isEmpty(hours_String))
                {
                    input_hours = prep_Hours;
                } else {
                    input_hours = new Integer(Integer.parseInt(hours_String));
                }
                minutes_String = editMinutes.getText().toString();
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
                seconds_String = editeSeconds.getText().toString();
                if (TextUtils.isEmpty(seconds_String))
                {
                    input_seconds = 0;
                } else {
                    input_seconds = new Integer(Integer.parseInt(seconds_String));
                    if (input_seconds > 60) {
                        input_seconds = 60;
                    }
                }

                //display the timer onto textField
                textViewTime.setText(input_hours + ":" + input_minutes + ":" + input_seconds);
                //converting from milliseconds to hours,minutes,seconds
                int hour = input_hours * 3600000;
                int minute = input_minutes * 60000;
                int seconds = input_seconds * 1000;

                //total time in milliseconds
                total = minute + hour + seconds;


                isPaused = false;
                isCanceled = false;

                //Disable the start and pause button
                start.setEnabled(false);
                resume.setEnabled(false);
                //Enabled the pause and cancel button
                pause.setEnabled(true);
                stop.setEnabled(true);


                long millisInFuture = total;
                long countDownInterval = 1000;

                timer = new CounterClass(millisInFuture, countDownInterval);
                timer.start();
/*

                //Initialize a new CountDownTimer instance
                timer = new CountDownTimer(millisInFuture, countDownInterval) {
                    public void onTick(long millisUntilFinished) {
                        //do something in every tick
                      //  if (isPaused || isCanceled) {
                            //If the user request to cancel or paused the
                            //CountDownTimer we will cancel the current instance
                            cancel();
                      //  } else {
                            //Display the remaining seconds to app interface
                            //1 second = 1000 milliseconds
                            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeRemaining),
                                    TimeUnit.MILLISECONDS.toMinutes(timeRemaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeRemaining)),
                                    TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)));
                            textViewTime.setText(hms);
                            //Put count down timer remaining time in a variable
                            timeRemaining = millisUntilFinished;
                        }
                //    }

                    public void onFinish() {
                        //Do something when count down finished
                        textViewTime.setText("Done");

                        //Enable the start button
                        start.setEnabled(true);
                        //Disable the pause, resume and cancel button
                        pause.setEnabled(false);
                        resume.setEnabled(false);
                        stop.setEnabled(false);
                    }
                }.start();
*/


            }
        });

        //Set a Click Listener for pause button
        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop timer
                timer.cancel();


                //When user request to pause the CountDownTimer
                isPaused = true;

                //Enable the resume and cancel button
                resume.setEnabled(true);
                stop.setEnabled(true);
                //Disable the start and pause button
                start.setEnabled(false);
                pause.setEnabled(false);

                long millisInFuture = timeRemaining;
                long countDownInterval = 1000;




            }
        });

        //Set a Click Listener for resume button
        resume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop timer
                //timer.cancel();



                //Disable the start and resume button
                start.setEnabled(false);
                resume.setEnabled(false);
                //Enable the pause and cancel button
                pause.setEnabled(true);
                stop.setEnabled(true);

                //Specify the current state is not paused and canceled.
                isPaused = false;
                isCanceled = false;

                //Initialize a new CountDownTimer instance
                long millisInFuture = timeRemaining;
                long countDownInterval = 1000;
                //timer = new CounterClass(10000000, 1000);

                timer.start();


/*
                //CounterClass timer;
                new CountDownTimer(millisInFuture, countDownInterval) {
                    public void onTick(long millisUntilFinished) {
                        //Do something in every tick
                        if (isPaused || isCanceled) {
                            //If user requested to pause or cancel the count down timer
                            cancel();

                        } else {
                            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeRemaining),
                                    TimeUnit.MILLISECONDS.toMinutes(timeRemaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeRemaining)),
                                    TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)));
                            textViewTime.setText(hms);
                            //Put count down timer remaining time in a variable
                            timeRemaining = millisUntilFinished;
                        }
                    }

                    public void onFinish() {
                        //Do something when count down finished
                        textViewTime.setText("Done");
                        //Disable the pause, resume and cancel button
                        pause.setEnabled(false);
                        resume.setEnabled(false);
                        stop.setEnabled(false);
                        //Enable the start button
                        start.setEnabled(true);
                    }
                }.start();
                */


                //Set a Click Listener for cancel/stop button
                stop.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When user request to cancel the CountDownTimer
                        isCanceled = true;
                        //stop timer
                        timer.cancel();



                        //Disable the cancel, pause and resume button
                        pause.setEnabled(false);
                        resume.setEnabled(false);
                        stop.setEnabled(false);
                        //Enable the start button
                        start.setEnabled(true);
                        //stop the timer
                        timer.cancel();
                        timer = null;
                        //Notify the user that CountDownTimer is canceled/stopped
                        textViewTime.setText("00:00:00.");
                    }
                });
            }
        });

        //Set a Click Listener for cancel/stop button
        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop the timer
                timer.cancel();
                timer = null;
                //When user request to cancel the CountDownTimer
                isCanceled = true;

                //Disable the cancel, pause and resume button
                pause.setEnabled(false);
                resume.setEnabled(false);
                stop.setEnabled(false);
                //Enable the start button
                start.setEnabled(true);



                //Notify the user that CountDownTimer is canceled/stopped
                textViewTime.setText("00:00:00");
            }
        });








/*
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


        */

        return view;
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
            textViewTime.setText(hms);
            millis = millisUntilDoned;
        }

        @Override
        public void onFinish()
        {
            textViewTime.setText("Completed.");
        }
    }
}




