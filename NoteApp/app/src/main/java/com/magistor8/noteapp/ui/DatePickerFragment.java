package com.magistor8.noteapp.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.magistor8.noteapp.R;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private DialogInterface.OnClickListener positiveButton;

    public void setPositiveButton(DialogInterface.OnClickListener positiveButton) {
        this.positiveButton = positiveButton;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Выберите дату")
                .setView(R.layout.datepicker_dialog)
                .setPositiveButton("OK", positiveButton)
                .setNegativeButton("Отмена", null)
                .create();
    }
}
