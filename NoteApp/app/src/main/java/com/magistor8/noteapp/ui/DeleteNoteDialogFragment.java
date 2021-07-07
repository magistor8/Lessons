package com.magistor8.noteapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.magistor8.noteapp.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeleteNoteDialogFragment extends BottomSheetDialogFragment {

    private View.OnClickListener positiveButton;

    public static DeleteNoteDialogFragment newInstance() {
        return new DeleteNoteDialogFragment();
    }

    public void setPositiveButton(View.OnClickListener positiveButton) {
        this.positiveButton = positiveButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);

        // Запретим пользователю выходить без выбора
        setCancelable(false);

        view.findViewById(R.id.btnOk).setOnClickListener(positiveButton);
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());

        return view;
    }

}
