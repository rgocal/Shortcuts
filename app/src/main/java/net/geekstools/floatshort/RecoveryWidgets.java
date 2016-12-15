package net.geekstools.floatshort;


import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class RecoveryWidgets extends Activity{

    AppWidgetManager mAppWidgetManager;
    AppWidgetHost mAppWidgetHost;

    @Override
    protected void onCreate(Bundle Saved){
        super.onCreate(Saved);

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, 666);

        stopService(new Intent(getApplicationContext(), Widget_Floating_one.class));
        stopService(new Intent(getApplicationContext(), Widget_Floating_two.class));
        stopService(new Intent(getApplicationContext(), Widget_Floating_three.class));

        MainScope.oneW = false;
        MainScope.twoW = false;
        MainScope.threeW = false;

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

        for(int i = 1; i <= 3; i++){
            startWidgetService(i);
        }

        finish();
    }

    public void startWidgetService(int serviceNum){
        if (serviceNum == 1) {
            final SharedPreferences sharedPreferences1 = getSharedPreferences("WidgetInfo1", Context.MODE_PRIVATE);
            int ID = sharedPreferences1.getInt("id", -1);
            if(ID == -1){
                Toast.makeText(getApplicationContext(), "No More Widgets", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            System.out.println("WidgetID: " + ID + " LOOP: " + serviceNum);

            Intent w = new Intent(getApplicationContext(), Widget_Floating_one.class);
            w.putExtra("widgetid", ID);
            startService(w);
        }
        else if (serviceNum == 2) {
            final SharedPreferences sharedPreferences1 = getSharedPreferences("WidgetInfo2", Context.MODE_PRIVATE);
            int ID = sharedPreferences1.getInt("id", -1);
            if(ID == -1){
                Toast.makeText(getApplicationContext(), "No More Widgets", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            System.out.println("WidgetID: " + ID + " LOOP: " + serviceNum);

            Intent w = new Intent(getApplicationContext(), Widget_Floating_two.class);
            w.putExtra("widgetid", ID);
            startService(w);
        }
        else if (serviceNum == 3) {
            final SharedPreferences sharedPreferences1 = getSharedPreferences("WidgetInfo3", Context.MODE_PRIVATE);
            int ID = sharedPreferences1.getInt("id", -1);
            if(ID == -1){
                Toast.makeText(getApplicationContext(), "No More Widgets", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            System.out.println("WidgetID: " + ID + " LOOP: " + serviceNum);

            Intent w = new Intent(getApplicationContext(), Widget_Floating_three.class);
            w.putExtra("widgetid", ID);
            startService(w);
        }
    }
}
