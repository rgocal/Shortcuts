package net.geekstools.floatshort;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class FloatingUnlimit extends Service {

    private WindowManager windowManager;

    boolean remove = false;
    boolean trans = false;

    Drawable appIcon;
    int iconColor, array;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId){
        array = intent.getIntExtra("array", 666);
        final String[] pack = new String[array];
        pack[startId] = intent.getStringExtra("pack");

        System.out.println(pack[startId]);
        if(appInstalledOrNot(pack[startId]) == false){stopSelf();	return 0;}

        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(pack[startId], PackageManager.GET_ACTIVITIES);
            ArrayList<ActivityInfo> packageActivities = new ArrayList<ActivityInfo>(Arrays.asList(pi.activities));

            for(int i = 0; i < pi.activities.length; i++) {
                ActivityInfo activityInfo = new ActivityInfo(pi.activities[i]);

                packageActivities.add(activityInfo);
                System.out.println("ActivityInfo >> " + activityInfo);
            }
        } catch (NameNotFoundException e) {}

        try {
            appIcon = manager.getApplicationIcon(pack[startId]);
        } catch (NameNotFoundException e1) {e1.printStackTrace();}

        int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
        if (API > 20) {
            Palette currentColor;
            final Drawable currentWallpaper = appIcon;
            final Bitmap bitmap = ((BitmapDrawable) currentWallpaper).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                currentColor = Palette.from(bitmap).generate();
            }
            else{
                Bitmap bitmapTemp = BitmapFactory.decodeResource(getResources(), R.drawable.marsala);
                currentColor = Palette.from(bitmapTemp).generate();
            }

            try{
                int defaultColor = getResources().getColor(R.color.default_color);

                iconColor = currentColor.getVibrantColor(defaultColor);
            }
            catch (NullPointerException e){
                System.out.println(e);

                iconColor = getResources().getColor(R.color.default_color);
            }
        }

        if(MainScope.hide == true){
            trans = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    appIcon.setAlpha(MainScope.alpha);
                }
            }, 2000);
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final ImageView[] floatingView = new ImageView[array];
        floatingView[startId] = new ImageView(this);
        floatingView[startId].setBackground(appIcon);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                MainScope.HW,
                MainScope.HW,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        params.windowAnimations = android.R.style.Animation_Dialog;

        windowManager.addView(floatingView[startId], params);
        floatingView[startId].setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //
            }
        });

        floatingView[startId].setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams paramsF = params;
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(trans == true){
                    appIcon.setAlpha(MainScope.opacity);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appIcon.setAlpha(MainScope.alpha);
                        }
                    }, 3000);
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("Touch ACTION_DOWN");

                        initialX = paramsF.x;
                        initialY = paramsF.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("Touch ACTION_UP");

                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("Touch ACTION_MOVE");

                        paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                        try{windowManager.updateViewLayout(floatingView[startId], paramsF);}
                        catch (IllegalArgumentException e){windowManager.addView(floatingView[startId], paramsF);}
                        break;
                }
                return false;
            }
        });
        floatingView[startId].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(remove == true){
                        if(floatingView[startId] == null){return;}
                        if (floatingView[startId].isShown()) {
                            windowManager.removeView(floatingView[startId]);
                            stopSelf(startId);
                        }
                    }
                    else{
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        PackageManager manager = getPackageManager();
                        i = manager.getLaunchIntentForPackage(pack[startId]);
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
        });
        floatingView[startId].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remove = true;
                    LayerDrawable drawPref = (LayerDrawable)getResources().getDrawable(R.drawable.draw_close_service);
                    GradientDrawable backPref = (GradientDrawable) drawPref.findDrawableByLayerId(R.id.backtemp);
                    backPref.setColor(iconColor);
                    floatingView[startId].setImageDrawable(drawPref);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(remove == true){
                                remove = false;
                                floatingView[startId].setImageDrawable(null);
                            }
                        }
                    }, 1500);

                    return true;
                }
        });

        int ReturnValua = 0;
        if(MainScope.Return == false){ReturnValua = Service.START_NOT_STICKY;}
        else if(MainScope.Return == true){ReturnValua = Service.START_REDELIVER_INTENT;}
        return ReturnValua;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println(getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println(getClass().getName() + ":::Reset");
    }

    /****************************/
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
