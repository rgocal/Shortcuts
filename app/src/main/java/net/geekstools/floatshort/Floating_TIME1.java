package net.geekstools.floatshort;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class Floating_TIME1 extends Service {


	private WindowManager windowManagerT1;
	private ImageView chatHead;
	MediaPlayer mediaPlayer = null;
	
	Context context;
	
	boolean b = false;
	boolean run = true;
	boolean trans = false;
	
	String pack;
	Drawable appIcon;
	int iconColor;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId){
       pack = intent.getStringExtra("pack");
       System.out.println(pack);
		if(appInstalledOrNot(pack) == false){stopSelf();	return 0;}
       
       PackageManager manager = this.getPackageManager();
		try {
			appIcon = manager.getApplicationIcon(pack);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		int API = android.os.Build.VERSION.SDK_INT;		System.out.println("API: " +  API);
		if (API > 20) {
			Palette currentColor;
			final Drawable currentWallpaper = appIcon;
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

				iconColor = currentColor.getVibrantColor(defaultColor);
			}
			catch (NullPointerException e){
				System.out.println(e);

				iconColor = getResources().getColor(R.color.default_color);
			}
		}

		if(TimeHelper1.hide == true){
			trans = true;
		}

		if(trans == true){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					appIcon.setAlpha(TimeHelper1.alpha);
				}
			}, 2000);
		}

		windowManagerT1 = (WindowManager) getSystemService(WINDOW_SERVICE);
		chatHead = new ImageView(this);
		chatHead.setBackground(appIcon);

		//Shortcuts Size
		int size = 0, HW = 0;
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				HW,
				HW,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.START;
		params.x = 0;
		params.y = 100;

		windowManagerT1.addView(chatHead, params);
		chatHead.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				//
			}
		});

		try {
			chatHead.setOnTouchListener(new View.OnTouchListener() {

				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(trans == true){
						appIcon.setAlpha(TimeHelper1.opacity);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								appIcon.setAlpha(TimeHelper1.alpha);
							}
						}, 3000);
					}

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						System.out.println("Touch ACTION_DOWN");

						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						System.out.println("Touch ACTION_UP");

						break;
					case MotionEvent.ACTION_MOVE:
						System.out.println("Touch ACTION_MOVE");

						paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
						windowManagerT1.updateViewLayout(chatHead, paramsF);
						break;
					}
					return false;
				}
			});
			chatHead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(TimeHelper1.time1 == false){
						stopService(new Intent(getApplicationContext(), Floating_TIME1.class));
						run = false;

						return;
					}
					else{
						Toast.makeText(getApplicationContext(), pack, Toast.LENGTH_SHORT).show();

						Intent i = new Intent(Intent.ACTION_MAIN);
			    		PackageManager manager = getPackageManager();
			    		i = manager.getLaunchIntentForPackage(pack);
			    		i.addCategory(Intent.CATEGORY_LAUNCHER);
			    		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    		startActivity(i);
					}
				}
			});
			chatHead.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					TimeHelper1.time1 = false;

					LayerDrawable drawPref = (LayerDrawable)getResources().getDrawable(R.drawable.draw_close_service);
					GradientDrawable backPref = (GradientDrawable) drawPref.findDrawableByLayerId(R.id.backtemp);
					backPref.setColor(iconColor);
					chatHead.setImageDrawable(drawPref);

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if(run == true){
								TimeHelper1.time1 = true;

								chatHead.setImageDrawable(null);
							}
						}
					}, 2000);

					return true;
				}
			});
		}
		catch (Exception e) {
			System.out.println(e);
		}

       return START_NOT_STICKY;
    }

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

		Uri notificationUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mediaPlayer = MediaPlayer.create(getApplicationContext(), notificationUri);
		mediaPlayer.start();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.stop();
			}
		}, 15000);

		System.out.println("time1");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(chatHead == null){return;}
		if (chatHead.isShown()) {
			windowManagerT1.removeView(chatHead);

			TimeHelper1.time1 = false;
			System.out.println("Channel time1 Reset");
		}
	}

	private boolean appInstalledOrNot(String uri) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		}
		catch (NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed ;
	}
}
