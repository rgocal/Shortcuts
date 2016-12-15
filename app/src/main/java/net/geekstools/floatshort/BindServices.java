package net.geekstools.floatshort;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.RemoteViews;

public class BindServices extends Service{

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        startForeground(666, bindService());

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected Notification bindService(){
        Notification.Builder  mBuilder = new Notification.Builder(this);

        RemoteViews remoteNotfication = new RemoteViews(getPackageName(), R.layout.notification);
        mBuilder.setContent(remoteNotfication);

        Intent CancelStable = new Intent(this, CancelStableTemp.class);
        PendingIntent cancelPending = PendingIntent.getActivity(this, 0, CancelStable, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteNotfication.setOnClickPendingIntent(R.id.cancelStable, cancelPending);

        mBuilder.setContentTitle(getResources().getString(R.string.app_name));
        mBuilder.setContentText(getResources().getString(R.string.bindDesc));
        mBuilder.setTicker(getResources().getString(R.string.app_name));
        mBuilder.setSmallIcon(R.drawable.ic_notification);

        Resources res = getResources();
        Bitmap bM = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
        mBuilder.setLargeIcon(bM);
        mBuilder.setAutoCancel(false);

        Intent Main = new Intent(this, ListGrid.class);
        PendingIntent mainPV = PendingIntent.getActivity(this, 0, Main, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent settingGUI = new Intent(this, SettingGUI.class);
        PendingIntent pV = PendingIntent.getActivity(this, 0, settingGUI, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent set = new Intent(this, StopAll.class);
        PendingIntent pS = PendingIntent.getActivity(this, 0, set, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent setR = new Intent(this, RecoveryShortcuts.class);
        PendingIntent pR = PendingIntent.getActivity(this, 0, setR, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(0, "Recover ALL", pR);
        mBuilder.addAction(0, "Remove ALL", pS);
        mBuilder.addAction(0, "Settings", pV);
        //mBuilder.addAction(R.drawable.ic_cancel_stable, " ", cancelPending);
        mBuilder.setContentIntent(mainPV);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return mBuilder.build();
    }
}
