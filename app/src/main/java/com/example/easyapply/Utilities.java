package com.example.easyapply;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.util.Date;

import static android.support.v4.content.ContextCompat.getSystemService;


public class Utilities {
    private static int unity = 1000;
    private static int second = unity * 1;
    private static int minute = second * 60;
    private static int hour = minute * 60;
    private static int day = hour * 12;
    private static int week = day * 7;


    public Utilities() {
    }




    public boolean edittextvalidator(EditText editText) {

        String validate = editText.getText().toString();

        if (TextUtils.isEmpty(validate)) {
            editText.setError("El Texto no deber estar vacio");
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public Long getDate() {


        Date date = new Date();
        Long aLong = date.getTime();
        return aLong;

    }

    public String timeBeforePublication(Long fecha) {

        Date date = new Date();

        Long milisecr =date.getTime()-fecha;

        if (milisecr / week > 0 && milisecr / week <= 52)
            if (milisecr / week == 1)
                return milisecr / week + " Week Ago";
            else
                return milisecr / week + " Weeks Ago";

        else if (milisecr / day > 0)
            if (milisecr / day == 1)
                return milisecr / day + " Day Ago";
            else
                return milisecr / day + " Days Ago";

        else if (milisecr / hour > 0)
            if (milisecr / hour == 1)
                return milisecr / hour + " Hour Ago";
            else
                return milisecr / hour + " Hours Ago";
        else if (milisecr / minute > 0)
            if (milisecr / minute == 1)
                return milisecr / minute + " Minute Ago";
            else
                return milisecr / minute + " Minutes Ago";
            else if (milisecr / second > 0)
            if (milisecr / second ==1)
                return milisecr / second + " Second Ago";
            else
                return milisecr / second + " Seconds Ago";

            else
            return milisecr+ " A long time ago";

    }




}
