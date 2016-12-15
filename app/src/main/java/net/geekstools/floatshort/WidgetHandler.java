package net.geekstools.floatshort;

import android.app.ActionBar;
import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WidgetHandler extends Activity{

    Bundle extras;

    int themeColor, themeTextColor, widgetLaunchColor;
    String themColorString;
    Palette currentColor;

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;

    RelativeLayout whole, widget, widget1, widget2, widget3;
    ImageButton showWidget;

    Button launchWidget1, launchWidget2, launchWidget3;
    TextView widgetLable1, widgetLable2, widgetLable3;
    ImageView icon1, icon2, icon3;

    int widgetWidth = 320, widgetHeight = 130;

    String sharedPrefNum;
    int serviceNum1, serviceNum2, serviceNum3;

    int id1, id2, id3;
    String pack1, pack2, pack3;
    String tag;

    @Override
    protected void onCreate(Bundle saved){
        super.onCreate(saved);
        setContentView(R.layout.widget_handler);

        widgetLaunchColor = themeColor = getResources().getColor(R.color.default_color);

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, 666);

        whole = (RelativeLayout)findViewById(R.id.whole);
        showWidget = (ImageButton)findViewById(R.id.showWidget);

        if(appInstalledOrNot("net.geekstools.geekylauncher") == false){
            ActionBar actBar = getActionBar();
            actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.app_name) + "</font>"));
            actBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            whole.setBackgroundColor(Color.WHITE);

            int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
            if(API > 20){
                final Drawable currentWallpaper = getWallpaper();
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

                    themeColor = currentColor.getVibrantColor(defaultColor);			System.out.println("themeColor Vibrant >> " + themeColor);
                    themeTextColor = currentColor.getDarkVibrantSwatch().getRgb();		System.out.println("themeTextColor getDarkVibrantSwatch >> " + themeTextColor);
                    themColorString = "#" + Integer.toHexString(currentColor.getDarkVibrantSwatch().getRgb()).substring(2);
                }
                catch (NullPointerException e){
                    System.out.println(e);

                    themeColor = getResources().getColor(R.color.default_color);		System.out.println("themeColor Vibrant >> " + themeColor);
                    themeTextColor = getResources().getColor(R.color.default_color);	System.out.println("themeTextColor getDarkVibrantSwatch >> " + themeTextColor);
                    themColorString = "" + getResources().getColor(R.color.default_color);
                }

                actBar.setTitle(Html.fromHtml("<font color='" + themColorString + "'>" + getResources().getString(R.string.app_name) + "</font>"));

                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(themeColor);
                getWindow().setNavigationBarColor(themeColor);
            }
        }
        else{
            getActionBar().hide();
            int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
            if(API > 20){
                final Drawable currentWallpaper = getWallpaper();
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

                    themeColor = currentColor.getVibrantColor(defaultColor);			System.out.println("themeColor Vibrant >> " + themeColor);
                    themeTextColor = currentColor.getDarkVibrantSwatch().getRgb();		System.out.println("themeTextColor getDarkVibrantSwatch >> " + themeTextColor);
                    themColorString = "#" + Integer.toHexString(currentColor.getDarkVibrantSwatch().getRgb()).substring(2);
                }
                catch (NullPointerException e){
                    System.out.println(e);

                    themeColor = getResources().getColor(R.color.default_color);		System.out.println("themeColor Vibrant >> " + themeColor);
                    themeTextColor = getResources().getColor(R.color.default_color);	System.out.println("themeTextColor getDarkVibrantSwatch >> " + themeTextColor);
                    themColorString = "" + getResources().getColor(R.color.default_color);
                }

                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.parseColor("#80000000"));
                getWindow().setNavigationBarColor(Color.parseColor("#80000000"));
            }
        }

        showWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWidget();
            }
        });
    }

    //Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

        tag = v.getTag().toString();

        if(tag.contains("launch")){
            String[] menuItems = getResources().getStringArray(R.array.MOD);
            for (int I = 0; I < menuItems.length; I++) {
                menu.add(Menu.NONE, I, I, menuItems[I]);
            }
        }
        else{
            String[] menuItems = getResources().getStringArray(R.array.ContextMenuRemove);
            for (int I = 0; I < menuItems.length; I++) {
                menu.add(Menu.NONE, I, I, menuItems[I]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if(tag.contains("launch")){
            int menuItemIndex = item.getItemId();
            String[] menuItems = getResources().getStringArray(R.array.MOD);
            String menuItemName = menuItems[menuItemIndex];

            if(menuItemName.equals("Small")){
                MainScope.widgetH = 1;
                MainScope.widgetW = 1;

                if(tag.contains("1")){
                    startWidgetService(id1, serviceNum1);
                }
                else if(tag.contains("2")){
                    startWidgetService(id2, serviceNum2);
                }
                else if(tag.contains("3")){
                    startWidgetService(id3, serviceNum3);
                }
            }
            else if(menuItemName.equals("Medium")){
                MainScope.widgetH = 130;
                MainScope.widgetW = 320;

                if(tag.contains("1")){
                    startWidgetService(id1, serviceNum1);
                }
                else if(tag.contains("2")){
                    startWidgetService(id2, serviceNum2);
                }
                else if(tag.contains("3")){
                    startWidgetService(id3, serviceNum3);
                }
            }
            else if(menuItemName.equals("Large")){
                MainScope.widgetH = 300;
                MainScope.widgetW = 320;

                if(tag.contains("1")){
                    startWidgetService(id1, serviceNum1);
                }
                else if(tag.contains("2")){
                    startWidgetService(id2, serviceNum2);
                }
                else if(tag.contains("3")){
                    startWidgetService(id3, serviceNum3);
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Shortcuts Size
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String s = sharedPrefs.getString("sizes", "2");
                    if(s.equals("1")){
                        MainScope.widgetH = 1;
                        MainScope.widgetW = 1;
                    }
                    else if(s.equals("2")){
                        MainScope.widgetH = 130;
                        MainScope.widgetW = 320;
                    }
                    else if(s.equals("3")){
                        MainScope.widgetH = 300;
                        MainScope.widgetW = 320;
                    }
                }
            }, 750);
        }
        else{
            int menuItemIndex = item.getItemId();
            String[] menuItems = getResources().getStringArray(R.array.ContextMenuRemove);
            String menuItemName = menuItems[menuItemIndex];

            int tagNum = Integer.parseInt(tag);
            switch (tagNum){
                case 1:
                    MainScope.oneW = false;
                    SharedPreferences sharedPreferences1 = getSharedPreferences("WidgetInfo1", Context.MODE_PRIVATE);
                    int appWidgetId1 = sharedPreferences1.getInt("id", -1);
                    AppWidgetProviderInfo appWidgetInfo1 = mAppWidgetManager.getAppWidgetInfo(appWidgetId1);
                    AppWidgetHostView hostView1 = mAppWidgetHost.createView(this, appWidgetId1, appWidgetInfo1);
                    removeWidget(hostView1, widget1, appWidgetId1);
                    saveWidgetInfo(tag, -1, getApplicationContext().getPackageName(), getApplicationContext().getPackageName());

                    icon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                    widgetLable1.setText(getResources().getString(R.string.app_name));

                    break;
                case 2:
                    MainScope.twoW = false;
                    SharedPreferences sharedPreferences2 = getSharedPreferences("WidgetInfo2", Context.MODE_PRIVATE);
                    int appWidgetId2 = sharedPreferences2.getInt("id", -1);
                    AppWidgetProviderInfo appWidgetInfo2 = mAppWidgetManager.getAppWidgetInfo(appWidgetId2);
                    AppWidgetHostView hostView2 = mAppWidgetHost.createView(this, appWidgetId2, appWidgetInfo2);
                    removeWidget(hostView2, widget2, appWidgetId2);
                    saveWidgetInfo(tag, -1, getApplicationContext().getPackageName(), getApplicationContext().getPackageName());

                    icon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                    widgetLable2.setText(getResources().getString(R.string.app_name));

                    break;
                case 3:
                    MainScope.threeW = false;
                    SharedPreferences sharedPreferences3 = getSharedPreferences("WidgetInfo3", Context.MODE_PRIVATE);
                    int appWidgetId3 = sharedPreferences3.getInt("id", -1);
                    AppWidgetProviderInfo appWidgetInfo3 = mAppWidgetManager.getAppWidgetInfo(appWidgetId3);
                    AppWidgetHostView hostView3 = mAppWidgetHost.createView(this, appWidgetId3, appWidgetInfo3);
                    removeWidget(hostView3, widget3, appWidgetId3);
                    saveWidgetInfo(tag, -1, getApplicationContext().getPackageName(), getApplicationContext().getPackageName());

                    icon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                    widgetLable3.setText(getResources().getString(R.string.app_name));

                    break;
            }
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_scope, menu);

        MenuItem settings = menu.findItem(R.id.pref);
        MenuItem recov = menu.findItem(R.id.recov);
        MenuItem recovw = menu.findItem(R.id.recovw);

        LayerDrawable drawSettings = (LayerDrawable)getResources().getDrawable(R.drawable.draw_settings);
        GradientDrawable backSettings = (GradientDrawable) drawSettings.findDrawableByLayerId(R.id.backtemp);

        LayerDrawable drawRecov = (LayerDrawable)getResources().getDrawable(R.drawable.draw_recov);
        GradientDrawable backRecov = (GradientDrawable) drawRecov.findDrawableByLayerId(R.id.backtemp);

        LayerDrawable drawAbout = (LayerDrawable)getResources().getDrawable(R.drawable.draw_recovw);
        GradientDrawable backAbout = (GradientDrawable) drawAbout.findDrawableByLayerId(R.id.backtemp);

        backSettings.setColor(themeColor);
        backRecov.setColor(themeColor);
        backAbout.setColor(themeColor);

        settings.setIcon(drawSettings);
        recov.setIcon(drawRecov);
        recovw.setIcon(drawAbout);

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.recovw: {
                startActivity(new Intent(getApplicationContext(), RecoveryWidgets.class));

                break;
            }
            case R.id.pref:{
                Intent s = new Intent(getApplicationContext(), SettingGUI.class);
                startActivity(s);

                finish();
                break;
            }
            case R.id.recov:{
                startActivity(new Intent(getApplicationContext(), RecoveryShortcuts.class));

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);

        //Shortcuts Size
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String s = sharedPrefs.getString("sizes", "2");
        if(s.equals("1")){
            MainScope.widgetH = 1;
            MainScope.widgetW = 1;
        }
        else if(s.equals("2")){
            MainScope.widgetH = 130;
            MainScope.widgetW = 320;
        }
        else if(s.equals("3")){
            MainScope.widgetH = 300;
            MainScope.widgetW = 320;
        }

        LayerDrawable drawPref = (LayerDrawable)getResources().getDrawable(R.drawable.draw_widget);
        GradientDrawable backPref = (GradientDrawable) drawPref.findDrawableByLayerId(R.id.backtemp);
        backPref.setColor(themeColor);
        showWidget.setBackground(getResources().getDrawable(R.drawable.draw_widget));

        ///////1
        serviceNum1 = 1;
        final SharedPreferences sharedPreferences1 = getSharedPreferences("WidgetInfo1", Context.MODE_PRIVATE);
        id1 = sharedPreferences1.getInt("id", -1);
        pack1 = sharedPreferences1.getString("pack", getApplicationContext().getPackageName());
        final String className1 = sharedPreferences1.getString("classname", getResources().getString(R.string.app_name));

        icon1.setImageDrawable(loadIcon(pack1));
        widgetLable1.setText(appLable(pack1));
        if(id1 != -1){createWidgetByID(widget1, id1);}

        if(API > 20){
            final Drawable currentWallpaper = loadIcon(pack1);
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

                widgetLaunchColor = currentColor.getVibrantColor(defaultColor);			System.out.println("widgetLaunchColor Vibrant >> " + themeColor);
            }
            catch (NullPointerException e){
                System.out.println(e);

                widgetLaunchColor = getResources().getColor(R.color.default_color);		System.out.println("widgetLaunchColor Vibrant >> " + themeColor);
            }
        }
        LayerDrawable drawWidget1 = (LayerDrawable)getResources().getDrawable(R.drawable.draw_open);
        GradientDrawable backWidget1 = (GradientDrawable) drawWidget1.findDrawableByLayerId(R.id.backtemp);
        backWidget1.setColor(widgetLaunchColor);
        launchWidget1.setBackground(getResources().getDrawable(R.drawable.draw_open));

        ///////2
        serviceNum2 = 2;
        final SharedPreferences sharedPreferences2 = getSharedPreferences("WidgetInfo2", Context.MODE_PRIVATE);
        id2 = sharedPreferences2.getInt("id", -1);
        pack2 = sharedPreferences2.getString("pack", getApplicationContext().getPackageName());
        final String className2 = sharedPreferences2.getString("classname", getResources().getString(R.string.app_name));

        icon2.setImageDrawable(loadIcon(pack2));
        widgetLable2.setText(appLable(pack2));
        if(id2 != -1){createWidgetByID(widget2, id2);}

        if(API > 20){
            final Drawable currentWallpaper = loadIcon(pack2);
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

                widgetLaunchColor = currentColor.getVibrantColor(defaultColor);			System.out.println("widgetLaunchColor Vibrant >> " + themeColor);
            }
            catch (NullPointerException e){
                System.out.println(e);

                widgetLaunchColor = getResources().getColor(R.color.default_color);		System.out.println("widgetLaunchColor Vibrant >> " + themeColor);
            }
        }
        LayerDrawable drawWidget2 = (LayerDrawable)getResources().getDrawable(R.drawable.draw_open);
        GradientDrawable backWidget2 = (GradientDrawable) drawWidget2.findDrawableByLayerId(R.id.backtemp);
        backWidget2.setColor(widgetLaunchColor);
        launchWidget2.setBackground(getResources().getDrawable(R.drawable.draw_open));

        ///////3
        serviceNum3 = 3;
        final SharedPreferences sharedPreferences3 = getSharedPreferences("WidgetInfo3", Context.MODE_PRIVATE);
        id3 = sharedPreferences3.getInt("id", -1);
        pack3 = sharedPreferences3.getString("pack", getApplicationContext().getPackageName());
        final String className3 = sharedPreferences3.getString("classname", getResources().getString(R.string.app_name));

        icon3.setImageDrawable(loadIcon(pack3));
        widgetLable3.setText(appLable(pack3));
        if(id3 != -1){createWidgetByID(widget3, id3);}

        if(API > 20){
            final Drawable currentWallpaper = loadIcon(pack3);
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

                widgetLaunchColor = currentColor.getVibrantColor(defaultColor);			System.out.println("widgetLaunchColor Vibrant >> " + themeColor);
            }
            catch (NullPointerException e){
                System.out.println(e);

                widgetLaunchColor = getResources().getColor(R.color.default_color);		System.out.println("widgetLaunchColor Vibrant >> " + themeColor);
            }
        }
        LayerDrawable drawWidget3 = (LayerDrawable)getResources().getDrawable(R.drawable.draw_open);
        GradientDrawable backWidget3 = (GradientDrawable) drawWidget3.findDrawableByLayerId(R.id.backtemp);
        backWidget3.setColor(widgetLaunchColor);
        launchWidget3.setBackground(getResources().getDrawable(R.drawable.draw_open));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();

        widget1 = (RelativeLayout)findViewById(R.id.widget1);
        launchWidget1 = (Button)findViewById(R.id.launch1);         registerForContextMenu(launchWidget1);
        widgetLable1 = (TextView)findViewById(R.id.label1);         registerForContextMenu(widgetLable1);
        icon1 = (ImageView)findViewById(R.id.icon1);

        widget2 = (RelativeLayout)findViewById(R.id.widget2);
        launchWidget2 = (Button)findViewById(R.id.launch2);         registerForContextMenu(launchWidget2);
        widgetLable2 = (TextView)findViewById(R.id.label2);         registerForContextMenu(widgetLable2);
        icon2 = (ImageView)findViewById(R.id.icon2);

        widget3 = (RelativeLayout)findViewById(R.id.widget3);
        launchWidget3 = (Button)findViewById(R.id.launch3);         registerForContextMenu(launchWidget3);
        widgetLable3 = (TextView)findViewById(R.id.label3);         registerForContextMenu(widgetLable3);
        icon3 = (ImageView)findViewById(R.id.icon3);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String color = sharedPrefs.getString("textcolor", "1");
        if(color.equals("1")){/*Default Color #222222*/}
        else if(color.equals("2")){
            widgetLable1.setTextColor(Color.WHITE);
            widgetLable2.setTextColor(Color.WHITE);
            widgetLable3.setTextColor(Color.WHITE);
        }

        launchWidget1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id1 != -1){
                    startWidgetService(id1, serviceNum1);
                }
            }
        });

        launchWidget2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id2 != -1){
                    startWidgetService(id2, serviceNum2);
                }
            }
        });

        launchWidget3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id3 != -1){
                    startWidgetService(id3, serviceNum3);
                }
            }
        });

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage(pack1);
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }
        });

        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage(pack2);
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }
        });

        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage(pack3);
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            }
            else if (requestCode == R.id.REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }

    //Required Installed Check
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }

    //Checks if the widget needs any configuration. If it needs, launches the configuration activity.
    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, R.id.REQUEST_CREATE_APPWIDGET);
        }
        else {
            createWidget(data);
        }
    }

    public void createWidget(Intent data) {
        extras = data.getExtras();                                               System.out.println("EXTRA >> " + extras);
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);		System.out.println("WidgetID >> " + appWidgetId);

        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);

        widgetWidth = appWidgetInfo.minWidth;
        widgetHeight = appWidgetInfo.minHeight;

        hostView.setMinimumWidth(widgetWidth);
        hostView.setMinimumHeight(widgetHeight);

        if(MainScope.oneW == false){
            sharedPrefNum =  "1";
            widget = widget1;       widget1.removeAllViews();

            widget1.addView(hostView);
            System.out.println(appWidgetInfo.provider.getClassName()
                    + " Widget WH: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);

            saveWidgetInfo(sharedPrefNum, appWidgetId, appWidgetInfo.provider.getPackageName(), appWidgetInfo.provider.getClassName());

            MainScope.oneW = true;
        }
        else if(MainScope.oneW == true){
            if(MainScope.twoW == false){
                sharedPrefNum = "2";
                widget = widget2;       widget2.removeAllViews();

                widget2.addView(hostView);
                System.out.println(appWidgetInfo.provider.getClassName()
                        + " Widget WH: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);

                saveWidgetInfo(sharedPrefNum, appWidgetId, appWidgetInfo.provider.getPackageName(), appWidgetInfo.provider.getClassName());

                MainScope.twoW = true;
            }
            else if(MainScope.twoW == true){
                if(MainScope.threeW == false){
                    sharedPrefNum = "3";
                    widget = widget3;       widget3.removeAllViews();

                    widget3.addView(hostView);
                    System.out.println(appWidgetInfo.provider.getClassName()
                            + " Widget WH: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);

                    saveWidgetInfo(sharedPrefNum, appWidgetId, appWidgetInfo.provider.getPackageName(), appWidgetInfo.provider.getClassName());

                    MainScope.threeW = true;
                }
                else if(MainScope.threeW == true){
                    Toast.makeText(getApplicationContext(), "Press-Hold on App Name to Remove Widget", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void selectWidget() {
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
    }

    //Avoid Reported Bug in Android
    public void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo = new ArrayList<AppWidgetProviderInfo>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    }

    public void saveWidgetInfo(String num, int ID, String packName, String widgetClass){
        System.out.println("WidgetID: " + ID + " Package: " + packName + " WidgetClass: " + widgetClass);

        SharedPreferences sharedpreferences = getSharedPreferences("WidgetInfo" + num, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefNum = sharedpreferences.edit();
        SharedPreferences.Editor id = sharedpreferences.edit();
        SharedPreferences.Editor packageName = sharedpreferences.edit();
        SharedPreferences.Editor className = sharedpreferences.edit();

        prefNum.putInt("prefNum", Integer.parseInt(num));
        id.putInt("id", ID);
        packageName.putString("pack", packName);
        className.putString("classname", widgetClass);

        id.apply();
        packageName.apply();
        className.apply();
    }

    public Drawable loadIcon(String pack){
        Drawable icon = getResources().getDrawable(R.drawable.ic_launcher);
        try{
            PackageManager packManager = getApplicationContext().getPackageManager();
            ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);

            icon = packManager.getApplicationIcon(app);
        }
        catch(Exception e){
            System.out.println(e);
        }

        return icon;
    }

    public String appLable(String pack){
        String label = getResources().getString(R.string.app_name);
        try {
            PackageManager packManager = getApplicationContext().getPackageManager();
            ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
            label = packManager.getApplicationLabel(app).toString();
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return label;
    }

    public void createWidgetByID(RelativeLayout widget, int  ID) {
        widget.removeAllViews();

        int appWidgetId = ID;

        int H = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, this.getResources().getDisplayMetrics());
        int W = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, this.getResources().getDisplayMetrics());

        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);

        widgetWidth = appWidgetInfo.minWidth;
        widgetHeight = appWidgetInfo.minHeight;

        hostView.setMinimumWidth(widgetWidth);
        hostView.setMinimumHeight(widgetHeight);

        widget.addView(hostView);
        System.out.println(appWidgetInfo.provider.getClassName()
                + " Widget WH: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);
    }

    public void removeWidget(AppWidgetHostView hostView, RelativeLayout widget, int ID) {
        mAppWidgetHost.deleteAppWidgetId(ID);
        widget.removeView(hostView);
        widget.removeAllViews();
    }

    public void startWidgetService(int ID, int serviceNum){
        if (serviceNum == 1) {
            Intent w = new Intent(getApplicationContext(), Widget_Floating_one.class);
            w.putExtra("widgetid", ID);
            startService(w);
        }
        else if (serviceNum == 2) {
            Intent w = new Intent(getApplicationContext(), Widget_Floating_two.class);
            w.putExtra("widgetid", ID);
            startService(w);
        }
        else if (serviceNum == 3) {
            Intent w = new Intent(getApplicationContext(), Widget_Floating_three.class);
            w.putExtra("widgetid", ID);
            startService(w);
        }
    }
}
