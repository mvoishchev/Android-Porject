package t4.csc413.smartchef;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static t4.csc413.smartchef.R.id.StartB;
import static t4.csc413.smartchef.R.id.StopB;
import static t4.csc413.smartchef.R.id.textTimer;

public class FragC extends android.support.v4.app.Fragment {
    static TextView v;
    View view;
    Button start, stop;
    TextView textViewTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_frag_c,container,false);

        SlideMain m = (SlideMain)getActivity(); //grabs info from parent activity
        v = (TextView) view.findViewById(R.id.TextFC);

        int prep_Hours = m.rr.getPrepTime_hours();
        int prep_Minutes = m.rr.getPrepTime_minutes();

        String text = "Preperation Time:\n" +
                "Hours: " + prep_Hours +
                "\t\t\tMinutes: " + prep_Minutes;
        v.setText(text);







        start = (Button)view.findViewById(StartB);
        stop = (Button)view.findViewById(StopB);
        textViewTime = (TextView)view.findViewById(textTimer);

        textViewTime.setText("Click Start!");
        int minute = prep_Minutes*60000;
        int hour = prep_Hours*3600000;
        int total = minute + hour;

        final CounterClass timer = new CounterClass(total, 1000);
        start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                timer.start();


            }
        });

        stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                timer.cancel();
            }
        });
        return  view;
    }


public class CounterClass extends CountDownTimer {

    public CounterClass(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        // TODO Auto-generated constructor stub
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub

        long millis = millisUntilFinished;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        System.out.println(hms);
        textViewTime.setText(hms);
    }

    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
        textViewTime.setText("Completed.");
    }


}
}
