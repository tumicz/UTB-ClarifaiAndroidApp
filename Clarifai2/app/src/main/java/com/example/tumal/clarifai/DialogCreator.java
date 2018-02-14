package com.example.tumal.clarifai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by TumaL on 2/13/2018.
 */

public class DialogCreator {

    public static void createYesNoDialog(String text, final Command<Boolean> onUserClick, Context context)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        onUserClick.execute(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        onUserClick.execute(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
