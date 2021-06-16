package com.aaxena.ourjuet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.aaxena.ourjuet.webUtilities.isConnected;


public class RefreshService extends LifecycleService {
    AttendenceViewModel mAttendenceViewModel;
    @Inject
    AttendenceRepository mAttendenceRepository;
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "my_service";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.GREEN);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }
    void startForeground() {
        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            // If earlier version channel ID is not used
            channelId = "";
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setOngoing(true)
                .setContentTitle("Connecting with JUET Servers")
                .setContentText("We will be done in a moment!")
                .setSmallIcon(R.drawable.sync);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationBuilder.setCategory(Notification.CATEGORY_SERVICE);
        Notification notification = notificationBuilder.build();
        startForeground(101, notification);

    }
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
        startForeground();
    }

    public static boolean pingHost(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
    void handleStatusCode(Constants.Status status){
            switch (status){
                case LOADING:
                    Toast.makeText(this,"Syncing with JUET Servers",Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    Toast.makeText(this,"Sync Complete!",Toast.LENGTH_SHORT).show();
                    stopSelf();
                    break;
                case WRONG_PASSWORD:
                    Toast.makeText(this,"Your Credentials Seem to be Wrong :(",Toast.LENGTH_SHORT).show();
                    stopSelf();
                    break;
                case NO_INTERNET:
                    Toast.makeText(this,"Hmm...OurJUET is not talking with the Internet",Toast.LENGTH_SHORT).show();
                    stopSelf();
                    break;
                case WEBKIOSK_DOWN:
                    Toast.makeText(this,"The JUET Servers are Not Responding",Toast.LENGTH_SHORT).show();
                    stopSelf();
                    break;
                case FAILED:
                    Toast.makeText(this,"A Wild Wild Error Just Occurred!",Toast.LENGTH_SHORT).show();
                    stopSelf();
                    break;
                case LOGGED_IN:
                    Toast.makeText(this, "You just checked-in!", Toast.LENGTH_SHORT).show();
                    break;
            }
    }
    String DateString;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            boolean isConnected = isConnected(this);
            DateString = new String();
            Boolean today = false;
            SharedPreferences prefs = getSharedPreferences(getString(R.string.preferencefile), Context.MODE_PRIVATE);

            if (!intent.getBooleanExtra("manual", false)) {
                DateString = prefs.getString(Constants.DATE, null);
                Date dateobj = new Date();
                SimpleDateFormat formattor = new SimpleDateFormat("dd/MMM HH:mm", Locale.getDefault());
                String temp = formattor.format(dateobj);
                temp = temp.substring(0, temp.indexOf(" "));
                if (DateString != null && DateString.substring(0, DateString.indexOf(" ")).equals(temp)) {
                    today = true;
                }
            }
            String user = prefs.getString(getString(R.string.key_enrollment), "").toUpperCase().trim();
            String pass = prefs.getString(getString(R.string.key_password), "");
            boolean temp = prefs.getBoolean("autosync", true) || intent.getBooleanExtra("manual", false);
            if ((!user.equals("") || !pass.equals("")) && !today && isConnected && temp) {
                sendNotification("Please Enter Login Details", 0, false);
                mAttendenceRepository.loadData().observe(this, this::handleStatusCode);
            } else if (user.equals("") || pass.equals("")) {
                stopSelf();
            } else if (today || !isConnected) {
                stopSelf();
            } else {
                sendNotification("Wrong Credentials", 0, false);
                stopSelf();
            }
            return START_STICKY;
        }
        return START_NOT_STICKY;

    }
    private void sendNotification(String msg, int i, boolean onGoing) {
        PendingIntent contentIntent;
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (i == 1) {
         //   contentIntent = PendingIntent.getActivity(getApplicationContext(), 55,
                //    new Intent(getApplicationContext(), DrawerActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
           // contentIntent = PendingIntent.getActivity(getApplicationContext(), 55,
           //         new Intent(getApplicationContext(), SettingsActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            // If earlier version channel ID is not used
            channelId = "";
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(),channelId)
                        .setContentTitle("Attendance")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.sync)
                        .setAutoCancel(true);
        if (onGoing)
            mBuilder.setOngoing(true);
     //   mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(105, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        AppDatabase.newInstance(this).AttendenceDao().updateLoading(false);
        super.onDestroy();
    }

}
