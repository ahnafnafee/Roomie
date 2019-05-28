package com.example.roomieprototype;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomieprototype.signUp.SignUpActivity1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public TextView test1;
    public ProgressBar progBar;
    FirebaseUser firebaseUser;
    // UI references.
    private TextInputEditText mEmailView;
    private TextInputLayout emailView;
    private TextInputEditText mPasswordView;
    private TextInputLayout passwordView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        emailView = findViewById(R.id.email_text_input);

        mPasswordView = findViewById(R.id.password);
        passwordView = findViewById(R.id.password_text_input);

        emailView.setErrorTextAppearance(R.style.TextFieldError);
        passwordView.setErrorTextAppearance(R.style.TextFieldError);

        test1 = findViewById(R.id.pass_forgot);
        test1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), UserAccount.class));
            }
        });


        // Firebase
        mAuth = FirebaseAuth.getInstance();

        // Progress Bar
        progBar = findViewById(R.id.progBar);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // sign in btn
        Button mEmailSignInButton = findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // sign up button
        Button mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(getBaseContext(), SignUpActivity1.class);
                startActivity(signupIntent);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        //check if user is null
//        if (firebaseUser != null){
//            Intent intent = new Intent(LoginActivity.this, CardStack.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    private void attemptLogin() {

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(getBaseContext(), MatchingScreen.class);
                                startActivity(myIntent);
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}