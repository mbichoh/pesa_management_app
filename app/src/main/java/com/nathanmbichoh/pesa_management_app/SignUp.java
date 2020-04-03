package com.nathanmbichoh.pesa_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.nathanmbichoh.pesa_management_app.model.RequestHandler;
import com.nathanmbichoh.pesa_management_app.model.SharedPrefManager;
import com.nathanmbichoh.pesa_management_app.model.URLs;
import com.nathanmbichoh.pesa_management_app.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private TextInputEditText txtName, txtEmail, txtPhone, txtPassword;
    private Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        txtName = (TextInputEditText) findViewById(R.id.fullnameTxt);
        txtEmail = (TextInputEditText) findViewById(R.id.emailTxt);
        txtPhone = (TextInputEditText) findViewById(R.id.phoneTxt);
        txtPassword = (TextInputEditText) findViewById(R.id.passTxt);

        btnLogin = (Button) findViewById(R.id.toLoginBtn);
        btnSignUp = (Button) findViewById(R.id.signUpBtn);

        //sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    registerUser();
            }
        });

        //login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });

//        //if user is login
//        if(SharedPrefManager.getInstance(this).isLoggedIn()){
//            finish();
//            startActivity(new Intent(this, Dashboard.class));
//            return;
//        }

    }

    public void registerUser(){
        final String name = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String phone = txtPhone.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        //validations
        if(TextUtils.isEmpty(name)){
            txtName.setError("Fill in this field");
            txtName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(email)){
            txtEmail.setError("Fill in this field");
            txtEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            txtPhone.setError("Fill in this field");
            txtPhone.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            txtPassword.setError("Fill in this field");
            txtPassword.requestFocus();
            return;
        }


        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), Verification.class));
                    }else if(obj.getBoolean("error")){
                        View view = findViewById(R.id.signUpBtn);
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
                    } else {
                        View view = findViewById(R.id.signUpBtn);
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
        RegisterUser ru = new RegisterUser();
        ru.execute();

    }
}
