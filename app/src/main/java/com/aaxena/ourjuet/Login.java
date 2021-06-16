package com.aaxena.ourjuet;

import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.lifecycle.ViewModelProviders;

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
            vibrateDeviceError();
            return false;
        }
        else if (mDob.getText().toString().isEmpty())
        {
            mDob.setError("Date of Birth is Required");
            vibrateDeviceError();
            return false;
        }
        else if (mPassword.getText().toString().isEmpty())
        {
            mPassword.setError("Password is Required");
            vibrateDeviceError();
            return false;
        }
        return true;
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
                            vibrateDeviceSuccess();
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
                            vibrateDeviceError();
                            break;
                        case NO_INTERNET:
                            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            vibrateDeviceError();
                            break;
                        case WEBKIOSK_DOWN:
                            Toast.makeText(this, "The JUET Servers are Not Responding", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            vibrateDeviceError();
                            break;
                        case FAILED:
                            Toast.makeText(this, "A Wild Wild Error Just Occurred!", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            vibrateDeviceError();
                            break;
                    }
                }
            });
        }
    }
    private void vibrateDeviceError() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,50,35,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    private void vibrateDeviceSuccess() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,70,38};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
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