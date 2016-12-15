package net.geekstools.floatshort;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPSScope extends ListActivity {

	int size, Position, counter;
	public static int HW = 0;
	public static int alpha = 133;
	public static int opacity = 255;
	
	private PackageManager packageManager = null;
	private List<ApplicationInfo> applist = null;
	private AdaptorScope listadaptor = null;
	ListView lv;

	TextView autoInfo;
	Button deleteAuto;
	RelativeLayout autoLayout;

	static boolean hide = false;
	static boolean one, two, three, four, five, six, seven, eight, nine, ten = false;

	int themeColor, themeTextColor;
	String themColorString;
	Palette currentColor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_scope);

		themeColor = getResources().getColor(R.color.default_color);

		ActionBar actBar = getActionBar();
		actBar.setTitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.gps) + "</font>"));
		actBar.setSubtitle(Html.fromHtml("<font color='" + themeColor + "'>" + getResources().getString(R.string.gps_desc) + "</font>"));
		actBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

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

			actBar.setTitle(Html.fromHtml("<font color='" + themColorString + "'>" + getResources().getString(R.string.gps) + "</font>"));
			actBar.setSubtitle(Html.fromHtml("<font color='" + themColorString + "'>" + getResources().getString(R.string.gps_desc) + "</font>"));

			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(themeColor);
			getWindow().setNavigationBarColor(themeColor);
		}

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		autoInfo = (TextView)findViewById(R.id.autoInfo);
		deleteAuto = (Button)findViewById(R.id.delete_auto);
		autoLayout = (RelativeLayout)findViewById(R.id.autoLayout);
		final File p1 = getApplicationContext().getFileStreamPath(".GPSPack1");
		final File p2 = getApplicationContext().getFileStreamPath(".GPSPack2");

		if(p1.exists() || p2.exists()){
			autoInfo.setText("");

			Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anm);
			autoLayout.startAnimation(anim);
			autoLayout.setVisibility(View.VISIBLE);

			if(p1.exists() && p2.exists()){
				autoInfo.append(appName(READ(".GPSPack1", getApplicationContext())) + "\n\n");
				autoInfo.append(appName(READ(".GPSPack2", getApplicationContext())));
			}
			else if(p1.exists()){
				autoInfo.append(appName(READ(".GPSPack1", getApplicationContext())));
			}
			else if(p2.exists()){
				autoInfo.append(appName(READ(".GPSPack2", getApplicationContext())));
			}

			deleteAuto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(p1.exists() || p2.exists()){
						if(p1.exists() && p2.exists()){
							p1.delete();
							p2.delete();
						}
						else if(p1.exists()){
							p1.delete();
						}
						else if(p2.exists()){
							p2.delete();
						}
					}

					Animation animGone = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
					autoLayout.setVisibility(View.GONE);
					autoLayout.startAnimation(animGone);
				}
			});
		}

		/*Auto Hide*/
		if(sharedPrefs.getBoolean("hide", false) == false){
			hide = false;
		}
		else if(sharedPrefs.getBoolean("hide", false) == true){
			hide = true;
		}

		/*Shortcuts Size*/
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
		counter = 1;

		packageManager = getPackageManager();
		new LoadApplications().execute();
	}

	@Override
	public void onResume(){
		super.onResume();

		lv = getListView();
		registerForContextMenu(lv);
	}

	@Override
	public void onPause(){
		/**
		 ***
		 **/
		super.onPause();
	}

	@Override
	public void onDestroy(){
		/**
		 ***
		 **/
		super.onDestroy();
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
				Intent b = new Intent(Intent.ACTION_VIEW, Uri.parse("http://geosp.geeksempire.net/"));
				b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(b);

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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if(counter == 1){
			ApplicationInfo app = applist.get(position);
			try {
				String pack = (app.packageName);
				
				FileOutputStream fOut = openFileOutput(".GPSPack1", MODE_PRIVATE);
				fOut.write((pack).getBytes());
				
				System.out.println("DONE 1>> " + pack);
				
				fOut.close();
				fOut.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}
			finally{
				counter = 2;
			}
		}
		else if(counter == 2){
			ApplicationInfo app = applist.get(position);
			try {
				String packLast = (app.packageName);
				
				FileOutputStream fOut = openFileOutput(".GPSPack2", MODE_PRIVATE);
				fOut.write((packLast).getBytes());
				
				System.out.println("DONE 2>> " + packLast);
				
				fOut.close();
				fOut.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}
			finally{
				Toast.makeText(getApplicationContext(), "Re-Click to Replace Apps", Toast.LENGTH_LONG).show();
				counter = 1;
			}
		}

		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anm);
		autoLayout.startAnimation(anim);
		autoLayout.setVisibility(View.VISIBLE);

		ApplicationInfo app = applist.get(position);
		autoInfo.setGravity(Gravity.CENTER);
		autoInfo.setText(appName(app.packageName));
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
		protected Void doInBackground(Void... params) {
			applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			listadaptor = new AdaptorScope(GPSScope.this, R.layout.snippet_list_row, applist);
			
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(listadaptor);
			
			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(GPSScope.this, null, "Loading Applications Info...");
			
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
	
	
	/*****************Functions********************/
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
