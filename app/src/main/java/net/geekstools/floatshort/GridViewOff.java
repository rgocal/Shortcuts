package net.geekstools.floatshort;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
import android.os.Build;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import net.geekstools.floatshort.nav.CardGridAdapter;
import net.geekstools.floatshort.nav.NavDrawerItem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridViewOff extends Activity {

	GridView grid;
	ImageView loadingView;
	ProgressBar loadingBar;

	CardGridAdapter adapter;
	ArrayList<NavDrawerItem> navDrawerItems;

	String[] data = new String[1000];
	String[] dataNew;
	String[] FavDrawer;

	private final static String regexPack = "\\*(.*?)\\*";
	private final static String regexName = "\\[(.*?)\\]";
	private final static String deleteStar = "*";
	private final static String deleteBrktL = "[";
	private final static String deleteBrktR = "[";

	String PackageName;
	String AppName = "Application";
	String AppVersion = "0";
	Drawable AppIcon;

	int themeColor, themeTextColor;
	String themColorString;
	Palette currentColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_off);
		grid = (GridView)findViewById(R.id.grid);

		themeColor = getResources().getColor(R.color.default_color);

		if(appInstalledOrNot("net.geekstools.geekylauncher") == false){
			ActionBar actBar = getActionBar();
			actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.app_name) + "</font>"));
			actBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

			grid.setBackgroundColor(Color.WHITE);

			int API = Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
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

			int API = Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
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
	}

	@Override
	public void onStart(){
		super.onStart();

		//AdSetup
		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
		final AdView adView = (AdView)findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("CDCAA1F20B5C9C948119E886B31681DE")
				.addTestDevice("A1FBA9B2C9228CE78D8F6E7D1BB16DB3")
				.build();
		adView.loadAd(adRequest);

		grid.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();

		LoadApplicationsOff loadApplicationsOff = new LoadApplicationsOff();
		loadApplicationsOff.execute();
	}

	//Context Menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

		Pattern p = Pattern.compile(regexPack);
		Matcher m = p.matcher(dataNew[info.position]);
		m.find();
		PackageName = m.group();
		PackageName = PackageName.replace(deleteStar, "");

		menu.setHeaderTitle(appName(PackageName));
		menu.setHeaderIcon(R.drawable.ic_about);
		String itemName = (dataNew[info.position]);
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
		String itemName = dataNew[info.position];

		Pattern p = Pattern.compile(regexPack);
		Matcher m = p.matcher(itemName);
		m.find();
		PackageName = m.group();
		PackageName = PackageName.replace(deleteStar, "");

		if(menuItemName.equals("Small")){
			MainScope.size = 24;
			MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

			CardGridAdapter.runService(PackageName);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Medium")){
			MainScope.size = 36;
			MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

			CardGridAdapter.runService(PackageName);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Large")){
			MainScope.size = 48;
			MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, this.getResources().getDisplayMetrics());

			CardGridAdapter.runService(PackageName);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setSizeBack();
				}
			}, 50);
		}
		else if(menuItemName.equals("Setting")){
			Intent s = new Intent(getApplicationContext(), SettingGUI.class);
			startActivity(s);

			finish();
		}
		else if(menuItemName.equals("Open App")){
			openApp(PackageName);
		}
		else if(menuItemName.equals("Uninstall")){
			uninstallApp(PackageName);

			Toast.makeText(getApplicationContext(),
					Html.fromHtml("<font color='#D70000'>" + "Restart App" + "</font>"), Toast.LENGTH_SHORT).show();
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

	/************************************/
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
	public int countLine(){
		int nLines = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(getApplicationContext().getFileStreamPath(".AppInfo")));

			while (reader.readLine() != null){
				nLines++;
			}
			System.out.println("Count of Line: " + nLines);
			reader.close();
		}
		catch(Exception e){
			System.out.println("ERROR Line Counting\n" + e);
		}

		return nLines;
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
	public Drawable appIcon(String pack){
		Drawable Icon = null;

		try{
			Icon = getPackageManager().getApplicationIcon(pack);
		}
		catch (Exception e){
			System.out.println(e);
		}

		return Icon;
	}
	public String appVersion(String pack){
		String Version = "0";

		try{
			PackageInfo packInfo = getPackageManager().getPackageInfo(pack, 0);
			Version = packInfo.versionName;
		}
		catch (Exception e){
			System.out.println(e);
		}

		return Version;
	}
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		String AndroidVersion = Build.VERSION.RELEASE;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return manufacturer + " Xperia " + model;
		}
	}
	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	/***********************Functions ContextMenu**************************/
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

	/******************************************/
	private class LoadApplicationsOff extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			String NAME = getDeviceName();
			System.out.println(NAME);

			ArrayList<String> myStringArray =  new ArrayList<String>();

			File f = getApplicationContext().getFileStreamPath(".AppInfo");
			if(!f.exists()){
				finish();
				return;
			}

			loadingView = (ImageView)findViewById(R.id.loadinglogo);
			loadingView.setBackground(new ColorDrawable(themeColor));

			loadingBar = (ProgressBar)findViewById(R.id.loadingProgress);
			loadingBar.getIndeterminateDrawable().setColorFilter(themeTextColor, android.graphics.PorterDuff.Mode.MULTIPLY);

			grid.clearChoices();
		}

		@Override
		protected Void doInBackground(Void... params) {
			//load content
			try{
				FileInputStream fin = new FileInputStream(getApplicationContext().getFileStreamPath(".AppInfo"));
				DataInputStream myDIS = new DataInputStream(fin);

				String line = "";
				int i = 0;
				while ( (line = myDIS.readLine()) != null) {
					data[i] = line;
					System.out.println(data[i]);
					i++;
				}

				dataNew = new String[i];
				System.arraycopy(data, 0, dataNew, 0, i);
				data = dataNew;

				/*************************************************************************************************/
				FavDrawer = new String[i];
				System.arraycopy(data, 0, FavDrawer, 0, i);
				data = FavDrawer;

				int count = countLine();
				System.out.println("onCreate: " + count);

				navDrawerItems = new ArrayList<NavDrawerItem>();

				for(int navItem = 0; navItem < count; navItem++){
					Pattern p = Pattern.compile(regexPack);
					Matcher m = p.matcher(FavDrawer[navItem]);
					m.find();
					PackageName = m.group();
					PackageName = PackageName.replace(deleteStar, "");
					System.out.println("Package Name >> " + navItem + " " + PackageName);
					AppName = appName(PackageName);
					AppVersion = appVersion(PackageName);
					AppIcon = appIcon(PackageName);

					navDrawerItems.add(new NavDrawerItem(
							PackageName                        //AppPack
							, AppIcon            //AppIcon
							, true
							, AppVersion                                        //AppVersion
							, AppName                                            //AppName
					));
					adapter = new CardGridAdapter(getApplicationContext(), navDrawerItems);
				}
			}
			catch(Exception e){
				System.out.println("Error: No Favorite\n" + e);
				finish();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.actanim);
			Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anm);
			LayoutAnimationController controller = new LayoutAnimationController(animation, 0.3f);

			loadingView.setVisibility(View.INVISIBLE);		loadingView.startAnimation(anim);
			loadingBar.setVisibility(View.INVISIBLE);		loadingBar.startAnimation(anim);
			grid.setVerticalFadingEdgeEnabled(true);
			grid.setAdapter(adapter);
			grid.setLayoutAnimation(controller);
			registerForContextMenu(grid);
		}
	}
}
