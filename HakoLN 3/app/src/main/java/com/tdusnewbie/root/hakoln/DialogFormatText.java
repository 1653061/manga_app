package com.tdusnewbie.root.hakoln;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DialogFormatText extends AppCompatDialogFragment {

    private TextView textSize, marginHorizontal;
    private ImageButton increaseSize, increaseMargin, decreaseSize, decreaseMargin;
    private float size;
    private int marginLeft, marginRight, spSize, dpMargin;
    private DialogFormatTextListener listener;
    private static final int SIZE_INCREASE = 5;
    private static final int MARGIN_INCREASE = 20;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialog_format_text,null);
        builder.setView(view).setTitle("Format Text");
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });

        Mapping(view);
        LoadData();

        return builder.create();
    }

    private void Mapping(View view)
    {
        textSize = view.findViewById(R.id.textSize);
        marginHorizontal = view.findViewById(R.id.marginHorizontal);

        increaseMargin = view.findViewById(R.id.increaseMargin);
        increaseSize = view.findViewById(R.id.increaseSize);
        decreaseMargin = view.findViewById(R.id.decreaseMargin);
        decreaseSize = view.findViewById(R.id.decreaseSize);

    }

    private void LoadData()
    {
        spSize = (int) (size / getContext().getResources().getDisplayMetrics().scaledDensity);
        dpMargin = marginRight = marginLeft;
        marginHorizontal.setText(String.valueOf(dpMargin));
        textSize.setText(String.valueOf(spSize));

        increaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spSize += SIZE_INCREASE;
                textSize.setText(String.valueOf(spSize));
                listener.formatingText(spSize,dpMargin);
            }
        });

        decreaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spSize -= SIZE_INCREASE;
                textSize.setText(String.valueOf(spSize));
                listener.formatingText(spSize,dpMargin);
            }
        });

        increaseMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpMargin += MARGIN_INCREASE;
                marginHorizontal.setText(String.valueOf(dpMargin));
                listener.formatingText(spSize,dpMargin);
            }
        });

        decreaseMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpMargin -= MARGIN_INCREASE;
                marginHorizontal.setText(String.valueOf(dpMargin));
                listener.formatingText(spSize,dpMargin);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogFormatTextListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogFormatTextListener");
        }
    }

    public interface DialogFormatTextListener
    {
        void formatingText(float newSize,int newMargin);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

}
