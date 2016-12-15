package net.geekstools.floatshort;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridViewDrawer extends Activity {

	Context context;
	LinearLayout mainGrid;

	private static ArrayList<GridViewHelper> mApplications;
	private final BroadcastReceiver mApplicationsReceiver = new ApplicationsIntentReceiver();
	private GridView mGrid;

	PackageManager packageManager;

	int themeColor, themeTextColor;
	String themColorString;
	Palette currentColor;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		mainGrid = (LinearLayout)findViewById(R.id.gridView);
		packageManager = getPackageManager();
		context = getApplicationContext();

		themeColor = getResources().getColor(R.color.default_color);

		if(appInstalledOrNot("net.geekstools.geekylauncher") == false){
			ActionBar actBar = getActionBar();
			actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.app_name) + "</font>"));
			actBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

			mainGrid.setBackgroundColor(Color.WHITE);

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
			ActionBar actBar = getActionBar();
			actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.app_name) + "</font>"));
			actBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
				window.setStatusBarColor(Color.TRANSPARENT);
				getWindow().setNavigationBarColor(Color.TRANSPARENT);
			}
		}

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
		}
		else if(s.equals("2")){
			MainScope.size = 36;
		}
		else if(s.equals("3")){
			MainScope.size = 48;
		}
		MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

		registerIntentReceivers();
		loadApplications(true);
		setupGallery();
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
				Intent s = new Intent(this, SettingGUI.class);
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

	//Context Menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		int i = (info.position);

		GridViewHelper app = (GridViewHelper) mGrid.getItemAtPosition(i);
		String pack = app.pack.toString();

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

		int Position = info.position;
		GridViewHelper app = (GridViewHelper) mGrid.getItemAtPosition(Position);
		String pack = app.pack.toString();

		if(menuItemName.equals("Small")){
			System.out.println("Position: " + Position);

			MainScope.size = 24;
			MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

			runService(pack);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Medium")){
			System.out.println("Position: " + Position);

			MainScope.size = 36;
			MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

			runService(pack);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Large")){
			System.out.println("Position: " + Position);

			MainScope.size = 48;
			MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

			runService(pack);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Setting")){
			Intent s = new Intent(GridViewDrawer.this, SettingGUI.class);
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

	public void setupGallery(){
		mGrid = (GridView) findViewById(R.id.gridView1);
		registerForContextMenu(mGrid);

		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.actanim);
		LayoutAnimationController controller = new LayoutAnimationController(animation, 0.3f);

		mGrid.setLayoutAnimation(controller);
		mGrid.setAdapter(new GridAdapter(this, mApplications));
		mGrid.setSelection(0);

		mGrid.setOnItemClickListener(new ShortCuts());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (Intent.ACTION_MAIN.equals(intent.getAction())) {
			getWindow().closeAllPanels();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		final int count = mApplications.size();
		for (int i = 0; i < count; i++) {
			mApplications.get(i).icon.setCallback(null);
		}

		unregisterReceiver(mApplicationsReceiver);
	}

	private void registerIntentReceivers() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED);
		filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(mApplicationsReceiver, filter);
	}

	private void loadApplications(boolean isLaunching) {

		if (isLaunching && mApplications != null) {
			return;
		}

		PackageManager manager = getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		if (apps != null) {
			final int count = apps.size();

			if (mApplications == null) {
				mApplications = new ArrayList<GridViewHelper>(count);
			}
			mApplications.clear();

			for (int i = 0; i < count; i++) {
				GridViewHelper application = new GridViewHelper();
				ResolveInfo info = apps.get(i);
				application.pack = info.activityInfo.packageName;	//loadLabel(manager);
				application.setActivity(new ComponentName(
								info.activityInfo.applicationInfo.packageName,
								info.activityInfo.name),
						Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				application.icon = info.activityInfo.loadIcon(manager);
				mApplications.add(application);
			}
		}
	}

	private class ApplicationsIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			loadApplications(false);
		}
	}

	private class GridAdapter extends ArrayAdapter<GridViewHelper> {
		private Rect mOldBounds = new Rect();

		public GridAdapter(Context context, ArrayList<GridViewHelper> apps) {
			super(context, 0, apps);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final GridViewHelper info = mApplications.get(position);

			if (convertView == null) {
				final LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.application, parent,
						false);
			}

			Drawable icon = info.icon;
			if (!info.filtered) {
				int HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getApplicationContext().getResources().getDisplayMetrics());

				int width = HW;
				int height = HW;

				final Bitmap.Config c = icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
				final Bitmap thumb = Bitmap.createBitmap(width, height, c);
				final Canvas canvas = new Canvas(thumb);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, 0));

				mOldBounds.set(icon.getBounds());
				icon.setBounds(0, 0, width, height);
				icon.draw(canvas);
				icon.setBounds(mOldBounds);
				icon = info.icon = new BitmapDrawable(thumb);
				info.filtered = true;
			}

			final TextView textView = (TextView) convertView.findViewById(R.id.label);

			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(GridViewDrawer.this);
			String color = sharedPrefs.getString("textcolor", "1");
			if(color.equals("1")){
				//Default Color #222222
			}
			else if(color.equals("2")){
				textView.setTextColor(Color.parseColor("#EEEEEE"));
			}
			textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
			textView.setText(appName(info.pack.toString()));

			return convertView;
		}
	}

	private class ShortCuts implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View v, int i, long id) {
			GridViewHelper app = (GridViewHelper) parent.getItemAtPosition(i);

			runService(app.pack.toString());
			saveServiceFile(app.pack.toString());
		}
	}

	/*************************************/
	public void runService(String packName){
		try {
			/*Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);*/
			if(MainScope.one == false){
				Intent s = new Intent(context, Floating_one.class);
				s.putExtra("pack", packName);
				context.startService(s);

				MainScope.one = true;
			}
			else if(MainScope.one = true){
				//ONE is Busy!
				if (MainScope.two == false){
					Intent s = new Intent(context, Floating_two.class);
					s.putExtra("pack", packName);
					context.startService(s);

					MainScope.two = true;
				}
				else if(MainScope.two == true){
					//TWO is Busy!
					if (MainScope.three == false){
						Intent s = new Intent(context, Floating_three.class);
						s.putExtra("pack", packName);
						context.startService(s);

						MainScope.three = true;
					}
					else if(MainScope.three == true){
						//THREE is Busy!
						if (MainScope.four == false){
							Intent s = new Intent(context, Floating_four.class);
							s.putExtra("pack", packName);
							context.startService(s);

							MainScope.four = true;
						}
						else if(MainScope.four == true){
							//FOUR is Busy!
							if (MainScope.five == false){
								Intent s = new Intent(context, Floating_five.class);
								s.putExtra("pack", packName);
								context.startService(s);

								MainScope.five = true;
							}
							else if(MainScope.five == true){
								//FIVE is Busy!
								if (MainScope.six == false){
									Intent s = new Intent(context, Floating_six.class);
									s.putExtra("pack", packName);
									context.startService(s);

									MainScope.six = true;
								}
								else if(MainScope.six == true){
									//SIX is Busy!
									if (MainScope.seven == false){
										Intent s = new Intent(context, Floating_seven.class);
										s.putExtra("pack", packName);
										context.startService(s);

										MainScope.seven = true;
									}
									else if(MainScope.seven == true){
										//SEVEN is Busy!
										if (MainScope.eight == false){
											Intent s = new Intent(context, Floating_eight.class);
											s.putExtra("pack", packName);
											context.startService(s);

											MainScope.eight = true;
										}
										else if(MainScope.eight == true){
											//EIGHT is Busy!
											if (MainScope.nine == false){
												Intent s = new Intent(context, Floating_nine.class);
												s.putExtra("pack", packName);
												context.startService(s);

												MainScope.nine = true;
											}
											else if(MainScope.nine == true){
												//NINE is Busy!
												if (MainScope.ten == false){
													Intent s = new Intent(context, Floating_ten.class);
													s.putExtra("pack", packName);
													context.startService(s);

													MainScope.ten = true;
												}
												else if(MainScope.ten == true){
													//TEN is Busy!
													Toast.makeText(context, "Long Press On Icon To Release A Service", Toast.LENGTH_SHORT).show();
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

		}
	}

	public void saveServiceFile(String packName){
		try {
			//Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
			if(MainScope.oneS == false){
				savePackName(packName, 1);

				MainScope.oneS = true;
			}
			else if(MainScope.oneS = true){
				//ONE is Busy!
				if (MainScope.twoS == false){
					savePackName(packName, 2);

					MainScope.twoS = true;
				}
				else if(MainScope.twoS == true){
					//TWO is Busy!
					if (MainScope.threeS == false){
						savePackName(packName, 3);

						MainScope.threeS = true;
					}
					else if(MainScope.threeS == true){
						//THREE is Busy!
						if (MainScope.fourS == false){
							savePackName(packName, 4);

							MainScope.fourS = true;
						}
						else if(MainScope.fourS == true){
							//FOUR is Busy!
							if (MainScope.fiveS == false){
								savePackName(packName, 5);

								MainScope.fiveS = true;
							}
							else if(MainScope.fiveS == true){
								//FIVE is Busy!
								if (MainScope.sixS == false){
									savePackName(packName, 6);

									MainScope.sixS = true;
								}
								else if(MainScope.sixS == true){
									//SIX is Busy!
									if (MainScope.sevenS == false){
										savePackName(packName, 7);

										MainScope.sevenS = true;
									}
									else if(MainScope.sevenS == true){
										//SEVEN is Busy!
										if (MainScope.eightS == false){
											savePackName(packName, 8);

											MainScope.eightS = true;
										}
										else if(MainScope.eightS == true){
											//EIGHT is Busy!
											if (MainScope.nineS == false){
												savePackName(packName, 9);

												MainScope.nineS = true;
											}
											else if(MainScope.nineS == true){
												//NINE is Busy!
												if (MainScope.tenS == false){
													savePackName(packName, 10);

													MainScope.tenS = true;
												}
												else if(MainScope.tenS == true){
													//TEN is Busy!
													//Return
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
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
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

	public void setSizeBack(){
		/*Shortcuts Size*/
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String s = sharedPrefs.getString("sizes", "NULL");
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
