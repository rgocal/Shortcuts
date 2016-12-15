package net.geekstools.floatshort;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListGrid extends Activity{

	private static final Uri BASE_URL =
			Uri.parse("android-app://net.geekstools.floatshort/http/g33kstools.blogspot.com/p/createshortcuts.html/");
	GoogleApiClient client;
	Uri urlForAction;
	String titleForAction;
	String descriptionForAction;

	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);

		int API = android.os.Build.VERSION.SDK_INT;		System.out.println("Permissions Check Point API: " +  API);
		if(API > 22){
			File p = getApplicationContext().getFileStreamPath(".Permissions");
			if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
					|| checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
			{
				System.out.println("NO PERMISSIONS SET");
				startActivity(new Intent(getApplicationContext(), DetailHelper.class));

				finish();
				return;
			}
			if(!Settings.canDrawOverlays(getApplicationContext())){
				System.out.println("NO PERMISSIONS SET");
				startActivity(new Intent(getApplicationContext(), DetailHelper.class));

				finish();
				return;
			}
		}

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(sharedPrefs.getBoolean("stable", true) == true){
			//StartBackgroundMainService
			MainScope.Stable = true;
			startService(new Intent(getApplicationContext(), BindServices.class));
		}
		else if(sharedPrefs.getBoolean("stable", true) == false){
			MainScope.Stable = false;
		}

		if(sharedPrefs.getBoolean("autorecov", false) == false){
			MainScope.Return = false;
		}
		else if(sharedPrefs.getBoolean("autorecov", false) == true){
			MainScope.Return = true;
		}

		//Auto Hide
		if(sharedPrefs.getBoolean("hide", false) == false){
			MainScope.hide = false;
		}
		else if(sharedPrefs.getBoolean("hide", false) == true){
			MainScope.hide = true;
		}

		//Shortcuts Size
		String s = sharedPrefs.getString("sizes", "2");
		if(s.equals("1")){
			MainScope.size = 24;

			MainScope.widgetH = 1;
			MainScope.widgetW = 1;
		}
		else if(s.equals("2")){
			MainScope.size = 36;

			MainScope.widgetH = 130;
			MainScope.widgetW = 320;
		}
		else if(s.equals("3")){
			MainScope.size = 48;

			MainScope.widgetH = 300;
			MainScope.widgetW = 320;
		}
		MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());
		MainScope.E = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());

		if(API > 20){
			try{
				AppOpsManager appOps = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
				int mode = appOps.checkOp("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
				if(mode == AppOpsManager.MODE_ALLOWED) {
					UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
					List<UsageStats> queryUsageStats = mUsageStatsManager
							.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
									System.currentTimeMillis() - 1000*60,			//begin
									System.currentTimeMillis());    				//end
					Collections.sort(queryUsageStats, new LastTimeLaunchedComparatorDesc());

					try{
						CustomUsageStats customUsageStats = new CustomUsageStats();
						customUsageStats.usageStats = queryUsageStats.get(1);
						String previousAppPack = customUsageStats.usageStats.getPackageName();
						if(previousAppPack.contains("com.google.android.googlequicksearchbox")){
							Toast.makeText(getApplicationContext(), "Google Now", Toast.LENGTH_LONG).show();

							Intent r = new Intent(getApplicationContext(), RecoveryShortcuts.class);
							r.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(r);

							finish();
							return;
						}
					}
					catch (IndexOutOfBoundsException e){
						System.out.println(e);
						finish();

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								startActivity(new Intent(getApplicationContext(), ListGrid.class));
								Toast.makeText(getApplicationContext(), "Loading Process Depends On your Device. Please Wait...", Toast.LENGTH_LONG).show();
							}
						}, 10);
					}
				}
				else{
					Intent intent = new Intent(getApplicationContext(), DetailHelper.class);
					startActivity(intent);

					finish();
					return;
				}
			}
			catch (SecurityException ignored){}
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				//Indexing AppInfo
				for(int j = 1; j <= 10; j++) {
					String packName = READ(j, getApplicationContext());
					System.out.println(j + ". " + packName);
					if (READ(j, getApplicationContext()).contains("null")) {
						System.out.println("null return");
					} else {
						try{indexAppInfo(appName(packName));} catch (Exception e){}
					}
				}
			}
		}, 100);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String style = sharedPref.getString("apps", "1");	System.out.println(style);
		if(style.equals("1")){
			File f = getApplicationContext().getFileStreamPath(".AppInfo");
			if(f.exists()){
				Intent listViewOff = new Intent(ListGrid.this, MainScopeOff.class);
				listViewOff.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(listViewOff);
			}
			else{
				Intent listView = new Intent(ListGrid.this, MainScope.class);
				listView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(listView);
			}
		}
		else if(style.equals("2")){
			File f = getApplicationContext().getFileStreamPath(".AppInfo");
			if(f.exists()){
				Intent gridViewOff = new Intent(ListGrid.this, GridViewOff.class);
				gridViewOff.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(gridViewOff);
			}
			else{
				Intent gridView = new Intent(ListGrid.this, GridViewDrawer.class);
				gridView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(gridView);
			}
		}

		finish();
	}

	@Override
	public void onDestroy(){
		if(client != null){
			AppIndex.AppIndexApi.end(client, getAction());
			client.disconnect();
		}
		super.onDestroy();
	}

	private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
		@Override
		public int compare(UsageStats left, UsageStats right) {
			return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
		}
	}

	protected Notification bindService(){
		Notification.Builder  mBuilder = new Notification.Builder(this);

		mBuilder.setContentTitle(getResources().getString(R.string.app_name));
		mBuilder.setContentText("Click on Stop to Remove All Shortcuts");
		mBuilder.setTicker(getResources().getString(R.string.app_name));
		mBuilder.setSmallIcon(R.drawable.ic_notification);

		Resources res = getResources();
		Bitmap bM = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		mBuilder.setLargeIcon(bM);
		mBuilder.setAutoCancel(false);

		Intent Main = new Intent(this, MainScope.class);
		PendingIntent mainPV = PendingIntent.getActivity(this, 0, Main, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent settingGUI = new Intent(this, SettingGUI.class);
		PendingIntent pV = PendingIntent.getActivity(this, 0, settingGUI, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent set = new Intent(this, StopAll.class);
		PendingIntent pS = PendingIntent.getActivity(this, 0, set, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.addAction(0, "Stop Services", pS);
		mBuilder.addAction(0, "Settings", pV);
		mBuilder.setContentIntent(mainPV);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		return mBuilder.build();
	}

	/*****************************************/
	public void indexAppInfo(String appName) throws Exception{
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
		client.connect();
		AppIndex.AppIndexApi.start(client, getAction());

		Uri APP_URI = BASE_URL.buildUpon().appendPath(appName).build();
		final Action viewAction = Action.newAction(Action.TYPE_VIEW, appName, APP_URI);
		System.out.println("URI >> " + APP_URI + " ||  " + "Title >> " +  appName + " INDEXED");

		// Call the App Indexing API view method
		final PendingResult<Status> result = AppIndex.AppIndexApi.start(client, viewAction);
		result.setResultCallback(new ResultCallbacks<Status>() {
			@Override
			public void onSuccess(@NonNull Status status) {
				System.out.println("ResultCallback Success>> " + status.isSuccess());
			}

			@Override
			public void onFailure(@NonNull Status status) {
				System.out.println("ResultCallback Success>> " + status.getStatusMessage());
			}
		});
	}
	public Action getAction() {
		urlForAction = BASE_URL;
		titleForAction = "Shortcuts";
		descriptionForAction = "Shortcuts Will Create Soon";

		Thing object = new Thing.Builder()
				.setName(titleForAction)
				.setDescription(descriptionForAction)
				.setUrl(urlForAction)
				.build();

		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}
	public String READ(int i, Context context){
		String temp = "NULL";
		String S = ".File" + i;
		File G = context.getFileStreamPath(S);
		if(!G.exists()){
			System.out.println(S + " NOT FOUND");
			temp = "null";
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
			System.out.println(e);
		}

		return temp;
	}
	public String appName(String pack){
		String Name = null;

		try{
			PackageManager packManager = getApplicationContext().getPackageManager();
			ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(pack, 0);
			Name = packManager.getApplicationLabel(app).toString();
		}
		catch(Exception e){
			System.out.println(e);
		}

		return Name;
	}
}
