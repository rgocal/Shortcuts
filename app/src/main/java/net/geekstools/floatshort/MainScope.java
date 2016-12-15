package net.geekstools.floatshort;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainScope extends ListActivity {

	RelativeLayout mainList;
	
	public static int size, Position;
	public static int widgetW, widgetH;
	public static int HW = 0;
	public static int E = 0;
	public static int alpha = 133;
	public static int opacity = 255;

	private PackageManager packageManager;
	private List<ApplicationInfo> applist;
	private Adaptor listadaptor;
	ListView lv;

	public static boolean hide = false;
	public static boolean Stable = false;
	public static boolean Return = false;
	public static boolean one, two, three, four, five, six, seven, eight, nine, ten = false;
	public static boolean oneS, twoS, threeS, fourS, fiveS, sixS, sevenS, eightS, nineS, tenS = false;
	public static boolean oneW, twoW, threeW = false;

	int themeColor, themeTextColor;
	String themColorString;
	Palette currentColor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_scope);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mainList = (RelativeLayout)findViewById(R.id.mainlist);

		themeColor = getResources().getColor(R.color.default_color);

		int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
		if(appInstalledOrNot("net.geekstools.geekylauncher") == false){
			ActionBar actBar = getActionBar();
			actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.app_name) + "</font>"));
			actBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

			mainList.setBackgroundColor(Color.WHITE);

			API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
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
			SharedPreferences.Editor stable = sharedPrefs.edit();
			stable.putBoolean("stable", true);
			stable.apply();
			getListView().setCacheColorHint(Color.TRANSPARENT);

			ActionBar actBar = getActionBar();
			actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.app_name) + "</font>"));
			actBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
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
				window.setStatusBarColor(Color.TRANSPARENT);
				getWindow().setNavigationBarColor(Color.TRANSPARENT);
			}
		}


		//Auto Hide
		if(sharedPrefs.getBoolean("hide", false) == false){
			hide = false;
		}
		else if(sharedPrefs.getBoolean("hide", false) == true){
			hide = true;
		}

		//Shortcuts Size
		String s = sharedPrefs.getString("sizes", "2");
		if(s.equals("1")){
			size = 24;
		}
		else if(s.equals("2")){
			size = 36;
		}
		else if(s.equals("3")){
			size = 48;
		}
		HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics());
		E = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());

		packageManager = getPackageManager();
		new LoadApplications().execute();
	}

	@Override
	public void onStart(){
		super.onStart();

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(sharedPrefs.getBoolean("stable", true) == false){
			Stable = false;
		}
		else if(sharedPrefs.getBoolean("stable", true) == true){
			Stable = true;
		}

		if(sharedPrefs.getBoolean("autorecov", false) == false){
			Return = false;
		}
		else if(sharedPrefs.getBoolean("autorecov", false) == true){
			Return = true;
		}
	}

	@Override
	public void onResume(){
		super.onResume();

		lv = getListView();
		registerForContextMenu(lv);

		File f = getApplicationContext().getFileStreamPath(".AppInfo");
		if(f.exists()){
			deleteFile(".AppInfo");
		}

		SaveApplications apps = new SaveApplications();
		apps.execute();
	}

	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	public void onBackPressed(){
		Intent homeIntent= new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(homeIntent);

		this.finish();

		super.onBackPressed();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	//Context Menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

		ApplicationInfo app = applist.get(info.position);
		String pack = app.packageName;

		menu.setHeaderTitle(getResources().getString(R.string.shortsize) + ": " + appName(pack));
		menu.setHeaderIcon(R.drawable.ic_about_small);

		String[] menuItems = getResources().getStringArray(R.array.ContextMenu);
		for (int I = 0; I < menuItems.length; I++) {
			menu.add(Menu.NONE, I, I, menuItems[I]);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = getResources().getStringArray(R.array.ContextMenu);
		String menuItemName = menuItems[menuItemIndex];

		Position = info.position;

		ApplicationInfo app = applist.get(Position);
		String pack = app.packageName;

		if(menuItemName.equals("Small")){
			System.out.println("Position: " + Position);

			size = 24;
			HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics());

			runService(Position);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Medium")){
			System.out.println("Position: " + Position);

			size = 36;
			HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics());

			runService(Position);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Large")){
			System.out.println("Position: " + Position);

			size = 48;
			HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics());

			runService(Position);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Setting")){
			Intent s = new Intent(MainScope.this, SettingGUI.class);
			startActivity(s);

			finish();
		}
		else if(menuItemName.equals("Open App")){
			openApp(pack);
		}
		else if(menuItemName.equals("Uninstall")){
			uninstallApp(pack);

			Toast.makeText(getApplicationContext(),
					Html.fromHtml("<font color='#D70000'>" + "Restart App" + "</font>"), Toast.LENGTH_SHORT).show();
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_scope, menu);

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
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = true;

		switch (item.getItemId()) {
		case R.id.recovw: {
			startActivity(new Intent(getApplicationContext(), RecoveryWidgets.class));
			
			break;
		}
		case R.id.pref:{
			Intent s = new Intent(MainScope.this, SettingGUI.class);
			startActivity(s);

			finish();
			break;
		}
		case R.id.recov:{
			startActivity(new Intent(getApplicationContext(), RecoveryShortcuts.class));

			break;
		}
		default: {
			result = super.onOptionsItemSelected(item);
				break;
			}
		}
			return result;
	}

	public void displayAboutDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.geeksempire));
		builder.setMessage(getResources().getString(R.string.geektools) + "\n\n"
				+ getResources().getString(R.string.guide));
		builder.setIcon(R.drawable.ic_about_small);

		builder.setPositiveButton("FAQ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse(getResources().getString(R.string.link_faq)));
				startActivity(browserIntent);
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("Rate", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(getResources().getString(R.string.link_rate)));
				startActivity(browserIntent);

				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

        runService(position);
		saveServiceFile(position);
	}
	
	private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();

		Collections.sort(list, new ApplicationInfo.DisplayNameComparator(packageManager));
		for (ApplicationInfo info : list) {
			try {
				if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return applist;
	}

	private class LoadApplications extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress = null;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(MainScope.this, null, "Loading Applications Info...");
			
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			listadaptor = new Adaptor(MainScope.this, R.layout.snippet_list_row, applist);
			
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
	        LayoutAnimationController controller = new LayoutAnimationController(animation, 0.3f);
	        
	        lv.setLayoutAnimation(controller);
			setListAdapter(listadaptor);
			
			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

	private List<ApplicationInfo> checkForLaunchIntentSave(List<ApplicationInfo> list, Context c) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		int i = 0;

		Collections.sort(list, new ApplicationInfo.DisplayNameComparator(packageManager));
		for (ApplicationInfo info : list) {
			try {
				if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);

					saveAppInfo(i, info.packageName, appName(info.packageName), c);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return applist;
	}

	private class SaveApplications extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			checkForLaunchIntentSave(packageManager.getInstalledApplications(PackageManager.GET_META_DATA), getApplicationContext());

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("Data Updated");
		}
	}
	
	/*****************Functions********************/
	public void saveAppInfo(int i, String packName, String appName, Context c){
		try {
			String toSave = i + " " + "[" + appName + "]" + " " + "*" + packName + "*" + "\n";
			FileOutputStream fOut = c.openFileOutput(".AppInfo", c.MODE_PRIVATE|c.MODE_APPEND);
			fOut.write((toSave).getBytes());

			System.out.println("DONE >> " + toSave);

			fOut.close();
			fOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public void saveServiceFile(int position){
		ApplicationInfo app = applist.get(position);
		String packName = app.packageName;
		try {
			//Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
			if(oneS == false){
				savePackName(packName, 1);

				oneS = true;
			}
			else if(oneS = true){
				//ONE is Busy!
				if (twoS == false){
					savePackName(packName, 2);

					twoS = true;
				}
				else if(twoS == true){
					//TWO is Busy!
					if (threeS == false){
						savePackName(packName, 3);

						threeS = true;
					}
					else if(threeS == true){
						//THREE is Busy!
						if (fourS == false){
							savePackName(packName, 4);

							fourS = true;
						}
						else if(fourS == true){
							//FOUR is Busy!
							if (fiveS == false){
								savePackName(packName, 5);

								fiveS = true;
							}
							else if(fiveS == true){
								//FIVE is Busy!
								if (sixS == false){
									savePackName(packName, 6);

									sixS = true;
								}
								else if(sixS == true){
									//SIX is Busy!
									if (sevenS == false){
										savePackName(packName, 7);

										sevenS = true;
									}
									else if(sevenS == true){
										//SEVEN is Busy!
										if (eightS == false){
											savePackName(packName, 8);

											eightS = true;
										}
										else if(eightS == true){
											//EIGHT is Busy!
											if (nineS == false){
												savePackName(packName, 9);

												nineS = true;
											}
											else if(nineS == true){
												//NINE is Busy!
												if (tenS == false){
													savePackName(packName, 10);

													tenS = true;
													Toast.makeText(getApplicationContext(), "ReClick to Refresh", Toast.LENGTH_LONG).show();
												}
												else if(tenS == true){
													//TEN is Busy!
													//Return
													oneS = twoS = threeS = fourS = fiveS = sixS = sevenS = eightS = nineS = tenS = false;
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
			Toast.makeText(MainScope.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void runService(int position){
		ApplicationInfo app = applist.get(position);
		try {
			/*Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);*/
			if(one == false){
				Intent s = new Intent(MainScope.this, Floating_one.class);
				s.putExtra("pack", app.packageName);
				startService(s);
				
				one = true;
			} 
			else if(one = true){
				//ONE is Busy!
				if (two == false){
					Intent s = new Intent(MainScope.this, Floating_two.class);
					s.putExtra("pack", app.packageName);
					startService(s);
					
					two = true;
				}
				else if(two == true){
					//TWO is Busy!
					if (three == false){
						Intent s = new Intent(MainScope.this, Floating_three.class);
						s.putExtra("pack", app.packageName);
						startService(s);
						
						three = true;
					}
					else if(three == true){
						//THREE is Busy!
						if (four == false){
							Intent s = new Intent(MainScope.this, Floating_four.class);
							s.putExtra("pack", app.packageName);
							startService(s);
							
							four = true;
						}
						else if(four == true){
							//FOUR is Busy!
							if (five == false){
								Intent s = new Intent(MainScope.this, Floating_five.class);
								s.putExtra("pack", app.packageName);
								startService(s);
								
								five = true;
							}
							else if(five == true){
								//FIVE is Busy!
								if (six == false){
									Intent s = new Intent(MainScope.this, Floating_six.class);
									s.putExtra("pack", app.packageName);
									startService(s);
									
									six = true;
								}
								else if(six == true){
									//SIX is Busy!
									if (seven == false){
										Intent s = new Intent(MainScope.this, Floating_seven.class);
										s.putExtra("pack", app.packageName);
										startService(s);
										
										seven = true;
									}
									else if(seven == true){
										//SEVEN is Busy!
										if (eight == false){
											Intent s = new Intent(MainScope.this, Floating_eight.class);
											s.putExtra("pack", app.packageName);
											startService(s);
											
											eight = true;
										}
										else if(eight == true){
											//EIGHT is Busy!
											if (nine == false){
												Intent s = new Intent(MainScope.this, Floating_nine.class);
												s.putExtra("pack", app.packageName);
												startService(s);
												
												nine = true;
											}
											else if(nine == true){
												//NINE is Busy!
												if (ten == false){
													Intent s = new Intent(MainScope.this, Floating_ten.class);
													s.putExtra("pack", app.packageName);
													startService(s);
													
													ten = true;
												}
												else if(ten == true){
													//TEN is Busy!
													Toast.makeText(getApplicationContext(), "Long Press On Icon To Release A Service", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(MainScope.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void setSizeBack(){
		/*Shortcuts Size*/
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String s = sharedPrefs.getString("sizes", "NULL");
		if(s.equals("1")){
			size = 24;
		}
		else if(s.equals("2")){
			size = 36;
		}
		else if(s.equals("3")){
			size = 48;
		}
		HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics());
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

	public void uninstallApp(String pack){
		Uri packageUri = Uri.parse("package:" + pack);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(uninstallIntent);
	}
	
	public void openApp(String pack){
		Intent i = new Intent(Intent.ACTION_MAIN);
		PackageManager manager = getPackageManager();
		i = manager.getLaunchIntentForPackage(pack);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		startActivity(i);
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

    //Save packName to Separate Files
    public void savePackName(String pack, int i){
    	try {
    		String fileName = ".File" + i;
    		FileOutputStream fOut = openFileOutput(fileName, MODE_PRIVATE);
			fOut.write((pack).getBytes());
			
			System.out.println("DONE " + i + " >> " + pack);
			
			fOut.close();
			fOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
    }
}
