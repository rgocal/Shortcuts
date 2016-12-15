package net.geekstools.floatshort;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusShare;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import net.geekstools.floatshort.SimpleGestureFilter.SimpleGestureListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class DetailHelper extends FragmentActivity implements SimpleGestureListener, GoogleApiClient.OnConnectionFailedListener {

	GoogleApiClient googleApiClient;
	PackageManager packageManager;

	RelativeLayout checkOverlayfull, checkStatfull, checkStoragefull, checkGPSfull, welcome, twitterFollow, plusCircle;
	TextView permissionInfo;
	Button share, gplus, twitter, skip;
	ImageView logo, checkOverlay, checkStat, checkStorage, checkGPS;

	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	private static final String TWITTER_KEY = "t5c3gatpUgMgRwIao4IGvDXWB";
	private static final String TWITTER_SECRET = "TtJ3mp5PFhTkBP5Knrt8uvVbP4ruUIaZRFYXoYyLlp46LHTSMc";

	int REQUEST_INVITE = 102;
	int REQUEST_SING_GOOGLE = 66, Google_Sign_In = -1;
	int REQUEST_PLUS_SHARE = 96;
	int API;

	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new TwitterCore(authConfig), new Twitter(authConfig), new TweetComposer());
		setContentView(R.layout.detail_helper);
		packageManager = getApplicationContext().getPackageManager();
		API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);

		if(API > 20){
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(Color.parseColor("#000000"));
			getWindow().setNavigationBarColor(Color.parseColor("#000000"));
		}

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();
		googleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		permissionInfo = (TextView)findViewById(R.id.PermissionsInfo);
		share = (Button)findViewById(R.id.share);
		gplus = (Button)findViewById(R.id.googleplus);
		twitter = (Button)findViewById(R.id.twitter);
		skip = (Button)findViewById(R.id.skip);
		checkOverlay = (ImageView) findViewById(R.id.checkOverlay);
		checkStat = (ImageView)findViewById(R.id.checkStat);
		checkStorage = (ImageView)findViewById(R.id.checkStorage);
		checkGPS = (ImageView)findViewById(R.id.checkGPS);
		checkOverlayfull = (RelativeLayout)findViewById(R.id.checkOverlayfull);
		checkStatfull = (RelativeLayout)findViewById(R.id.checkStatfull);
		checkStoragefull = (RelativeLayout)findViewById(R.id.checkStoragefull);
		checkGPSfull = (RelativeLayout)findViewById(R.id.checkGPSfull);
		welcome = (RelativeLayout)findViewById(R.id.welcome);
		plusCircle = (RelativeLayout)findViewById(R.id.gxplus);
		twitterFollow = (RelativeLayout)findViewById(R.id.gxtwitter);

		permissionInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(API > 22){
					if(Settings.canDrawOverlays(getApplicationContext())){
						Intent i = new Intent(getApplicationContext(), MainScopeOff.class);
						overridePendingTransition(R.anim.actanim, R.anim.actanim);
						startActivity(i);

						finish();
					}
				}
			}
		});

		if(API < 23){checkOverlay.setImageDrawable(getDrawable(R.drawable.ic_set_selector));	checkOverlay.setEnabled(false);}
		checkOverlayfull.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(API > 22){
					Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
							Uri.parse("package:" + getPackageName()));
					startActivity(intent);
				}
			}
		});

		if(API < 20){checkStat.setEnabled(false);}
		checkStatfull.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(API > 20){
					Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
					startActivity(intent);
				}
			}
		});

		if(API < 23){checkStorage.setImageDrawable(getDrawable(R.drawable.ic_set_selector));}
		checkStoragefull.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(API > 22){
					if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
						getPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
					}else{goToSetting();}

				} else{goToSetting();}
			}
		});

		if(API < 23){checkGPS.setImageDrawable(getDrawable(R.drawable.ic_set_selector));}
		checkGPSfull.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(API > 22){
					if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
						getPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
					}else{goToSetting();}
				}else{goToSetting();}
			}
		});

		share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Google_Sign_In = 2;
				signIn();
			}
		});

		twitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shareTwitter();
			}
		});

		gplus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Google_Sign_In = 1;
				signIn();
			}
		});

		LoadApplications loadInfo = new LoadApplications();
		loadInfo.execute();
	}
	@Override
	public void onStart(){
		super.onStart();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(welcome.isShown()){
					Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
					welcome.setVisibility(View.GONE);
					welcome.startAnimation(animation);
					if(API > 20){
						Window window = DetailHelper.this.getWindow();
						window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
						window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
						window.setStatusBarColor(Color.parseColor("#757575"));
						getWindow().setNavigationBarColor(Color.parseColor("#757575"));
					}
				}
			}
		}, 21000);

		plusCircle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_plus)));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		twitterFollow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_twitter)));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		skip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
				welcome.setVisibility(View.GONE);
				welcome.startAnimation(animation);
				if(API > 20){
					Window window = DetailHelper.this.getWindow();
					window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
					window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					window.setStatusBarColor(Color.parseColor("#757575"));
					getWindow().setNavigationBarColor(Color.parseColor("#757575"));
				}
			}
		});
	}
	@Override
	public void onResume(){
		super.onResume();

		permissionInfo.setText(Html.fromHtml(getString(R.string.guide)));
		int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
		if(API > 20){
			AppOpsManager appOps = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
			int mode = appOps.checkOp("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
			if(mode != AppOpsManager.MODE_ALLOWED) {
				permissionInfo.setText(Html.fromHtml(getString(R.string.usagestatError)));
				checkStat.setImageDrawable(getDrawable(R.drawable.ic_cancel_stable));
			}
			else if(mode == AppOpsManager.MODE_ALLOWED){
				checkStat.setImageDrawable(getDrawable(R.drawable.ic_set_selector));
			}
		}
		if(API > 22){
			getPermissions(Manifest.permission.INTERNET);
			getPermissions(Manifest.permission.CHANGE_WIFI_STATE);
			getPermissions(Manifest.permission.ACCESS_WIFI_STATE);
			getPermissions(Manifest.permission.WAKE_LOCK);
			getPermissions(Manifest.permission.BLUETOOTH);
			getPermissions(Manifest.permission.BLUETOOTH_ADMIN);
			getPermissions(Manifest.permission.VIBRATE);

			doAfterGrant(Manifest.permission.ACCESS_FINE_LOCATION);
			doAfterGrant(Manifest.permission.READ_EXTERNAL_STORAGE);

			if (!Settings.canDrawOverlays(getApplicationContext())) {
				permissionInfo.setText(Html.fromHtml(getString(R.string.overlayError)));
				checkOverlay.setImageDrawable(getDrawable(R.drawable.ic_cancel_stable));
			}
			else if(Settings.canDrawOverlays(getApplicationContext())){
				checkOverlay.setImageDrawable(getDrawable(R.drawable.ic_set_selector));
			}

            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                permissionInfo.setText(Html.fromHtml(getString(R.string.runtimeError)));
            }
		}
	}
	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	public void onSwipe(int direction) {
		switch (direction) {
			case SimpleGestureFilter.SWIPE_RIGHT:
				break;
			case SimpleGestureFilter.SWIPE_LEFT:
				break;
			case SimpleGestureFilter.SWIPE_DOWN:
				break;
			case SimpleGestureFilter.SWIPE_UP:
				break;
		}
	}
	@Override
	public void onDoubleTap() {}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(getPackageName(), "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

		if (requestCode == REQUEST_INVITE) {
			if (resultCode == RESULT_OK) {
				// Get the invitation IDs of all sent messages
				String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
				for (String id : ids) {
					Log.d(getPackageName(), "Sent Invitation To >> " + id);
				}
			}
			else {System.out.println("Invitation Failed >> " + data);}
		}
		else if(requestCode == REQUEST_SING_GOOGLE){
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if(result.isSuccess()){
				if(Google_Sign_In == 1){
					GoogleSignInAccount acct = result.getSignInAccount();
					System.out.println("Google Account >>" + result.getSignInAccount().getDisplayName());

					shareGooglePlus();
				}
				else if(Google_Sign_In == 2){
					onInviteClicked();
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

	/********************Functions*************************/
	private void onInviteClicked() {
		String color = getResources().getColor(R.color.android_def) +  "";
		Intent intent = new AppInviteInvitation
				.IntentBuilder(getString(R.string.invitation_title))
				.setMessage(getString(R.string.invitation_message))
				.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
				.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
				.build();
		startActivityForResult(intent, REQUEST_INVITE);
	}
	private void shareGooglePlus(){
		Intent shareIntent = new PlusShare.Builder(this)
				.setType("text/*")
				.setText("+GeeksEmpire" + "\n"  +getString(R.string.invitation_title) + "\n" + getString(R.string.invitation_message)
						+ "\n" + "#GeeksEmpire")
				.setContentUrl(Uri.parse(getString(R.string.link_shortcuts_pro)))
				.getIntent();

		startActivityForResult(shareIntent, REQUEST_PLUS_SHARE);
	}
	private void signIn() {
		Auth.GoogleSignInApi.revokeAccess(googleApiClient);

		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
		startActivityForResult(signInIntent, REQUEST_SING_GOOGLE);
	}
	private void shareTwitter(){
		Uri path = Uri.parse("android.resource://net.geekstools.floatshort.PRO/" + R.drawable.ic_launcher_pro_marsala);
		URL url = null;
		try {
			url = new URL(getString(R.string.link_shortcuts_pro));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		TweetComposer.Builder builder = new TweetComposer.Builder(this)
				.url(url)
				.text("@GeeksEmpire" + "\n" + getString(R.string.invitation_message)
						+ "\n" + "#GeeksEmpire")
				.image(path);
		builder.show();
	}

	/********************Apps Info*************************/
	private class LoadApplications extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA), getApplicationContext());
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

	/********************Permissions Setup*************************/
	final private int REQUEST_CODE_ASK_PERMISSIONS = 666;
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				if((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
						&& (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
				{
					try {
						String fileName = ".Permissions";
						FileOutputStream fOut = openFileOutput(fileName, MODE_PRIVATE);
						fOut.write((getPackageName().toString()).getBytes());

						fOut.close();
						fOut.flush();
					}
					catch (IOException e) {
						e.printStackTrace();
						System.out.println(e);
					}
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	public void getPermissions(final String Permission) {
		int hasPermission = checkSelfPermission(Permission);
		if (hasPermission != PackageManager.PERMISSION_GRANTED) {
			if (!shouldShowRequestPermissionRationale(Permission)) {
				requestPermissions(new String[] {Permission}, REQUEST_CODE_ASK_PERMISSIONS);
			}
			requestPermissions(new String[] {Permission}, REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}
		doAfterGrant(Permission);
	}
	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(DetailHelper.this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				})
				.create()
				.show();
	}
	private void doAfterGrant(String Perm) {
		if(checkSelfPermission(Perm) == PackageManager.PERMISSION_GRANTED){
			System.out.println(Perm + " Permissions Set");
			if(Perm.equals(Manifest.permission.READ_EXTERNAL_STORAGE)){checkStorage.setImageDrawable(getDrawable(R.drawable.ic_set_selector));}
			if(Perm.equals(Manifest.permission.ACCESS_FINE_LOCATION)){checkGPS.setImageDrawable(getDrawable(R.drawable.ic_set_selector));}
		}
	}
	public void goToSetting(){
		Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.setData(Uri.parse("package:" + getApplicationContext().getPackageName().toString()));
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivityForResult(i, 0);

		Toast.makeText(getApplicationContext(), getResources().getString(R.string.I_Need), Toast.LENGTH_LONG).show();
	}
}
