package com.example.dateappinterface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class WrongLoginDataDialog extends AppCompatDialogFragment {
    String reason = new String();
    public WrongLoginDataDialog(String reason){
        this.reason = reason;
    }

    public WrongLoginDataDialog() {    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wrong Data")
                .setMessage("Your "+reason+ " is incorrect, try again")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openDialog();
                    }
                });
        return builder.create();
    }

    public void openDialog(){
        WrongLoginDataDialog wrongLoginDataDialog = new WrongLoginDataDialog();
        wrongLoginDataDialog.show(getFragmentManager(), "Wrong "+reason);
    }
}