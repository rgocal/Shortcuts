package net.geekstools.floatshort;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CloudNotificationHandler extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(">>> ", "From: " + remoteMessage.getFrom());
        Log.d(">>> ", "Notification Message Body: " + remoteMessage.getNotification().getBody());

        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }

    private void sendNotification(String messageBody, String messageTitle) {
        Intent app = new Intent(this, ListGrid.class);
        app.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingApp = PendingIntent.getActivity(this, 0, app, PendingIntent.FLAG_ONE_SHOT);

        Intent promot = new Intent(this, DeepLinkedPromotHandler.class);
        promot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingPromot = PendingIntent.getActivity(this, 0, promot, PendingIntent.FLAG_ONE_SHOT);

        Intent share = new Intent(this, DetailHelper.class);
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingShare = PendingIntent.getActivity(this, 0, share, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingApp);

    //    notificationBuilder.addAction(0, "Promotions", pendingPromot);
        notificationBuilder.addAction(0, "Features Detail", pendingShare);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(333, notificationBuilder.build());
    }
}
