package com.example.dateappinterface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class OutOfPairsDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Out of Pairs")
                .setMessage("You have viewed all pairs matching your preferences")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openDialog();
                    }
                });
        return builder.create();
    }

    public void openDialog(){
        OutOfPairsDialog outOfPairsDialog = new OutOfPairsDialog();
        outOfPairsDialog.show(getFragmentManager(), "out of pairs");
    }
}
