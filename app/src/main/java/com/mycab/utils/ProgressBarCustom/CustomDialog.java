package com.mycab.utils.ProgressBarCustom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.WindowManager;

public class CustomDialog implements DialogInterface {

    public static Dialog dialog;

    @Override
    public void showDialog(int view, Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("sfgdsgv", e.getMessage(), e);
        }

    }

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }
}
