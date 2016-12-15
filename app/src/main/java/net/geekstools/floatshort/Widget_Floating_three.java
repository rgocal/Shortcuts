package net.geekstools.floatshort;

import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Widget_Floating_three extends Service{

    WindowManager windowManager;
    Context context;

    ViewGroup vG, widgetLayout;
    RelativeLayout wholeViewWidget;
    AppWidgetHostView hostView;

    TextView label;
    ImageView icon;
    Button close;

    int initX = 50;
    int initY = 50;
    int H = 130, W = 320, P = 3, M = 30;

    int appWidgetId;
    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;

    boolean trans = false;

    Palette currentColor;
    int widgetCloseColor;

    SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        appWidgetId = intent.getIntExtra("widgetid", -1);
        System.out.println("Floating WidgetID: " + appWidgetId);
        final AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        //Define View
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vG = (ViewGroup)layoutInflater.inflate(R.layout.widget_floating, null, false);

        label = (TextView)vG.findViewById(R.id.label);
        icon = (ImageView)vG.findViewById(R.id.icon);
        close = (Button)vG.findViewById(R.id.close);

        wholeViewWidget = (RelativeLayout)vG.findViewById(R.id.whole_widget_view);
        widgetLayout = (ViewGroup) vG.findViewById(R.id.widget);

        label.setText(appWidgetInfo.loadLabel(getPackageManager()));
        icon.setImageDrawable(appWidgetInfo.loadIcon(getApplicationContext(), DisplayMetrics.DENSITY_DEFAULT));

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

        hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        System.out.println(">>  minResizeHeight: " + appWidgetInfo.minResizeHeight +
                " >> minResizeWidth: " + appWidgetInfo.minResizeWidth);

        H = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.widgetH, this.getResources().getDisplayMetrics());
        W = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.widgetW, this.getResources().getDisplayMetrics());
        P = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, this.getResources().getDisplayMetrics());
        M = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, M, this.getResources().getDisplayMetrics());

        if(MainScope.widgetW == 1 && MainScope.widgetH == 1){
            H = appWidgetInfo.minHeight;
            W = appWidgetInfo.minWidth;
        }

        H = H + M;
        System.out.println("W*H -- M >> " + W + "*" + H + " -- " + M);
        hostView.setMinimumHeight(H);
        hostView.setMinimumWidth(W);

        final RelativeLayout.LayoutParams widgetRelativeLayout = new RelativeLayout.LayoutParams(W, H);
        wholeViewWidget.setPadding(P, P, P, P);
        wholeViewWidget.setElevation(19);
        wholeViewWidget.setLayoutParams(widgetRelativeLayout);
        wholeViewWidget.requestLayout();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                W,
                H,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 50;
        params.y = 50;
        params.windowAnimations = android.R.style.Animation_Dialog;

        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        widgetLayout.addView(hostView);
        windowManager.addView(vG, params);

        try{
            label.setOnTouchListener(new View.OnTouchListener() {
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
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}});

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    PackageManager manager = getPackageManager();
                    i = manager.getLaunchIntentForPackage(appWidgetInfo.provider.getPackageName());
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent m = new Intent(getApplicationContext(), Widget_Mini_three.class);
                            m.putExtra("widgetid", appWidgetId);
                            m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startService(m);
                        }
                    }, 250);
                }
            });

            close.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    stopSelf();
                    return false;
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

        System.out.println("Widget ONE");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vG.isShown()) {
            windowManager.removeView(vG);
            mAppWidgetHost.stopListening();

            System.out.println("Channel Widget ONE Reset");
        }
    }

    //Required Installed Check
    private boolean appInstalledOrNot(String packName) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
