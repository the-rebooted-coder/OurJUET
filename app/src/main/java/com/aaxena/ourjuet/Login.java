package com.aaxena.ourjuet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText mEnrollment;
    EditText mPassword,mPrefferredPercentage, mDob;
    Button mLogin;
    LoginViewModel mLoginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

     void init() {
         mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
         mEnrollment = findViewById(R.id.input_enrollment);
         mPassword = findViewById(R.id.input_password);
         mDob = findViewById(R.id.dob);
         mPrefferredPercentage = findViewById(R.id.input_attendence);
         mLogin = findViewById(R.id.btn_login);
         mLogin.setOnClickListener(this);
         mDob.setOnClickListener(this);
    }
    boolean validate(){
        if (mEnrollment.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enrollment is Required", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (mPrefferredPercentage.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preferred Percentage is Required", Toast.LENGTH_SHORT).show();

            return false;

        }
        if (mPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Password is Required", Toast.LENGTH_SHORT).show();

            return false;

        }
        if (mDob.getText().toString().isEmpty()) {
            Toast.makeText(this, "DOB Percentage is Required", Toast.LENGTH_SHORT).show();

            return false;

        }
        return true;
    }

    String getHostOfInst(String code) {
        if ("JUET".equals(code)) {
            SharedPreferencesUtil.getInstance(this).savePreferences(INSTITUTE_PROTOCOL, "https://");
            return "webkiosk.juet.ac.in";
        }
        SharedPreferencesUtil.getInstance(this).savePreferences(INSTITUTE_PROTOCOL, "https://");
        return "webkiosk.juet.ac.in";
    }

    @Override
    public void onClick(View view) {
        if (view == mLogin && validate()){
            String instCode = mInst.getSelectedItem().toString();
            SharedPreferencesUtil.getInstance(this).savePreferences(INSTITUTE_CODE, instCode);
            SharedPreferencesUtil.getInstance(this).savePreferences(INSTITUTE_URL, getHostOfInst(instCode));

            mLoginViewModel.loginUser(mEnrollment.getText().toString(),mPassword.getText().toString(), mDob.getText().toString()).observe(this,status -> {
                if (status != null) {
                    switch (status){
                        case LOADING:
                            showProgress();
                            break;
                        case SUCCESS:
                            SharedPreferences.Editor editor = this.getSharedPreferences(this.getString(R.string.preferencefile), Context.MODE_PRIVATE).edit();
                            editor.putString(getString(R.string.key_preferred_attendence), mPrefferredPercentage.getText().toString());
                            editor.putBoolean("autosync", true);
                            SharedPreferencesUtil.getInstance(this).savePreferences("dark",true);
                            editor.apply();
//                            createShortcuts();
                            Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            Intent refresh = new Intent(this,RefreshService.class);
                            refresh.putExtra("manual",true);
                            ActivityCompat.startForegroundService(this,refresh);
                            Intent intent = new Intent(this,DrawerActivity.class);
                            startActivity(intent);
                            finish();
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
                            Toast.makeText(this, "Webkiosk Not Responding", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            break;
                        case FAILED:
                            Toast.makeText(this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                            dismissProgress();
                            break;
                    }
                }
            });
        }
        else if(view== mDob){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog dialog = new DatePickerDialog(this);
                dialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                    mDob.setText(String.format("%02d", dayOfMonth)+"-"+String.format("%02d", month+1)+"-"+year);
                    dialog.dismiss();
                });
                dialog.show();
            }else {
                mDob.setFocusable(true);
                mDob.requestFocus();
            }

        }
    }
    ProgressDialog dialog;
    void showProgress(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging You In...");
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
