package net.geekstools.floatshort;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class RecieverGPS extends BroadcastReceiver{

	static boolean gps1 = true, gps2 = true;
	public static int alpha = 133;
	public static int opacity = 255;
	static boolean hide = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final LocationManager locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE );
		if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
			System.out.println("GPS Enabled");
			
			//Auto Hide
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			if(sharedPrefs.getBoolean("hide", false) == false){
				hide = false;
			}
			else if(sharedPrefs.getBoolean("hide", false) == true){
				hide = true;
			}
			
			File p1 = context.getFileStreamPath(".GPSPack1");
			//new File("/data/data/net.geekstools.floatshort.floatshort.PRO/files/.GPSPack1");
			File p2 = context.getFileStreamPath(".GPSPack2");
			//new File("/data/data/net.geekstools.floatshort.floatshort.PRO/files/.GPSPack2");
			
			if(p1.exists()){
				String pack1 = READ(".GPSPack1", context);
				Intent a = new Intent(context, Floating_GPS1.class);
				a.putExtra("pack", pack1);
				context.startService(a);
				
				if(p2.exists()){
					String pack2 = READ(".GPSPack2", context);
					Intent b = new Intent(context, Floating_GPS2.class);
					b.putExtra("pack", pack2);
					context.startService(b);
				}
			}
		}
		else if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false){
			System.out.println("GPS Disabled");

			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			if(sharedPrefs.getBoolean("autoremove", false) == true){
				context.stopService(new Intent(context, Floating_GPS1.class));
				context.stopService(new Intent(context, Floating_GPS2.class));
			}
		}
	}
	
	//Read-File
	public String READ(String S, Context context){
		System.out.println(S + " Exists!");
		String temp = "NULL";
		try{
			FileInputStream fin = context.openFileInput(S);
		         
			BufferedReader br = new BufferedReader(new InputStreamReader(fin, "UTF-8"), 1024);
			System.out.println(br);
		         
			int c;
			temp = "";
			while( (c = br.read()) != -1){
				temp = temp + Character.toString((char)c);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
			
		return temp;
	}
	
	/*Intent i = new Intent(Intent.ACTION_MAIN);
    		PackageManager manager = context.getPackageManager();
    		i = manager.getLaunchIntentForPackage(pack1);
    		i.addCategory(Intent.CATEGORY_LAUNCHER);
    		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		context.startActivity(i);*/
}
