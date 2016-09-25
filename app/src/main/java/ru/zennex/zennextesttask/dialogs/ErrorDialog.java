package ru.zennex.zennextesttask.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import ru.zennex.zennextesttask.R;
import ru.zennex.zennextesttask.fragments.ParsingFragment;

import static ru.zennex.zennextesttask.fragments.ParsingFragment.PARSING_FRAGMENT_TAG;

/**
 * Created by Kostez on 23.09.2016.
 */

public class ErrorDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private String message = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error)
                .setMessage(message)
                .setNegativeButton(R.string.revert, this)
                .setPositiveButton(R.string.done, this);

        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case Dialog.BUTTON_POSITIVE:
                ((ParsingFragment) getFragmentManager().findFragmentByTag(PARSING_FRAGMENT_TAG)).startDownload();
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
