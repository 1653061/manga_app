package com.tdusnewbie.root.hakoln.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tdusnewbie.root.hakoln.DialogFormatText;
import com.tdusnewbie.root.hakoln.R;

public class ReadNovelView extends AppCompatActivity implements DialogFormatText.DialogFormatTextListener {

    private ScrollView scrollView;
    private ImageButton previousChapter, nextChapter, formatText, formatTheme, goToNovelView;
    private TextView titleChapter, pageNumber, txtContent;
    private Toolbar barInfo, barChangePage;

    private double xDown, xUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_novel_view);
        initialize();
    }

    private void Mapping()
    {
        scrollView = findViewById(R.id.scrollView);

        previousChapter = findViewById(R.id.previosChapter);
        nextChapter = findViewById(R.id.nextChapter);
        formatText = findViewById(R.id.formatText);
        formatTheme = findViewById(R.id.formatTheme);
        goToNovelView = findViewById(R.id.goToNovelView);

        titleChapter = findViewById(R.id.titleChapter);
        pageNumber = findViewById(R.id.pageNumber);
        txtContent = findViewById(R.id.txtContent);

        barInfo = findViewById(R.id.barInfo);
        barChangePage = findViewById(R.id.barChangePage);

    }

    private void initialize()
    {
        Mapping();

        setSupportActionBar(barChangePage);

        previousChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BarChangePage","Previous Chapter");
            }
        });

        nextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BarChangePage","Next Chapter");
            }
        });

        formatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BarChangePage","Format Text");
                //Log.e("FontContent",txtContent.getTypeface().toString());
                onOpenDialogFormatText();
            }
        });

        formatTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BarChangePage","Format Theme");
            }
        });

        goToNovelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BarChangePage","Go To Novel View");
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Log.d("Gesture","Down");
                    xDown = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    Log.d("Gesture", "Move");
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    xUp = event.getX();
                    double distance = Math.abs(xDown-xUp);
                    Log.d("Gesture","Distance: " + distance);
                    if(distance < 20) {
                        if (barChangePage.isShown()) {
                            barChangePage.setVisibility(View.GONE);
                            barInfo.setVisibility(View.GONE);
                        }
                        else {
                            barChangePage.setVisibility(View.VISIBLE);
                            barInfo.setVisibility(View.VISIBLE);
                        }
                    }
                    //return true;
                }                if(event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    Log.d("Gesture", "Move");
                }
                return false;
            }
        });
    }

    private void onOpenDialogFormatText()
    {
        DialogFormatText dialogFormatText = new DialogFormatText();
        dialogFormatText.setSize(txtContent.getTextSize());
        dialogFormatText.setMarginLeft(scrollView.getPaddingLeft());
        dialogFormatText.setMarginRight(scrollView.getPaddingRight());
        dialogFormatText.show(getSupportFragmentManager(),"Format Text Dialog");
    }




    @Override
    public void formatingText(float newSize,int newMargin) {
        txtContent.setTextSize(newSize);
        scrollView.setPadding(newMargin,0,newMargin,0);
    }
}
