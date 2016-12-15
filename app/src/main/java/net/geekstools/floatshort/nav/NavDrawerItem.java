package net.geekstools.floatshort.nav;

import android.graphics.drawable.Drawable;

public class NavDrawerItem {
	
	String title;
	String desc;
	Drawable icon;
	String count = "0";
	boolean isCounterVisible = false;
	//public Animation Anm;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, Drawable icon){
		this.title = title;
		this.icon = icon;
	}
	
	public NavDrawerItem(String title, Drawable icon, boolean isCounterVisible, String count, String desc /*Animation anim*/){
		this.title = title;
		this.desc = desc;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	//	this.Anm = anim;
	}
	
	public String getTitle(){
		return this.title;
	}

	public String getDesc(){
		return this.desc;
	}
	
	public Drawable getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}

	public void setDesc(String Desc){
		this.desc = desc;
	}
	
	public void setIcon(Drawable icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
