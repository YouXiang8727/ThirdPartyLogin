package com.youxiang8727.thirdpartylogin.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomAlertDialogUtils {
    private Context context;
    private Activity activity;
    public CustomAlertDialogUtils(Context context){
        this.context = context;
        this.activity = (Activity) context;
    }
    public void showAlert(String title,
                          String message,
                          String positiveText,
                          DialogInterface.OnClickListener positiveOnclickListener,
                          boolean cancelable){
        if (activity.isFinishing()){
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveOnclickListener)
                .setCancelable(cancelable)
                .create();
        alertDialog.show();
    }

}
