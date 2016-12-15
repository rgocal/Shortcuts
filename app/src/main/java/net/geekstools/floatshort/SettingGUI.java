package net.geekstools.floatshort;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SettingGUI extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	ListPreference sizes, style, colortext;
	SharedPreferences sharedPrefs;
	SwitchPreference stable, autoremove, autotrans, autorecovs;
	Preference app, overview, prefwifi, prefbluetooth, prefgps, preftime, launcher, widget, ad;

	int themeColor, themeTextColor;
	String themColorString;
	Palette currentColor;

	@Override
	protected void onCreate(Bundle saved){
		super.onCreate(saved);
		addPreferencesFromResource(R.xml.setting);
		proVersion();

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		themeColor = getResources().getColor(R.color.default_color);

		ActionBar act = getActionBar();
		act.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.settingTitle) + "</font>"));
		act.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
		if (API > 20) {
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
			act.setTitle(Html.fromHtml("<font color='" + themColorString + "'>" + getResources().getString(R.string.settingTitle) + "</font>"));

			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(themeColor);
			getWindow().setNavigationBarColor(themeColor);
		}

		stable = (SwitchPreference)findPreference("stable");
		autoremove = (SwitchPreference)findPreference("autoremove");
		autorecovs = (SwitchPreference)findPreference("autorecov");
		autotrans = (SwitchPreference)findPreference("hide");

		widget = (Preference)findPreference("widget");
		app = (Preference)findPreference("app");
		overview = (Preference)findPreference("overview");
		prefwifi = (Preference)findPreference("wifi");
		prefbluetooth = (Preference)findPreference("bluetooth");
		prefgps = (Preference)findPreference("gps");
		preftime = (Preference)findPreference("time");
		launcher = (Preference)findPreference("launcher");
		ad = (Preference)findPreference("ad_pref");

		String s = sharedPrefs.getString("sizes", "2");
		sizes = (ListPreference)findPreference("sizes");
		if(s.equals("1")){
			sizes.setSummary("SMALL");
		}
		else if(s.equals("2")){
			sizes.setSummary("MEDIUM");
		}
		else if(s.equals("3")){
			sizes.setSummary("LARGE");
		}

		style = (ListPreference)findPreference("apps");
		String t = sharedPrefs.getString("apps", "1");
		if(t.equals("1")){
			style.setSummary("List View");
		}
		else if(t.equals("2")){
			style.setSummary("Grid View");
		}

		colortext = (ListPreference)findPreference("textcolor");
		String c = sharedPrefs.getString("textcolor", "1");
		if(c.equals("1")){
			colortext.setSummary("Dark");
		}
		else if(c.equals("2")){
			colortext.setSummary("Light");
		}

		ad.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(Intent.ACTION_VIEW,
						Uri.parse(getResources().getString(R.string.link_shortcuts_pro)));
				startActivity(i);

				Toast.makeText(getApplicationContext(),
						Html.fromHtml("<font color='" + themeColor + "'>" + "Thanks" + "</font>"), Toast.LENGTH_SHORT).show();

				return true;
			}
		});

		app.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(Intent.ACTION_VIEW,
						Uri.parse(getResources().getString(R.string.link_rate)));
				startActivity(i);

				Toast.makeText(getApplicationContext(),
						Html.fromHtml("<font color='" + themeColor + "'>" + "Thanks" + "</font>"), Toast.LENGTH_SHORT).show();

				return true;
			}
		});
		overview.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getApplicationContext(), DetailHelper.class);
				startActivity(i);
				
				return true;
			}
		});
		prefwifi.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent w = new Intent(SettingGUI.this, WiFiScope.class);
				w.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(w);
				
				return true;
			}
		});
		prefbluetooth.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent b = new Intent(SettingGUI.this, BluetoothScope.class);
				b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(b);
				
				return true;
			}
		});
		prefgps.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent g = new Intent(SettingGUI.this, GPSScope.class);
				g.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(g);
				
				return true;
			}
		});
		preftime.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent t = new Intent(SettingGUI.this, TimeScope.class);
				t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(t);

				return true;
			}
		});

		launcher.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(Intent.ACTION_VIEW,
						Uri.parse(getResources().getString(R.string.link_launcher)));
				startActivity(i);

				Toast.makeText(getApplicationContext(),
						Html.fromHtml("<font color='" + themeColor + "'>" + "Thanks" + "</font>"), Toast.LENGTH_SHORT).show();

				return true;
			}
		});

		widget.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent w = new Intent(getApplicationContext(), WidgetHandler.class);
				w.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(w);

				return true;
			}
		});
	}

	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);

		LayerDrawable drawPref = (LayerDrawable)getResources().getDrawable(R.drawable.draw_pref);
		GradientDrawable backPref = (GradientDrawable) drawPref.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawWifi = (LayerDrawable)getResources().getDrawable(R.drawable.draw_wifi);
		GradientDrawable backWifi = (GradientDrawable) drawWifi.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawBluetooth = (LayerDrawable)getResources().getDrawable(R.drawable.draw_bluetooth);
		GradientDrawable backBluetooth = (GradientDrawable) drawBluetooth.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawGPS = (LayerDrawable)getResources().getDrawable(R.drawable.draw_gps);
		GradientDrawable backGPS = (GradientDrawable) drawGPS.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawTime = (LayerDrawable)getResources().getDrawable(R.drawable.draw_time);
		GradientDrawable backTime = (GradientDrawable) drawTime.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawGeekyLauncher = (LayerDrawable)getResources().getDrawable(R.drawable.draw_geekylauncher);
		RotateDrawable backGL = (RotateDrawable) drawGeekyLauncher.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawPro = (LayerDrawable)getResources().getDrawable(R.drawable.draw_pref_pro);
		GradientDrawable backPro = (GradientDrawable) drawPro.findDrawableByLayerId(R.id.backtemp);
		GradientDrawable backPro2 = (GradientDrawable) drawPro.findDrawableByLayerId(R.id.backtemp2);

		backPref.setColor(themeColor);
		backWifi.setColor(themeColor);
		backBluetooth.setColor(themeColor);
		backGPS.setColor(themeColor);
		backTime.setColor(themeColor);
		backPro.setColor(themeColor);	backPro2.setColor(themeColor);
		backGL.setDrawable(new ColorDrawable(themeColor));

		stable.setIcon(drawPref);
		widget.setIcon(drawPref);
		autoremove.setIcon(drawPref);
		autorecovs.setIcon(drawPref);
		autotrans.setIcon(drawPref);
		style.setIcon(drawPref);
		colortext.setIcon(drawPref);
		sizes.setIcon(drawPref);
		overview.setIcon(drawPref);
		prefwifi.setIcon(drawWifi);
		prefbluetooth.setIcon(drawBluetooth);
		prefgps.setIcon(drawGPS);
		preftime.setIcon(drawTime);
		launcher.setIcon(drawGeekyLauncher);
		ad.setIcon(drawPro);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);

		if(appInstalledOrNot("net.geekstools.geekylauncher") == true){
			stable.setChecked(true);
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent m = new Intent(SettingGUI.this, ListGrid.class);
		m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(m);	
		
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		MenuItem recov = menu.findItem(R.id.pref);
		MenuItem osp = menu.findItem(R.id.osp);
		MenuItem recovw = menu.findItem(R.id.recovw);

		LayerDrawable drawRecov = (LayerDrawable)getResources().getDrawable(R.drawable.draw_recov);
		GradientDrawable backRecov = (GradientDrawable) drawRecov.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawOSP = (LayerDrawable)getResources().getDrawable(R.drawable.draw_osp);
		GradientDrawable backOSP = (GradientDrawable) drawOSP.findDrawableByLayerId(R.id.backtemp);

		LayerDrawable drawAbout = (LayerDrawable)getResources().getDrawable(R.drawable.draw_recovw);
		GradientDrawable backAbout = (GradientDrawable) drawAbout.findDrawableByLayerId(R.id.backtemp);

		backRecov.setColor(themeColor);
		backOSP.setColor(themeColor);
		backAbout.setColor(themeColor);

		recov.setIcon(drawRecov);
		osp.setIcon(drawOSP);
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
			case R.id.osp: {
				Intent p = new Intent(getApplicationContext(), PurchaseActivity.class);
				p.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(p);

				break;
			}
			case R.id.pref:{
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		String s = sharedPreferences.getString("sizes", "NULL");
		sizes = (ListPreference)findPreference("sizes");
		if(s.equals("1")){
			sizes.setSummary("SMALL");
		}
		else if(s.equals("2")){
			sizes.setSummary("MEDIUM");
		}
		else if(s.equals("3")){
			sizes.setSummary("LARGE");
		}
		
		style = (ListPreference)findPreference("apps");
		String t = sharedPrefs.getString("apps", "1");
		if(t.equals("1")){
			style.setSummary("List View");
		}
		else if(t.equals("2")){
			style.setSummary("Grid View");
		}
		
		colortext = (ListPreference)findPreference("textcolor");
		String c = sharedPrefs.getString("textcolor", "1");
		if(c.equals("1")){
			colortext.setSummary("Dark");
		}
		else if(c.equals("2")){
			colortext.setSummary("Light");
		}

		if(sharedPreferences.getBoolean("stable", true) == true){
			//StartBackgroundMainService
			MainScope.Stable = true;
			startService(new Intent(getApplicationContext(), BindServices.class));
		}
		else if(sharedPreferences.getBoolean("stable", true) == false){
			//StartBackgroundMainService
			MainScope.Stable = false;
			stopService(new Intent(getApplicationContext(), BindServices.class));
		}
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

	private void proVersion() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(SettingGUI.this);
		builder.setTitle(getResources().getString(R.string.geeksempire));
		builder.setMessage(getString(R.string.promo_dialogue_text));
		builder.setIcon(R.drawable.ic_about_small);

		builder.setPositiveButton(getString(R.string.pro_version), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(getResources().getString(R.string.link_shortcuts_pro)));
				startActivity(browserIntent);

				Toast.makeText(getApplicationContext(),
						Html.fromHtml("<font color='#A6D3CF'>" + "Thanks" + "</font>"), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(getString(R.string.donate), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(getApplicationContext(), PurchaseActivity.class);
				startActivity(i);

				Toast.makeText(getApplicationContext(),
						Html.fromHtml("<font color='#A6D3CF'>" + "Thanks" + "</font>"), Toast.LENGTH_SHORT).show();

				dialog.dismiss();
			}
		});
		builder.setNeutralButton(getString(R.string.osp_text), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(Intent.ACTION_VIEW,
						Uri.parse(getResources().getString(R.string.link_osp)));
				startActivity(i);

				Toast.makeText(getApplicationContext(),
						Html.fromHtml("<font color='#A6D3CF'>" + "Thanks" + "</font>"), Toast.LENGTH_SHORT).show();

				dialog.dismiss();
			}
		});
		builder.show();
	}
}
