package com.nathanmbichoh.pesa_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {

    private TextInputEditText txtUname, txtPword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUname = (TextInputEditText) findViewById(R.id.usernameTxt);
        txtPword = (TextInputEditText) findViewById(R.id.passwordTxt);

        btnLogin = (Button) findViewById(R.id.loginBtn);
        btnSignUp = (Button) findViewById(R.id.toLoginBtn);

        //login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //after verification
                userLogin();
            }
        });

        //sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String phone = txtUname.getText().toString();
        final String password = txtPword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(phone)) {
            txtUname.setError("Please enter your username");
            txtUname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtPword.setError("Please enter your password");
            txtPword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        String message = obj.getString("message").toString();
                        View view = findViewById(R.id.loginBtn);
                            if(message.equals("Verify Account")) {
                                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                                snackbar.setDuration(10000);
                                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                snackbar.setAction("CLICK HERE TO VERIFY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), Verification.class));
                                    }
                                });
                                snackbar.show();
                            }else if(message.equals("Invalid phone or password")) {
                                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                                snackbar.setDuration(10000);
                                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                snackbar.setAction("OKAY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Do something
                                    }
                                });
                                snackbar.show();
                            }else if(message.equals("Login successful")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("phone"),
                                        userJson.getString("code")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));

                            }
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password);

                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}