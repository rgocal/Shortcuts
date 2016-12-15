package net.geekstools.floatshort;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adaptor extends ArrayAdapter<ApplicationInfo> {
	private List<ApplicationInfo> appsList = null;
	private Context context;
	private PackageManager packageManager;

public Adaptor(Context context, int textViewResourceId, List<ApplicationInfo> appsList) {
	super(context, textViewResourceId, appsList);
	this.context = context;
	this.appsList = appsList;
	packageManager = context.getPackageManager();
}

	@Override
	public int getCount() {
		return ((null != appsList) ? appsList.size() : 0);
}

	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != appsList) ? appsList.get(position) : null);
}

	@Override
	public long getItemId(int position) {
		return position;
}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
			if (null == view) {
				LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(R.layout.snippet_list_row, null);
			}

			ApplicationInfo data = appsList.get(position);
			PackageInfo pckInfo = null;
			try {
				pckInfo = packageManager.getPackageInfo(data.packageName, 0);
			}
			catch (NameNotFoundException e) {e.printStackTrace();}

			if (null != data) {
				TextView appName = (TextView) view.findViewById(R.id.app_name);
				TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
				TextView version = (TextView)view.findViewById(R.id.version);
				ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
				
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
				String color = sharedPrefs.getString("textcolor", "1");
				if(color.equals("1")){
					//Default Color #222222
				}
				else if(color.equals("2")){
					appName.setTextColor(Color.parseColor("#EEEEEE"));
					packageName.setTextColor(Color.parseColor("#EEEEEE"));
					version.setTextColor(Color.parseColor("#EEEEEE"));
				}
				
				appName.setText(data.loadLabel(packageManager));
				packageName.setText(data.packageName);
				version.setText(pckInfo.versionName);
				iconview.setImageDrawable(data.loadIcon(packageManager));
			}
			return view;
	}
}