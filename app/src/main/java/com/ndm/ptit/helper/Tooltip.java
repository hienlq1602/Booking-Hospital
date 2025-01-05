package com.ndm.ptit.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


import com.ndm.ptit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class Tooltip {

    public static String getToday()
    {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar calendar = Calendar.getInstance(timeZone);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        String dateValue = String.valueOf(date);
        if( date < 10)
        {
            dateValue = "0" + dateValue;
        }
        String monthValue = String.valueOf(month);
        if( month < 10)
        {
            monthValue = "0" + monthValue;
        }
        String yearValue = String.valueOf(year);

        return yearValue + "-" + monthValue + "-" + dateValue;
    }


    @SuppressLint("SimpleDateFormat")
    public static String beautifierDatetime(Context context, String input)
    {
        if( input.length() != 19)
        {
            return "Tooltip - beautifierDatetime - error: value is not valid " + input.length();
        }
        String output = "";

        String dateInput = input.substring(0,10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String dateOutput = "";
        DateFormat dateFormat = new SimpleDateFormat("EE, dd-MM-yyyy");
        try
        {
            Date dateFormatted = formatter.parse(dateInput);
            assert dateFormatted != null;
            dateOutput = dateFormat.format(dateFormatted);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String timeOutput = input.substring(11, 16);

        output = dateOutput + " " +context.getString(R.string.at) + " " + timeOutput;

        return output;
    }



    public static void setLocale(Context context, SharedPreferences sharedPreferences)
    {
        /*tu bo nho ROM cua thiet bi lay ra ngon ngu da cai dat cho ung dung*/
        String language = sharedPreferences.getString("language", context.getString(R.string.vietnamese));

        String vietnamese = context.getString(R.string.vietnamese);
            String deutsch = context.getString(R.string.deutsch);


        Locale myLocale = new Locale("en");
        if(Objects.equals(language, vietnamese))
        {
            myLocale = new Locale("vi");
        }
        if(Objects.equals(language,deutsch))
        {
            myLocale = new Locale("de");
        }


        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();


        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(myLocale);


        Locale.setDefault(myLocale);
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
