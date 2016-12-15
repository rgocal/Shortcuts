package net.geekstools.floatshort;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GetTime extends Activity{

	TimePicker timePick;
	AlarmManager alarmManager, alarm1 , alarm2;

	Button set, cancel1, cancel2;
	ImageView icon1, icon2;
	TextView info;

	String pack, action;
	PendingIntent floatService;

	int themeColor, defSysColor;

	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		setContentView(R.layout.time_pick);

		timePick = (TimePicker)findViewById(R.id.timePicker);			timePick.setIs24HourView(true);

		themeColor = getResources().getColor(R.color.default_color);
		defSysColor = getResources().getColor(R.color.android_def);

		ActionBar actBar = getActionBar();
		actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.time) + "</font>"));
		actBar.setSubtitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.time_desc) + "</font>"));
		actBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " + API);
		if(API > 20){
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(defSysColor);
			window.setNavigationBarColor(defSysColor);
		}

		pack = getIntent().getStringExtra("pack");		System.out.println(pack);
		action = getIntent().getStringExtra("time");	System.out.println(action);

		alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
		if(action.contains("timehelper1")){
			alarm1 = alarmManager;
		}
		else if(action.contains("timehelper2")){
			alarm2 = alarmManager;
		}

		info = (TextView)findViewById(R.id.info);
		set = (Button)findViewById(R.id.set);
		cancel1 = (Button)findViewById(R.id.cancelButton1);
		cancel2 = (Button)findViewById(R.id.cancelButton2);

		icon1 = (ImageView)findViewById(R.id.iconOne);
		icon2 = (ImageView)findViewById(R.id.iconTwo);

		final File t1 = getApplicationContext().getFileStreamPath("time1");
		final File t2 = getApplicationContext().getFileStreamPath("time2");

		if(t1.exists() && t2.exists()){
			cancel1.setVisibility(View.VISIBLE);
			cancel2.setVisibility(View.VISIBLE);

			String pack1 = READ("time1", getApplicationContext());
			String pack2 = READ("time2", getApplicationContext());
			String timeInfo1 = READ("timeInfo1", getApplicationContext());
			String timeInfo2 = READ("timeInfo2", getApplicationContext());
			String label = null;
			 try {
				PackageManager packManager = getApplicationContext().getPackageManager();
				ApplicationInfo app1 = getApplicationContext().getPackageManager().getApplicationInfo(pack1, 0);
				ApplicationInfo app2 = getApplicationContext().getPackageManager().getApplicationInfo(pack2, 0);
				label = packManager.getApplicationLabel(app1).toString() + "   " + timeInfo1
						+ "\n" +
						packManager.getApplicationLabel(app2).toString() + "   " + timeInfo2;

				loadIcon(pack1, icon1);
				loadIcon(pack2, icon2);
			}
			 catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			info.setHint(label + "\nTap on Cancel Button to Release a Service");
		}
		else if(t1.exists()){
			cancel1.setVisibility(View.VISIBLE);

			String pack = READ("time1", getApplicationContext());
			String label = null;
			 try {
				PackageManager packManager = getApplicationContext().getPackageManager();
				ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
				label = packManager.getApplicationLabel(app).toString();
			}
			 catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			 info.setText(label + "   " + READ("timeInfo1", getApplicationContext()));
			 loadIcon(pack, icon1);
		}
		else if(t2.exists()){
			cancel2.setVisibility(View.VISIBLE);

			String pack = READ("time2", getApplicationContext());
			String label = null;
			 try {
				PackageManager packManager = getApplicationContext().getPackageManager();
				ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
				label = packManager.getApplicationLabel(app).toString();
			}
			 catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			 info.setText(label + "   " + READ("timeInfo2", getApplicationContext()));
			 loadIcon(pack, icon2);
		}

		set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Vibrator vibe = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
	            vibe.vibrate(75);

	            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
		        set.startAnimation(animation2);

		        if(action.contains("timehelper1")){
					 try {
							FileOutputStream fOut = openFileOutput("time1", MODE_PRIVATE);
							fOut.write((pack).getBytes());
							
							fOut.close();
							fOut.flush();
					 } 
					 catch (IOException e) {
						 e.printStackTrace();
					 }
				}
				else if(action.contains("timehelper2")){
					try {
						FileOutputStream fOut = openFileOutput("time2", MODE_PRIVATE);
						fOut.write((pack).getBytes());
						
						fOut.close();
						fOut.flush();
					} 
			        catch (IOException e) {
						e.printStackTrace();
					}
				}
		        
				Intent act = new Intent(action);
				PendingIntent floatService = PendingIntent
						.getActivity(getBaseContext(), 0, act, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_UPDATE_CURRENT);

				int hour = timePick.getCurrentHour();
				int minute = timePick.getCurrentMinute();

		        //86400000 = 24hrs
				Calendar C = Calendar.getInstance();
				int yr = C.get(Calendar.YEAR);
				int mnth = C.get(Calendar.MONTH);
				int day = C.get(Calendar.DAY_OF_MONTH);
				int hrs = C.get(Calendar.HOUR_OF_DAY);
				int min = C.get(Calendar.MINUTE);

				GregorianCalendar calendarThen = new GregorianCalendar(yr, mnth, day, hour, minute);
				
				long alarmTime = calendarThen.getTimeInMillis();
				long repeatTime = AlarmManager.INTERVAL_DAY;
		        
		        if(action.contains("timehelper1")){
		        	alarm1.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime , repeatTime, floatService);
					try {
						String timeInfo1 = hour + ":" + minute;
						FileOutputStream fOut = openFileOutput("timeInfo1", MODE_PRIVATE);
						fOut.write((timeInfo1).getBytes());

						fOut.close();
						fOut.flush();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
		        	System.out.println("Time >> " + "Hour: " + hour + "| minute: " + minute);
			        Toast.makeText(getApplicationContext(), "Alarm ONE at " + hour + ":" + minute + " Set!", Toast.LENGTH_LONG).show();
				}
				else if(action.contains("timehelper2")){
					alarm2.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime , repeatTime, floatService);
					try {
						String timeInfo2 = hour + ":" + minute;
						FileOutputStream fOut = openFileOutput("timeInfo2", MODE_PRIVATE);
						fOut.write((timeInfo2).getBytes());

						fOut.close();
						fOut.flush();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Time >> " + "Hour: " + hour + "| minute: " + minute);
			        Toast.makeText(getApplicationContext(), "Alarm TWO at " + hour + ":" + minute + " Set!", Toast.LENGTH_LONG).show();
				}

		        finish();
			}
		});
		
		cancel1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(floatService != null && alarm1 != null){
					alarm1.cancel(floatService);
				}
				
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
				cancel1.startAnimation(anim);
				cancel1.setVisibility(View.GONE);
				icon1.startAnimation(anim);
				icon1.setVisibility(View.GONE);
				
				t1.delete();
				
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						finish();
					}
				}, 500);
			}
		});
		cancel1.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				String pack = READ("time1", getApplicationContext());
				String label = null;
				 try {
					PackageManager packManager = getApplicationContext().getPackageManager();
					ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
					label = packManager.getApplicationLabel(app).toString();
				} 
				 catch (NameNotFoundException e) {
					e.printStackTrace();
				}  
				 
				Toast.makeText(getApplicationContext(), label + "\nis integrated to this service", Toast.LENGTH_LONG).show();
				
				return true;
			}
		});
		cancel2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(floatService != null && alarm2 != null){
					alarm2.cancel(floatService);
				}

				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
				cancel2.startAnimation(anim);
				cancel2.setVisibility(View.GONE);
				icon2.startAnimation(anim);
				icon2.setVisibility(View.GONE);
				
				t2.delete();
				
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						finish();
					}
				}, 500);
			}
		});
		cancel2.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				String pack = READ("time2", getApplicationContext());
				String label = null;
				 try {
					PackageManager packManager = getApplicationContext().getPackageManager();
					ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
					label = packManager.getApplicationLabel(app).toString();
				} 
				 catch (NameNotFoundException e) {
					e.printStackTrace();
				}  
				 
				Toast.makeText(getApplicationContext(), label + "\nis integrated to this service", Toast.LENGTH_LONG).show();
				
				return true;
			}
		});
	}
	
	//Read-File
	public String READ(String S, Context context){
		String temp = "NULL";
			
		File G = context.getFileStreamPath(S);
		if(!G.exists()){
			finish();
		}
		try{
			FileInputStream fin = context.openFileInput(S);
				
			BufferedReader br = new BufferedReader(new InputStreamReader(fin, "UTF-8"), 1024);
			System.out.println(br);
					         
			int c;
			temp = "";
			while((c = br.read()) != -1){
				temp = temp + Character.toString((char)c);
			}
		}
		catch(Exception e){
			System.out.println("GetTime " + e);
		}
						
		return temp;
	}
	
	public void loadIcon(String pack, ImageView imgView){
		try{
			PackageManager packManager = getApplicationContext().getPackageManager();
			ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
			
			Drawable icon = packManager.getApplicationIcon(app);
			imgView.setBackground(icon);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}
