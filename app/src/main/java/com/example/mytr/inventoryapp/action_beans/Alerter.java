package com.example.mytr.inventoryapp.action_beans;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.mytr.inventoryapp.data_beans.AlertMessage;

public class Alerter {

    public static void showAlert(Context context, AlertMessage alertMessage) {
       new AlertDialog.Builder(context)
                .setTitle(alertMessage.getTitle())
                .setMessage(alertMessage.getMessage())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
