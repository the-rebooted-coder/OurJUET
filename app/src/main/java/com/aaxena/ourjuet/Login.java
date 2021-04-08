package com.aaxena.ourjuet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText enr;
    EditText password;
    EditText dob;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enr = findViewById(R.id.enrNumber);
        password = findViewById(R.id.password);
        dob = findViewById(R.id.dob);
        signIn = findViewById(R.id.loginButton);
    }
}