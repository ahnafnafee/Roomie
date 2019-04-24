package com.example.roomieprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
// Firebase imports
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity1 extends AppCompatActivity {
    private TextInputEditText mFullNameView;
    private TextInputLayout fullnameView;

    private TextInputEditText mUserNameView;
    private TextInputLayout usernameView;

    private TextInputEditText mEmailView;
    private TextInputLayout emailView;

    private TextInputEditText mPasswordView;
    private TextInputLayout passwordView;

    private TextInputEditText mPassword2View;
    private TextInputLayout password2View;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_1);

        // firebase database initiated
        db = FirebaseFirestore.getInstance();

        // firebase auth initiated
        mAuth = FirebaseAuth.getInstance();

        // text layouts
        fullnameView = findViewById(R.id.fullname_text_input);
        usernameView = findViewById(R.id.username_text_input);
        emailView = findViewById(R.id.email_reg_text_input);
        passwordView = findViewById(R.id.password_reg_text_input);
        password2View = findViewById(R.id.password2_reg_text_input);

        // test inputs
        mFullNameView = findViewById(R.id.fullname);
        mUserNameView = findViewById(R.id.username);
        mEmailView = findViewById(R.id.email_reg);
        mPasswordView = findViewById(R.id.password_reg);
        mPassword2View = findViewById(R.id.password2_reg);

        // registration action
        Button btnSignUp = findViewById(R.id.signup_reg_button);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseReg();
            }
        });

        // going back to login screen
        TextView signBack = findViewById(R.id.signin_back);
        signBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(signinIntent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void FireBaseReg() {
        // Getting text from inputs
        final String fullname = mFullNameView.getText().toString().trim();
        final String username = mUserNameView.getText().toString().trim();
        final String email = mEmailView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();
        final String password2 = mPassword2View.getText().toString().trim();

        //TODO: Add validation checks for the string fields


        //TODO: Validate password against the two fields


        final DocumentReference docRef = db.collection("userData").document(email);

        // auth with email and pass
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // store in firebase db
                            RegData regData = new RegData(
                                    fullname,
                                    username,
                                    email
                            );

                            docRef.set(regData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUpActivity1.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                            Intent myIntent = new Intent(getBaseContext(), TraitsActivity.class);
                                            startActivity(myIntent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity1.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(SignUpActivity1.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
