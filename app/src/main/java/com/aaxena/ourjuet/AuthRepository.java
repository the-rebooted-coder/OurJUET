package com.aaxena.ourjuet;

import android.content.Context;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jsoup.Connection;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.aaxena.ourjuet.webUtilities.login;

@Singleton
public class AuthRepository {
    private static AuthRepository mInstance;
    private AppExecutors mAppExecutors = AppExecutors.newInstance();
    Map<String, String> loginCookies = null;
    SharedPreferencesUtil mSharedPreferencesUtil;
    @Inject
    public AuthRepository(SharedPreferencesUtil sharedPreferencesUtil) {
        this.mSharedPreferencesUtil = sharedPreferencesUtil;
    }

    public static AuthRepository getInstance(){
        if (mInstance == null){
            mInstance = new AuthRepository(null);
        }
        return mInstance;
    }
    public LiveData<Constants.Status> loginUser(Context context){
        MutableLiveData<Constants.Status> mLoginStatus = new MutableLiveData<>();
        mLoginStatus.setValue(Constants.Status.LOADING);
        if (mSharedPreferencesUtil == null){
            mSharedPreferencesUtil = SharedPreferencesUtil.getInstance(context.getApplicationContext());
        }
        String user = mSharedPreferencesUtil.getPreferences(Constants.ENROLLMENT, "").toUpperCase().trim();
        String pass = mSharedPreferencesUtil.getPreferences(Constants.PASSWORD, "");
        String dob = mSharedPreferencesUtil.getPreferences(Constants.DOB, "");

        mAppExecutors.networkIO().execute(() -> {
            if (!webUtilities.isConnected(context)){
                mAppExecutors.mainThread().execute(()-> {
                    mLoginStatus.setValue(Constants.Status.NO_INTERNET);
                }) ;
            }
            else if (!RefreshService.pingHost(Constants.PING_HOST, 80, 5000)) {
                mAppExecutors.mainThread().execute(()-> {
                    mLoginStatus.setValue(Constants.Status.WEBKIOSK_DOWN);
                });
            }else
                try {
                    Pair<Connection.Response, Connection.Response> res = login(user, dob, pass);
                    Runnable wrongPasswordRunnable = () -> {
                        mLoginStatus.setValue(Constants.Status.WRONG_PASSWORD);
                    };
                    if(res.second.body().contains("Invalid Password")){
                        loginCookies = null;
                        mAppExecutors.mainThread().execute(wrongPasswordRunnable);
                    }else  if(res.second.body().contains("Invalid Login Credentials")){
                        loginCookies = null;
                        mAppExecutors.mainThread().execute(wrongPasswordRunnable);
                    }else {
                        Runnable runnable = () -> {
                            mLoginStatus.setValue(Constants.Status.SUCCESS);
                        };
                            loginCookies = res.first.cookies();
                            mAppExecutors.mainThread().execute(runnable);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    mAppExecutors.mainThread().execute(()-> {
                        mLoginStatus.setValue(Constants.Status.FAILED);
                    });
                }
        });


        return mLoginStatus;
    }

    public Map<String, String> getLoginCookies() {
        return loginCookies;
    }

    public void setLoginCookies(Map<String, String> loginCookies) {
        this.loginCookies = loginCookies;
    }
}
