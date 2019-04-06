package com.example.roomieprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView signBack = findViewById(R.id.signin_back);

        signBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(signinIntent);
            }
        });


    }


}
