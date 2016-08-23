package com.example.kaushalmandayam.eventmanager;

/**
 * Created by Kaushal.Mandayam on 8/22/2016.
 */

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class Utility {

    public static void showSimpleAlertDialog(Context context, String title, String message){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}
