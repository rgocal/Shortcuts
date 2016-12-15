package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class RecoveryShortcuts extends Activity{

	//http://g33kstools.blogspot.com/p/createshortcuts.html/
	private static final Uri BASE_URL =
			Uri.parse("android-app://net.geekstools.floatshort.PRO/http/g33kstools.blogspot.com/p/createshortcuts.html/");

	GoogleApiClient client;
	Uri urlForAction;
	String titleForAction;
	String descriptionForAction;

	String packName;
	int j;
	
	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		stopService(new Intent(getApplicationContext(), Floating_one.class));
		stopService(new Intent(getApplicationContext(), Floating_two.class));
		stopService(new Intent(getApplicationContext(), Floating_three.class));
		stopService(new Intent(getApplicationContext(), Floating_four.class));
		stopService(new Intent(getApplicationContext(), Floating_five.class));
		stopService(new Intent(getApplicationContext(), Floating_six.class));
		stopService(new Intent(getApplicationContext(), Floating_seven.class));
		stopService(new Intent(getApplicationContext(), Floating_eight.class));
		stopService(new Intent(getApplicationContext(), Floating_nine.class));
		stopService(new Intent(getApplicationContext(), Floating_ten.class));

		MainScope.one = false;
		MainScope.two = false;
		MainScope.three = false;
		MainScope.four = false;
		MainScope.five = false;
		MainScope.six = false;
		MainScope.seven = false;
		MainScope.eight = false;
		MainScope.nine = false;
		MainScope.ten = false;

		MainScope.oneS = false;
		MainScope.twoS = false;
		MainScope.threeS = false;
		MainScope.fourS = false;
		MainScope.fiveS = false;
		MainScope.sixS = false;
		MainScope.sevenS = false;
		MainScope.eightS = false;
		MainScope.nineS = false;
		MainScope.tenS = false;

		MainScope.alpha = 133;
		MainScope.opacity = 255;
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
		}
		else if(s.equals("2")){
			MainScope.size = 36;
		}
		else if(s.equals("3")){
			MainScope.size = 48;
		}
		MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

		/**************************/
		for(j = 1; j <= 10; j++){
			packName = READ(j, getApplicationContext());
			System.out.println(j + ". " + packName);
			if(READ(j, getApplicationContext()).contains("null")){
				Toast.makeText(getApplicationContext(), "No More Floating Shortcuts", Toast.LENGTH_LONG).show();

				finish();
				return;
			}
			runService(packName);
			try{indexAppInfo(appName(packName));}catch (Exception e){}
		}
		/**************************/

		finish();
	}

	@Override
	public void onStart(){
		super.onStart();
	}

	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	public void onDestroy(){
		if(client != null){
			AppIndex.AppIndexApi.end(client, getAction());
			client.disconnect();
		}
		super.onDestroy();
	}

	/*********************************************/
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

	public String READ(int i, Context context){
		String temp = "NULL";
		String S = ".File" + i;
		File G = context.getFileStreamPath(S);
		if(!G.exists()){
			System.out.println(S + " NOT FOUND");
			temp = "null";
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
			System.out.println(e);
		}

		return temp;
	}
	public void runService(String packName){
		try {
			/*Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);*/
			if(MainScope.one == false){
				Intent s = new Intent(RecoveryShortcuts.this, Floating_one.class);
				s.putExtra("pack", packName);
				startService(s);
				
				MainScope.one = true;
				MainScope.oneS = true;
			} 
			else if(MainScope.one = true){
				//ONE is Busy!
				if (MainScope.two == false){
					Intent s = new Intent(RecoveryShortcuts.this, Floating_two.class);
					s.putExtra("pack", packName);
					startService(s);
					
					MainScope.two = true;
					MainScope.twoS = true;
				}
				else if(MainScope.two == true){
					//TWO is Busy!
					if (MainScope.three == false){
						Intent s = new Intent(RecoveryShortcuts.this, Floating_three.class);
						s.putExtra("pack", packName);
						startService(s);
						
						MainScope.three = true;
						MainScope.threeS = true;
					}
					else if(MainScope.three == true){
						//THREE is Busy!
						if (MainScope.four == false){
							Intent s = new Intent(RecoveryShortcuts.this, Floating_four.class);
							s.putExtra("pack", packName);
							startService(s);
							
							MainScope.four = true;
							MainScope.fourS = true;
						}
						else if(MainScope.four == true){
							//FOUR is Busy!
							if (MainScope.five == false){
								Intent s = new Intent(RecoveryShortcuts.this, Floating_five.class);
								s.putExtra("pack", packName);
								startService(s);
								
								MainScope.five = true;
								MainScope.fiveS = true;
							}
							else if(MainScope.five == true){
								//FIVE is Busy!
								if (MainScope.six == false){
									Intent s = new Intent(RecoveryShortcuts.this, Floating_six.class);
									s.putExtra("pack", packName);
									startService(s);
									
									MainScope.six = true;
									MainScope.sixS = true;
								}
								else if(MainScope.six == true){
									//SIX is Busy!
									if (MainScope.seven == false){
										Intent s = new Intent(RecoveryShortcuts.this, Floating_seven.class);
										s.putExtra("pack", packName);
										startService(s);
										
										MainScope.seven = true;
										MainScope.sevenS = true;
									}
									else if(MainScope.seven == true){
										//SEVEN is Busy!
										if (MainScope.eight == false){
											Intent s = new Intent(RecoveryShortcuts.this, Floating_eight.class);
											s.putExtra("pack", packName);
											startService(s);
											
											MainScope.eight = true;
											MainScope.eightS = true;
										}
										else if(MainScope.eight == true){
											//EIGHT is Busy!
											if (MainScope.nine == false){
												Intent s = new Intent(RecoveryShortcuts.this, Floating_nine.class);
												s.putExtra("pack", packName);
												startService(s);
												
												MainScope.nine = true;
												MainScope.nineS = true;
											}
											else if(MainScope.nine == true){
												//NINE is Busy!
												if (MainScope.ten == false){
													Intent s = new Intent(RecoveryShortcuts.this, Floating_ten.class);
													s.putExtra("pack", packName);
													startService(s);
													
													MainScope.ten = true;
													MainScope.tenS = true;
												}
												else if(MainScope.ten == true){
													//TEN is Busy!
													System.out.println("Long Press On Icon To Release A Service");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}  
		catch (Exception e) {
			Toast.makeText(RecoveryShortcuts.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
