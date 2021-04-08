package com.aaxena.ourjuet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void validate() {
        if (enr.getText().toString().isEmpty())
        {
         enr.setError("Enrollment Number is Required",getDrawable(R.drawable.ic_baseline_error_outline_24));
            vibrateDeviceThird();
        }
        else if (password.getText().toString().isEmpty())
        {
         password.setError("Password is Required",getDrawable(R.drawable.ic_baseline_error_outline_24));
            vibrateDeviceThird();
        }
        else if (dob.getText().toString().isEmpty())
        {
         dob.setError("Date of Birth is Required",getDrawable(R.drawable.ic_baseline_error_outline_24));
            vibrateDeviceThird();
        }
        else
        {
            Toast.makeText(this,"Doing",Toast.LENGTH_SHORT).show();
        }
    }
    private void vibrateDeviceThird() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(36, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(28);
        }
    }
}