package net.geekstools.floatshort;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsReceiver extends BroadcastReceiver{

	private PackageManager packageManager = null;
	Context c;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		packageManager = context.getPackageManager();
		c = context;
		
		File f = context.getFileStreamPath(".AppInfo");
		if(f.exists()){
			context.deleteFile(".AppInfo");
		}
		
		LoadApplications app = new LoadApplications();
		app.execute();
	}
	
	private class LoadApplications extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA), c);

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
	
	private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list, Context c) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		int i = 0;

		Collections.sort(list, new ApplicationInfo.DisplayNameComparator(packageManager));
		for (ApplicationInfo info : list) {
			try {
				if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);
					
					saveAppInfo(i, info.packageName, appName(info.packageName, c), c);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return applist;
	}
	
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
	
	public String appName(String pack, Context c){
		String Name = null;
		
		try{
			PackageManager packManager = c.getPackageManager();
			ApplicationInfo app = c.getPackageManager().getApplicationInfo(pack, 0);
			Name = packManager.getApplicationLabel(app).toString();
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return Name;
	}
}
