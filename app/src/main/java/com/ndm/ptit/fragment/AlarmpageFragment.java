package com.ndm.ptit.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.ndm.ptit.R;

import java.util.ArrayList;
import java.util.Calendar;
public class AlarmpageFragment extends Fragment {

    private final String TAG = "Alarm-page Fragment";

    private AppCompatButton btnConfirm;
    private TextView txtTimeValue;
    private AppCompatButton btnTimepicker;


    private AppCompatCheckBox cbxVibrate;
    private AppCompatCheckBox cbxMonday, cbxTuesday,cbxWednesday, cbxThursday, cbxFriday, cbxSaturday, cbxSunday;


    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    MaterialTimePicker timePicker = new MaterialTimePicker
            .Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour)
            .setMinute(minute)
            .build();

    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
    private TextView EXTRA_MESSAGE;
    int EXTRA_HOUR = 9;
    int EXTRA_MINUTE = 0;
    ArrayList<Integer> EXTRA_DAYS = new ArrayList<>();

    private Context context;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarmpage, container, false);

        setupComponent(view);
        setupEvent();

        intent.putExtra(AlarmClock.EXTRA_RINGTONE, R.raw.alarm_sound_3);

        Bundle bundle = getArguments();
        if( bundle != null)
        {
            String message = bundle.getString("message");
            EXTRA_MESSAGE.setText( message );
        }

        return view;
    }

    private void setupComponent(View view)
    {
        context = requireContext();
        activity = requireActivity();

        btnConfirm = view.findViewById(R.id.btnConfirm);
        txtTimeValue = view.findViewById(R.id.txtTimeValue);
        btnTimepicker = view.findViewById(R.id.btnTimepicker);
        EXTRA_MESSAGE = view.findViewById(R.id.txtMessage);

        cbxVibrate = view.findViewById(R.id.cbxVibrate);
        cbxMonday = view.findViewById(R.id.cbxMonday);
        cbxTuesday = view.findViewById(R.id.cbxTuesday);
        cbxWednesday = view.findViewById(R.id.cbxWednesday);
        cbxThursday = view.findViewById(R.id.cbxThursday);
        cbxFriday = view.findViewById(R.id.cbxFriday);
        cbxSaturday = view.findViewById(R.id.cbxSaturday);
        cbxSunday = view.findViewById(R.id.cbxSunday);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void setupEvent()
    {
        /*BUTTON TIME PICKER*/
        btnTimepicker.setOnClickListener(view->{

            timePicker.show(getParentFragmentManager(), null);
            timePicker.addOnPositiveButtonClickListener(view1 -> {
                EXTRA_HOUR = timePicker.getHour();
                EXTRA_MINUTE = timePicker.getMinute();

                String hourValue = String.valueOf(EXTRA_HOUR);
                String minuteValue = String.valueOf(EXTRA_MINUTE);

                if( EXTRA_HOUR < 10)
                {
                    hourValue = "0" + EXTRA_HOUR;
                }
                if( EXTRA_MINUTE < 10)
                {
                    minuteValue = "0" + EXTRA_MINUTE;
                }

                String timeValue = hourValue + ":" + minuteValue;
                txtTimeValue.setText(timeValue);
            });
        });
        /*end BUTTON TIME PICKER*/


        /*CHECK BOX VIBRATE*/
        cbxVibrate.setOnCheckedChangeListener((compoundButton, isVibrate) -> intent.putExtra(AlarmClock.EXTRA_VIBRATE, isVibrate));

        /*CHECKBOX MONDAY -> SUNDAY*/
        cbxMonday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.MONDAY);
            }
            else
            {
                removeElementFromArray(Calendar.MONDAY);
            }
        });

        cbxTuesday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.TUESDAY);
            }
            else
            {
                removeElementFromArray(Calendar.TUESDAY);
            }
        });

        cbxWednesday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.WEDNESDAY);
            }
            else
            {
                removeElementFromArray(Calendar.WEDNESDAY);
            }
        });

        cbxThursday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.THURSDAY);
            }
            else
            {
                removeElementFromArray(Calendar.THURSDAY);
            }
        });

        cbxFriday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.FRIDAY);
            }
            else
            {
                removeElementFromArray(Calendar.FRIDAY);
            }
        });

        cbxSaturday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.SATURDAY);
            }
            else
            {
                removeElementFromArray(Calendar.SATURDAY);
            }
        });

        cbxSunday.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked)
            {
                EXTRA_DAYS.add(Calendar.SUNDAY);
            }
            else
            {
                removeElementFromArray(Calendar.SUNDAY);
            }
        });/*end CHECKBOX MONDAY -> SUNDAY */


        /*BUTTON CONFIRM*/
        btnConfirm.setOnClickListener(view -> {
            intent.putExtra(AlarmClock.EXTRA_HOUR, EXTRA_HOUR);
            intent.putExtra(AlarmClock.EXTRA_MINUTES, EXTRA_MINUTE);
            intent.putExtra(AlarmClock.EXTRA_MESSAGE, EXTRA_MESSAGE.getText().toString());
            intent.putExtra(AlarmClock.EXTRA_DAYS, EXTRA_DAYS);

            Intent intentChooser = Intent.createChooser(intent, "Chon App de dat loi nhac nho");
            startActivity(intentChooser);
//            activity.finish();

//             if (intent.resolveActivity(this.getPackageManager()) != null) {
//                 startActivity(intent);
//                 Toast.makeText(this, "Alarm has been set", Toast.LENGTH_SHORT).show();
//             }
//             else
//             {
//
//             }
        });/*end BUTTON CONFIRM*/
    }

    private void removeElementFromArray(int value)
    {
        EXTRA_DAYS.removeIf(element -> element == value);
    }
}