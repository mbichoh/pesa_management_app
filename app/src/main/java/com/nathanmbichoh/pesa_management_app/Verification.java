package com.nathanmbichoh.pesa_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.nathanmbichoh.pesa_management_app.model.RequestHandler;
import com.nathanmbichoh.pesa_management_app.model.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Verification extends AppCompatActivity {

    private Button btnVerify;
    private EditText txtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //if session in on -login
//        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
//            finish();
//            startActivity(new Intent(this, Login.class));
//        }

        txtCode = (EditText) findViewById(R.id.verificationTxt);
        btnVerify = (Button) findViewById(R.id.verifyBtn);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVerified();
            }
        });

    }

    private void isVerified() {
        final String code = txtCode.getText().toString();

        //validate input
        if (TextUtils.isEmpty(code)) {
            txtCode.setError("Fill in this field");
            txtCode.requestFocus();
            return;
        }

        //if input
        class Verify extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }else if(obj.getBoolean("error")){
                        View view = findViewById(R.id.verifyBtn);
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
                        View view = findViewById(R.id.verifyBtn);
                        Snackbar snackbar = Snackbar.make(view, "Invalid Verification Code", Snackbar.LENGTH_LONG);
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

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("code", code);

                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_VERIFY, params);
            }
        }

        Verify ul = new Verify();
        ul.execute();
    }
}
