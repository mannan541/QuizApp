package com.android.quizapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.quizapp.R;
import com.android.quizapp.models.User;

import java.util.List;

import static com.android.quizapp.R.id.email;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(email);
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

                    List<User> loginInfoList = User.find(User.class, "email = ? ", mEmailView.getText().toString());
                    if (loginInfoList.size() == 0 || loginInfoList.equals(null)) {
                        User user = new User("Name",
                                mEmailView.getText().toString(),
                                mPasswordView.getText().toString());
                        user.save();
                    }

                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}

