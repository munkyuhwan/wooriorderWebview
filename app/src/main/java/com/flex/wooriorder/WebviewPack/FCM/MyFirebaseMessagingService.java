package com.flex.wooriorder.WebviewPack.FCM;
/*
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.flex.wooriorder.MainActivity;
import com.flex.wooriorder.R;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println(" === === === === === === === === === ===received message === === === === === === === === === ===" );

        if (remoteMessage.getData().size() > 0) {
            System.out.println("received message : " + remoteMessage.getData());

            Map<String, String> bundle = remoteMessage.getData();
            System.out.println("received message getData : " + bundle.toString());
        }

        if (remoteMessage.getNotification() != null) {
            System.out.println("received message getNotification: " + remoteMessage.getNotification().getBody());
        }

        sendPushNotification(remoteMessage);

    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

     sendRegistrationToServer(token);
    }


    private void scheduleJob() {


    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
    private void sendPushNotification(RemoteMessage remoteMessage) {

        System.out.println("sendPushNotification..........................");
        System.out.println(remoteMessage.getData());
        String url = remoteMessage.getData().get("link");
        System.out.println("received message url=[" + url + "]=-------------------");
        // String logmsg = (url == null || url.length() <= 0) ? "NO URL in msg" : "OK URL in msg";
        // Log.d(TAG, logmsg);


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("type", "notification");
        intent.putExtra("link", url);

        // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                (int)System.currentTimeMillis(), // ,
                // (int)(System.currentTimeMillis()/1000), // ,
                intent,
                PendingIntent.FLAG_MUTABLE); // FLAG_CANCEL_CURRENT); //FLAG_ONE_SHOT); FLAG_MUTABLE  FLAG_IMMUTABLE
        // PendingIntent.FLAG_CANCEL_CURRENT); // FLAG_CANCEL_CURRENT); //FLAG_ONE_SHOT);


        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                getResources().getClass().getName());
        wakelock.acquire(500);

        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("version sdk: "," version sdk higher than Oreo");
            String channelId = getString(R.string.default_notification_channel_id);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL);

            //--- 시작 (notification 에 이미지 표시)------------------------------------------------------------------------------------------//
            // from ==> https://team-platform.tistory.com/27
            Uri uri_image = remoteMessage.getNotification().getImageUrl();

            if (uri_image != null) {
                try {
                    URL url_image = new URL(uri_image.toString());
                    Bitmap bitmap = BitmapFactory.decodeStream(url_image.openConnection().getInputStream());
                    notificationBuilder.setLargeIcon(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //--- 끝  (notification 에 이미지 표시)------------------------------------------------------------------------------------------//
            Log.e("notificaion: "," display notification!!!!");

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // register channel
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    "puding_client",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(serviceChannel);
            startForeground((int)System.currentTimeMillis(), notificationBuilder.build());


            //notificationManager.notify((int)System.currentTimeMillis(), notificationBuilder.build());

            return;
        }
        Log.e("version sdk: "," version sdk lower than Oreo");

        //Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
            //notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(
                (int)System.currentTimeMillis(),//9999,
                notificationBuilder.build());// ID of notification
    }

}

*/