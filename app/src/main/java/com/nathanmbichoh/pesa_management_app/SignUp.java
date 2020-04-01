package com.nathanmbichoh.pesa_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

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

            }
        });

        //login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Verification.class));
            }
        });
    }
}
