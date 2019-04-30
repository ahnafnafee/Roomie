package com.example.roomieprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

// Firebase imports


public class SignUpActivity3 extends AppCompatActivity {
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
    private FirebaseUser user;

    //Initialising Spinner variables

    String sleepSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_3);

        NiceSpinner sleepSpinner = findViewById(R.id.sleep_spinner);
        NiceSpinner cleanSpinner = findViewById(R.id.clean_spinner);
        NiceSpinner eatSpinner = findViewById(R.id.eat_spinner);
        NiceSpinner studySpinner = findViewById(R.id.study_spinner);
        NiceSpinner socialSpinner = findViewById(R.id.social_spinner);
        NiceSpinner temperatureSpinner = findViewById(R.id.temperature_spinner);
        NiceSpinner apartmentSpinner = findViewById(R.id.apartment_spinner);
        NiceSpinner dormSpinner = findViewById(R.id.dorm_spinner);

        //Populating Spinners
        List<String> dataset1 = new LinkedList<>(Arrays.asList("Night Owl (After 12 am)", "Early Bird (Before 12 am)", "Depends on the day"));
        sleepSpinner.attachDataSource(dataset1);

        List<String> dataset2 = new LinkedList<>(Arrays.asList("Neat Freak", "Relatively Neat", "Relatively Messy", "Messy"));
        cleanSpinner.attachDataSource(dataset2);

        List<String> dataset3 = new LinkedList<>(Arrays.asList("Vegetarian", "Vegan", "Halal", "Kosher", "No Preference"));
        eatSpinner.attachDataSource(dataset3);

        List<String> dataset4 = new LinkedList<>(Arrays.asList("With Music", "Quiet", "With Other People", "By Myself"));
        studySpinner.attachDataSource(dataset4);

        List<String> dataset5 = new LinkedList<>(Arrays.asList("Party Animal", "Depends on my mood", "Couch Potatoes"));
        socialSpinner.attachDataSource(dataset5);

        List<String> dataset6 = new LinkedList<>(Arrays.asList("Freezing", "Cold", "Moderate", "Warm", "Melting"));
        temperatureSpinner.attachDataSource(dataset6);

        List<String> dataset7 = new LinkedList<>(Arrays.asList("Yes", "No"));
        apartmentSpinner.attachDataSource(dataset7);

        List<String> dataset8 = new LinkedList<>(Arrays.asList("Dorm", "Suite"));
        dormSpinner.attachDataSource(dataset8);

        // firebase database initiated
        //db = FirebaseFirestore.getInstance();

        // firebase auth initiated
        //mAuth = FirebaseAuth.getInstance();

        // User initiated
        //user = mAuth.getCurrentUser();

        //final CollectionReference colRef = db.collection("userData").document(user.getEmail()).collection("Traits");


        //Initializing the selected Traits Listeners to variables
        /*sleepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sleepSchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cleanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cleanSchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        eatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String eatSchedule = parent.getSelectedItem().toString();

            }
        });
        studySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String studySchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        socialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String socialSchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        temperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temperatureSchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        apartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String apartmentSchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dormSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dormSchedule = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        // registration action
        Button btnSignUp = findViewById(R.id.signup_reg_button);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //colRef.add(sleepSchedule);
                Intent myIntent = new Intent(getBaseContext(), CardStack.class);
                startActivity(myIntent);
            }
        });
    }

}
