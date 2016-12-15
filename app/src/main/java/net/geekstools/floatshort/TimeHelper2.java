package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TimeHelper2 extends Activity{

	static boolean time2 = true;
	public static int alpha = 133;
	public static int opacity = 255;
	static boolean hide = false;
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		System.out.println("TimeHelper2");
		
		//Auto Hide
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(sharedPrefs.getBoolean("hide", false) == false){
			hide = false;
		}
		else if(sharedPrefs.getBoolean("hide", false) == true){
			hide = true;
		}
		
		String pack = READ("time2", getApplicationContext());
		
		Intent s = new Intent(TimeHelper2.this, Floating_TIME2.class);
		s.putExtra("pack", pack);
		startService(s);
		
		finish();
	}
	
	//Read-File
	public String READ(String S, Context context){
		String temp = "NULL";

		File G = context.getFileStreamPath(S);
		//new File("/data/data/net.geekstools.floatshort.floatshort.PRO/files/" + S);
		if(!G.exists()){
			finish();
		}
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
}
