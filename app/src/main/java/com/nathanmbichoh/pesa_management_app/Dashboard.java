package com.nathanmbichoh.pesa_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.TimeZone;

public class Dashboard extends AppCompatActivity {

    private LinearLayout mLinearLayout, mCustomLinearLayout;
    private ConstraintLayout mConstraintLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView mImageViewUp, btnShowDatePicker;

    private TextView txtUsername, txtSelectedDate;
    private LinearLayout mLinearActive, mLinearComplete, mLinearCanceled;

    private TextInputLayout mTextInputLayout;
    private AutoCompleteTextView mAutoCompleteTextView;

    private TextInputEditText txtPlanName, txtAmountDep, txtAmountRec;

    private Button btnSavePlan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.custom_bottom_sheet_constraint);

        mBottomSheetBehavior = BottomSheetBehavior.from(mConstraintLayout);
        mCustomLinearLayout = (LinearLayout) findViewById(R.id.custom_linear_layout);
        mImageViewUp = (ImageView) findViewById(R.id.custom_header_arrow);

        //fetch user logged in name
        txtUsername = (TextView) findViewById(R.id.txtUserWelcome);

        //linear buttons
        mLinearActive = (LinearLayout) findViewById(R.id.activePlansBtn);
        mLinearCanceled = (LinearLayout) findViewById(R.id.canceledPlansBtn);
        mLinearComplete = (LinearLayout) findViewById(R.id.completedPlansBtn);

        //textview
        txtSelectedDate = (TextView) findViewById(R.id.txtSelectedDate);

        //TextInputEditText
        txtPlanName = (TextInputEditText) findViewById(R.id.planNameTxt);
        txtAmountDep = (TextInputEditText) findViewById(R.id.amountTxt);
        txtAmountRec = (TextInputEditText) findViewById(R.id.amountExpectTxt);

        //buttons
        btnSavePlan = (Button) findViewById(R.id.savePlanBtn);

        //datePicker
        btnShowDatePicker = (ImageView) findViewById(R.id.btnShowDatePicker);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Nairobi"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        calendar.setTimeInMillis(today);

        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        long january = calendar.getTimeInMillis();

        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        long december = calendar.getTimeInMillis();

        //calender constraints
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setStart(january);
        constraintBuilder.setEnd(december);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();  //for datePicker() single month and select show
        builder.setTitleText("SELECT A DATE");
        builder.setSelection(today); //get today
        builder.setCalendarConstraints(constraintBuilder.build()); //get year calender
        final MaterialDatePicker materialDatePicker = builder.build();

        btnShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                txtSelectedDate.setText("Receiving Date : "+materialDatePicker.getHeaderText());
            }
        });

        //linear active
        mLinearActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Active Card clicked", Snackbar.LENGTH_LONG);
                snackbar.setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something
                    }
                });
                snackbar.show();

            }
        });
        //linear completed
        mLinearComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Completed Card clicked", Snackbar.LENGTH_LONG);
                snackbar.setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something
                    }
                });
                snackbar.show();

            }
        });

        //linear canceled
        mLinearCanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Canceled Card clicked", Snackbar.LENGTH_LONG);
                snackbar.setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something
                    }
                });
                snackbar.show();

            }
        });

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

        //dropdown
        mTextInputLayout = (TextInputLayout) findViewById(R.id.txtDropdownLayout);
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.dropdown);

        String[] items = new String[]{
                "Save Money",
                "Airtime(Coming soon)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Dashboard.this,
                R.layout.dropdown_item,
                items
        );

        mAutoCompleteTextView.setAdapter(adapter);
    }
}
