package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StopAll extends Activity{

	@Override
	protected void onCreate(Bundle Saved){
		super.onCreate(Saved);

		stopService(new Intent(getApplicationContext(), Floating_one.class));
		stopService(new Intent(getApplicationContext(), Floating_two.class));
		stopService(new Intent(getApplicationContext(), Floating_three.class));
		stopService(new Intent(getApplicationContext(), Floating_four.class));
		stopService(new Intent(getApplicationContext(), Floating_five.class));
		stopService(new Intent(getApplicationContext(), Floating_six.class));
		stopService(new Intent(getApplicationContext(), Floating_seven.class));
		stopService(new Intent(getApplicationContext(), Floating_eight.class));
		stopService(new Intent(getApplicationContext(), Floating_nine.class));
		stopService(new Intent(getApplicationContext(), Floating_ten.class));

		stopService(new Intent(getApplicationContext(), Widget_Floating_one.class));
		stopService(new Intent(getApplicationContext(), Widget_Floating_two.class));
		stopService(new Intent(getApplicationContext(), Widget_Floating_three.class));

		stopService(new Intent(getApplicationContext(), Widget_Mini_one.class));
		stopService(new Intent(getApplicationContext(), Widget_Mini_two.class));
		stopService(new Intent(getApplicationContext(), Widget_Mini_three.class));

		MainScope.one = false;
		MainScope.two = false;
		MainScope.three = false;
		MainScope.four = false;
		MainScope.five = false;
		MainScope.six = false;
		MainScope.seven = false;
		MainScope.eight = false;
		MainScope.nine = false;
		MainScope.ten = false;

		MainScope.oneS = false;
		MainScope.twoS = false;
		MainScope.threeS = false;
		MainScope.fourS = false;
		MainScope.fiveS = false;
		MainScope.sixS = false;
		MainScope.sevenS = false;
		MainScope.eightS = false;
		MainScope.nineS = false;
		MainScope.tenS = false;

		MainScope.oneW = false;
		MainScope.twoW = false;
		MainScope.threeW = false;

		finish();
	}
}
