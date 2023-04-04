package com.example.hemoshare;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EnterCodeDialog extends AppCompatDialogFragment {

    private EditText edtCode;
    private EnterCodeDialogListner enterCodeDialogListner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_enter_code,null);
        builder.setView(view)
                .setTitle("Completion Code")
                .setIcon(R.drawable.hemo_share_logo)
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code = edtCode.getText().toString();
                        enterCodeDialogListner.applyTexts(code);
                    }
                });
        edtCode = view.findViewById(R.id.edtCode);
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            enterCodeDialogListner = (EnterCodeDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement EnterCodeDialogListner");
        }
    }

    public interface  EnterCodeDialogListner{
        void applyTexts(String code);
    }
}
