package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LauncherHandler extends Activity{

	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);

		Intent i = getIntent();
		String launcherHandler = i.getStringExtra("launcher");

		if(launcherHandler.equals("settings")){
			Intent s = new Intent(LauncherHandler.this, SettingGUI.class);
			startActivity(s);
		}
		else if(launcherHandler.equals("widgets")){
			Intent w = new Intent(LauncherHandler.this, WidgetHandler.class);
			startActivity(w);
		}

		finish();
	}
}
