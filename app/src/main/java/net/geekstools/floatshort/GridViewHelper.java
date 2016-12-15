package net.geekstools.floatshort;


import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;


public class GridViewHelper {

	CharSequence pack;
	Intent intent;
	Drawable icon;
	boolean filtered;

	final void setActivity(ComponentName className, int launchFlags) {
		 intent = new Intent(Intent.ACTION_MAIN);
		 intent.addCategory(Intent.CATEGORY_LAUNCHER);
		 intent.setComponent(className);
		 intent.setFlags(launchFlags);
	 }

	 @Override
	 public boolean equals(Object o) {
		 if (this == o) {
			 return true;
		 }
		 if (!(o instanceof GridViewHelper)) {
			 return false;
		 }

		 GridViewHelper that = (GridViewHelper) o;

		 return 
				 pack.equals(that.pack) 
				 && 
				 intent.getComponent().getClassName().equals(that.intent.getComponent().getClassName());
	 }

	 @Override
	 public int hashCode() {
		 int result;
		 result = (pack != null ? pack.hashCode() : 0);
		 final String name = intent.getComponent().getPackageName();
		 result = 31 * result + (name != null ? name.hashCode() : 0);
		 return result;
	 }
}
