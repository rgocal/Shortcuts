package net.geekstools.floatshort;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class RecieverWiFi extends BroadcastReceiver{

	static boolean WiFi1 = true, WiFi2 = true;
	public static int alpha = 133;
	public static int opacity = 255;
	static boolean hide = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final WifiManager wManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE );
		if(wManager.isWifiEnabled() == true){
			System.out.println("WiFi Enabled");
			
			//Auto Hide
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			if(sharedPrefs.getBoolean("hide", false) == false){
				hide = false;
			}
			else if(sharedPrefs.getBoolean("hide", false) == true){
				hide = true;
			}
			
			File p1 = context.getFileStreamPath(".WiFiPack1");
			//new File("/data/data/net.geekstools.floatshort.floatshort.PRO/files/.WiFiPack1");
			File p2 = context.getFileStreamPath(".WiFiPack2");
			//new File("/data/data/net.geekstools.floatshort.floatshort.PRO/files/.WiFiPack2");
			
			if(p1.exists()){
				String pack1 = READ(".WiFiPack1", context);
				Intent a = new Intent(context, Floating_WiFi1.class);
				a.putExtra("pack", pack1);
				context.startService(a);
				
				if(p2.exists()){
					String pack2 = READ(".WiFiPack2", context);
					Intent b = new Intent(context, Floating_WiFi2.class);
					b.putExtra("pack", pack2);
					context.startService(b);
				}
			}
		}
		else if(wManager.isWifiEnabled() == false){
			System.out.println("WiFi Disabled");
			
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			if(sharedPrefs.getBoolean("autoremove", false) == true){
				context.stopService(new Intent(context, Floating_WiFi1.class));
				context.stopService(new Intent(context, Floating_WiFi2.class));
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
