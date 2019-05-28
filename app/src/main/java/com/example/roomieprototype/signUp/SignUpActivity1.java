package com.example.roomieprototype.signUp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomieprototype.LoginActivity;
import com.example.roomieprototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

// Firebase imports

public class SignUpActivity1 extends AppCompatActivity {
    private TextInputEditText mFullNameView;
    private TextInputLayout fullnameView;

    private TextInputEditText mEmailView;
    private TextInputLayout emailView;

    private TextInputEditText mPasswordView;
    private TextInputLayout passwordView;

    private TextInputEditText mPassword2View;
    private TextInputLayout password2View;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;


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
        emailView = findViewById(R.id.email_reg_text_input);
        passwordView = findViewById(R.id.password_reg_text_input);
        password2View = findViewById(R.id.password2_reg_text_input);

        // test inputs
        mFullNameView = findViewById(R.id.fullname);
        mEmailView = findViewById(R.id.email_reg);
        mPasswordView = findViewById(R.id.password_reg);
        mPassword2View = findViewById(R.id.password2_reg);

        // registration action
        Button btnNext = findViewById(R.id.next_reg_button_1);
        btnNext.setOnClickListener(new View.OnClickListener() {
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
                            final RegData regData = new RegData(
                                    fullname,
                                    email
                            );

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            UserProfileChangeRequest updateDisplayName = new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                            UserProfileChangeRequest updateImageURL = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("gs://roomieprototype.appspot.com/" + email)).build();

                            firebaseUser.updateProfile(updateDisplayName);
                            firebaseUser.updateProfile(updateImageURL);

                            final HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("email", email);
                            hashMap.put("fullname", fullname);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");

                            docRef.set(regData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            reference.setValue(hashMap);
                                            Toast.makeText(SignUpActivity1.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                            Intent myIntent = new Intent(getBaseContext(), SignUpActivity2.class);
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

        //Sign in to firebase after registration
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity1.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
