package com.android.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailView.getText().toString().equals("")) {
                    mEmailView.setError("Email Required");
                } else if (mPasswordView.getText().toString().equals("")) {
                    mPasswordView.setError("Password Required");
                } else if (mPasswordView.getText().toString().equals("") && mEmailView.getText().equals("")) {
                    mEmailView.setError("Email Required");
                    mPasswordView.setError("Password Required");
                } else {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", mEmailView.getText().toString());
                    editor.putBoolean("IsLoggedIn", true);
                    editor.commit();

                    Toast.makeText(LoginActivity.this, "You are successfully logged in as: "
                                    + mEmailView.getText().toString() + " with password: " + mPasswordView.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

}

