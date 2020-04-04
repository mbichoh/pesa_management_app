package com.nathanmbichoh.pesa_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nathanmbichoh.pesa_management_app.model.RequestHandler;
import com.nathanmbichoh.pesa_management_app.model.SharedPrefManager;
import com.nathanmbichoh.pesa_management_app.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    String insertData = "https://ect.co.ke/pma/newplan.php";

    private LinearLayout mLinearLayout, mCustomLinearLayout;
    private ConstraintLayout mConstraintLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView mImageViewUp;

    private TextView txtUsername, txtSelectedDate;
    private LinearLayout mLinearActive, mLinearComplete, mLinearCanceled, mLinearLogout, btnShowDatePicker;

    private TextInputLayout mTextInputLayout;
    private AutoCompleteTextView mAutoCompleteTextView;

    private TextInputEditText txtPlanName, txtAmountDep, txtAmountRec;

    private Button btnSavePlan;

    String user_id, phone, dateReceive, save_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.custom_bottom_sheet_constraint);


        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        mBottomSheetBehavior = BottomSheetBehavior.from(mConstraintLayout);
        mCustomLinearLayout = (LinearLayout) findViewById(R.id.custom_linear_layout);
        mImageViewUp = (ImageView) findViewById(R.id.custom_header_arrow);

        //fetch user logged in name
        txtUsername = (TextView) findViewById(R.id.txtUserWelcome);
        txtUsername.setText("Welcome "+user.getName());

        user_id = ""+user.getId();
        phone = user.getPhone();

        //linear buttons
        mLinearActive = (LinearLayout) findViewById(R.id.activePlansBtn);
        mLinearCanceled = (LinearLayout) findViewById(R.id.canceledPlansBtn);
        mLinearComplete = (LinearLayout) findViewById(R.id.completedPlansBtn);
        mLinearLogout  = (LinearLayout) findViewById(R.id.logoutBtn);

        //textview
        txtSelectedDate = (TextView) findViewById(R.id.txtSelectedDate);

        //TextInputEditText
        txtPlanName = (TextInputEditText) findViewById(R.id.planNameTxt);
        txtAmountDep = (TextInputEditText) findViewById(R.id.amountTxt);
        txtAmountRec = (TextInputEditText) findViewById(R.id.amountExpectTxt);

        //buttons
        btnSavePlan = (Button) findViewById(R.id.savePlanBtn);
        btnSavePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AddData();
            }
        });

        //datePicker
        btnShowDatePicker = (LinearLayout) findViewById(R.id.btnShowDatePicker);
        btnShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //datePicker
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

        //logout
        mLinearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Dashboard.this, Login.class));
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
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                save_type = parent.getAdapter().getItem(position).toString();
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int monthed = month + 1;
        txtSelectedDate.setText("Receiving Date:  "+year+"-"+monthed+"-"+dayOfMonth);
        dateReceive = year+"-"+month+"-"+dayOfMonth;
    }

    private void AddData() {
        final String name = txtPlanName.getText().toString().trim();
        final String deposit = txtAmountDep.getText().toString().trim();
        final String receive = txtAmountRec.getText().toString().trim();

        //validations
        if(TextUtils.isEmpty(name)){
            txtPlanName.setError("Fill in this field");
            txtPlanName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(deposit)){
            txtAmountDep.setError("Fill in this field");
            txtAmountDep.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(receive)){
            txtAmountRec.setError("Fill in this field");
            txtAmountRec.requestFocus();
            return;
        }

       // Toast.makeText(getApplicationContext(), name +", "+deposit+", "+receive+", "+dateReceive+", "+phone+", "+save_type+", "+user_id ,Toast.LENGTH_LONG).show();

        class CreatePlan extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("plan_name", name);
                params.put("save_type", save_type);
                params.put("deposit", deposit);
                params.put("receive", receive);
                params.put("phone", phone);
                params.put("date", dateReceive);
                params.put("user_id", user_id);

                //returing the response
                return requestHandler.sendPostRequest(insertData, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        View view = findViewById(R.id.savePlanBtn);
                        Snackbar snackbar = Snackbar.make(view, obj.getString("message"), Snackbar.LENGTH_LONG);
                        snackbar.setDuration(10000);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.setAction("OKAY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Do something
                            }
                        });
                        snackbar.show();

                    }else {
                        View view = findViewById(R.id.savePlanBtn);
                        Snackbar snackbar = Snackbar.make(view, "Some error occurred", Snackbar.LENGTH_LONG);
                        snackbar.setDuration(10000);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.setAction("OKAY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Do something
                            }
                        });
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        CreatePlan ru = new CreatePlan();
        ru.execute();

    }
}
