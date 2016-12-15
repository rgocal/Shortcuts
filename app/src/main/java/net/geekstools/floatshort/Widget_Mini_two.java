package net.geekstools.floatshort;

import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class Widget_Mini_two extends Service{

    WindowManager windowManager;
    Context context;

    ViewGroup vG;
    ImageView icon;
    Button close, move;

    int appWidgetId;
    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;

    Palette currentColor;
    int widgetCloseColor;

    SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appWidgetId = intent.getIntExtra("widgetid", -1);
        System.out.println("Floating WidgetID: " + appWidgetId);
        final AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        //Define View
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vG = (ViewGroup)layoutInflater.inflate(R.layout.widget_floating_mini, null, false);

        icon = (ImageView)vG.findViewById(R.id.icon);
        close = (Button)vG.findViewById(R.id.close);
        move = (Button)vG.findViewById(R.id.move);

        widgetCloseColor = getResources().getColor(R.color.default_color);
        int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
        if(API > 20) {
            final Drawable currentWallpaper = appWidgetInfo.loadIcon(getApplicationContext(), DisplayMetrics.DENSITY_DEFAULT);
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

                widgetCloseColor = currentColor.getVibrantColor(defaultColor);			System.out.println("widgetCloseColor Vibrant >> " + widgetCloseColor);
            }
            catch (NullPointerException e){
                System.out.println(e);

                widgetCloseColor = getResources().getColor(R.color.default_color);		System.out.println("widgetCloseColor Vibrant >> " + widgetCloseColor);
            }
        }

        LayerDrawable drawWidget = (LayerDrawable)getResources().getDrawable(R.drawable.draw_close);
        GradientDrawable backWidget = (GradientDrawable) drawWidget.findDrawableByLayerId(R.id.backtemp);
        backWidget.setColor(widgetCloseColor);
        close.setBackground(getResources().getDrawable(R.drawable.draw_close));

        LayerDrawable drawWidgetMove = (LayerDrawable)getResources().getDrawable(R.drawable.draw_move);
        GradientDrawable backWidgetMove = (GradientDrawable) drawWidgetMove.findDrawableByLayerId(R.id.backtemp);
        backWidgetMove.setColor(widgetCloseColor);
        move.setBackground(getResources().getDrawable(R.drawable.draw_move));

        LayerDrawable drawWidgetIcon = (LayerDrawable)getResources().getDrawable(R.drawable.draw_widget_mini);
        GradientDrawable backWidgetIcon = (GradientDrawable) drawWidgetIcon.findDrawableByLayerId(R.id.backtrans);
        icon.setBackground(appWidgetInfo.loadIcon(getApplicationContext(), DisplayMetrics.DENSITY_XXXHIGH));

        int HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 88, this.getResources().getDisplayMetrics());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                HW,
                HW,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 50;
        params.y = 50;
        params.windowAnimations = android.R.style.Animation_Dialog;

        windowManager.addView(vG, params);
        try{
            move.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}});
            move.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsF = params;
                int initialX;
                int initialY;
                float initialTouchX;
                float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
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
                            try{
                                windowManager.updateViewLayout(vG, paramsF);
                            }
                            catch (IllegalArgumentException e){
                                windowManager.addView(vG, paramsF);
                            }
                            break;
                    }
                    return false;
                }
            });
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent m = new Intent(getApplicationContext(), Widget_Floating_two.class);
                            m.putExtra("widgetid", appWidgetId);
                            m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(m);
                        }
                    }, 250);
                }
            });
            icon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {return false;}});

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();
                }
            });
        }
        catch (Exception e){
            System.out.println(e);
        }

        int ReturnValua = 0;
        if(MainScope.Return == false){
            ReturnValua = Service.START_NOT_STICKY;
        }
        else if(MainScope.Return == true){
            ReturnValua = Service.START_REDELIVER_INTENT;
        }
        System.out.println("Return >> " + ReturnValua);
        return ReturnValua;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MainScope.oneW = true;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);

        mAppWidgetHost.startListening();

        System.out.println("Widget Mini ONE");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vG.isShown()) {
            windowManager.removeView(vG);
            mAppWidgetHost.stopListening();

            System.out.println("Channel Widget Mini ONE Reset");
        }
    }
}
