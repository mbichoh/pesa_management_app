package com.nathanmbichoh.pesa_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {

    private LinearLayout mLinearLayout, mCustomLinearLayout;
    private ConstraintLayout mConstraintLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView mImageViewUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.custom_bottom_sheet_constraint);

        mBottomSheetBehavior = BottomSheetBehavior.from(mConstraintLayout);
        mCustomLinearLayout = (LinearLayout) findViewById(R.id.custom_linear_layout);
        mImageViewUp = (ImageView) findViewById(R.id.custom_header_arrow);

        mCustomLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //returns an int and if you want to change staff
//                switch (newState){
//                    case BottomSheetBehavior.STATE_COLLAPSED;
//                    //do something
//                        break;
//                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mImageViewUp.setRotation(slideOffset * 180);
            }
        });
    }
}
