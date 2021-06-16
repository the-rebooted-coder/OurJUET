package com.aaxena.ourjuet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText mEnrollment;
    EditText mPassword,mPrefferredPercentage, mDob;
    Button mLogin;
    LoginViewModel mLoginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    private void init() {
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mEnrollment = findViewById(R.id.enrNumber);
        mPassword = findViewById(R.id.password);
        mDob = findViewById(R.id.dob);

        mLogin = findViewById(R.id.loginButton);
        mLogin.setOnClickListener(this);
    }

    boolean validate() {
        if (mEnrollment.getText().toString().isEmpty())
        {
            mEnrollment.setError("Enrollment Number is Required");
            vibrateDeviceThird();
            return false;
        }
        else if (mDob.getText().toString().isEmpty())
        {
            mDob.setError("Date of Birth is Required");
            vibrateDeviceThird();
            return false;
        }
        else if (mPassword.getText().toString().isEmpty())
        {
            mPassword.setError("Password is Required");
            vibrateDeviceThird();
            return false;
        }
        return true;
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

    @Override
    public void onClick(View view) {
        if (view == mLogin && validate()){

            mLoginViewModel.loginUser(mEnrollment.getText().toString(),mPassword.getText().toString(), mDob.getText().toString()).observe(this,status -> {
                if (status != null) {
                    switch (status){
                        case LOADING:
                            showProgress();
                            break;
                        case SUCCESS:
                            Toast.makeText(this, "You just checked-in!", Toast.LENGTH_SHORT).show();
                      //      Intent refresh = new Intent(this,RefreshService.class);
                      //      refresh.putExtra("manual",true);
                      //      ActivityCompat.startForegroundService(this,refresh);
                      //      Intent intent = new Intent(this,DrawerActivity.class);
                      //      startActivity(intent);
                      //      finish();
                            dismissProgress();
                            break;
                        case WRONG_PASSWORD:
                            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            break;
                        case NO_INTERNET:
                            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            break;
                        case WEBKIOSK_DOWN:
                            Toast.makeText(this, "The JUET Servers are Not Responding", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            break;
                        case FAILED:
                            Toast.makeText(this, "A Wild Wild Error Just Occurred!", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            break;
                    }
                }
            });
        }
    }
    ProgressDialog dialog;
    void showProgress(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging In...");
        dialog.setProgressPercentFormat(null);
        dialog.setProgressNumberFormat(null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    void dismissProgress(){
        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();
    }
}