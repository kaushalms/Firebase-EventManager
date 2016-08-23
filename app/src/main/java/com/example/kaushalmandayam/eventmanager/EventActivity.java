package com.example.kaushalmandayam.eventmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Kaushal.Mandayam on 8/11/2016.
 */
public class EventActivity extends AppCompatActivity implements View.OnClickListener {
    private Calendar mCalendar = Calendar.getInstance();
    private DateFormat mDateFormater = new SimpleDateFormat("MM/dd/yyyy");
    private int mYear = mCalendar.get(Calendar.YEAR);
    private int mMonth = mCalendar.get(Calendar.MONTH);
    private int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    private EditText startDate;
    private EditText startTime;
    private TextView locationTV;
    private ImageView crossImage;
    private Button selectFriends;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;

    private SimpleDateFormat dateFormatter;


    static final int DATE_DIALOG_ID = 0;
    static final int CUSTOM_DIALOG = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        locationTV= (TextView) findViewById(R.id.eventLocation);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("placeName");
            locationTV.setText(j);
        }
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
        setTimeField();
    }

    private void findViewsById() {
        startDate = (EditText) findViewById(R.id.eventStartDate);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();
        startTime = (EditText) findViewById(R.id.eventStartTime);
        startTime.setInputType(InputType.TYPE_NULL);
        startTime.requestFocus();
        crossImage = (ImageView) findViewById(R.id.eventActivityCross);
        selectFriends= (Button) findViewById(R.id.selectFriends);

    }

    //Select date
    private void setDateTimeField() {
        startDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void setTimeField() {
        startTime.setOnClickListener(this);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selectHour="";
                String selectMinute="";
                if (selectedHour<10)
                    selectHour= "0"+selectedHour;
                else
                    selectHour=""+selectedHour;

                if (selectedMinute<10)
                    selectMinute= "0"+selectedMinute;
                else
                    selectMinute=""+selectedMinute;
                startTime.setText(selectHour + ":" + selectMinute);
            }
        }, hour, minute, true);//Yes 24 hour time

    }

    @Override
    public void onClick(View v) {
        if (v == startDate) {
            fromDatePickerDialog.show();

        }
        else if (v == startTime){
            fromTimePickerDialog.show();
        }
        else if( v==locationTV){
            startActivity(new Intent(this, SearchActivity.class));
        }
        else if( v== crossImage){
            locationTV.setText("");
        }
        else if(v == selectFriends){
            startActivity(new Intent(this, FriendListActivity.class));
        }
    }
}

