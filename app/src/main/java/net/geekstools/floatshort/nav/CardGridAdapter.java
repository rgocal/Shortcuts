package net.geekstools.floatshort.nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.geekstools.floatshort.Floating_eight;
import net.geekstools.floatshort.Floating_five;
import net.geekstools.floatshort.Floating_four;
import net.geekstools.floatshort.Floating_nine;
import net.geekstools.floatshort.Floating_one;
import net.geekstools.floatshort.Floating_seven;
import net.geekstools.floatshort.Floating_six;
import net.geekstools.floatshort.Floating_ten;
import net.geekstools.floatshort.Floating_three;
import net.geekstools.floatshort.Floating_two;
import net.geekstools.floatshort.MainScope;
import net.geekstools.floatshort.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CardGridAdapter extends BaseAdapter {

	private static Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	private final static String regexPack = "\\*(.*?)\\*";
	private final static String regexName = "\\[(.*?)\\]";
	private final static String deleteStar = "*";
	private final static String deleteBrktL = "[";
	private final static String deleteBrktR = "[";

	String PackageName;
	SharedPreferences sharedPrefs;

	public CardGridAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.fav_card_grid, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtDesc = (TextView) convertView.findViewById(R.id.desc);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String color = sharedPrefs.getString("textcolor", "1");
		if(color.equals("1")){
			//Default Color #222222
		}
		else if(color.equals("2")){
			txtTitle.setTextColor(Color.parseColor("#EEEEEE"));
			txtDesc.setTextColor(Color.parseColor("#EEEEEE"));
			txtCount.setTextColor(Color.parseColor("#EEEEEE"));
		}

		imgIcon.setImageDrawable(navDrawerItems.get(position).getIcon());
		txtTitle.setText(navDrawerItems.get(position).getTitle());
		txtDesc.setText(navDrawerItems.get(position).getDesc());
		txtCount.setText(navDrawerItems.get(position).getCount());

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		MainScope.alpha = 133;
		MainScope.opacity = 255;
		if(sharedPrefs.getBoolean("hide", false) == false){
			MainScope.hide = false;
		}
		else if(sharedPrefs.getBoolean("hide", false) == true){
			MainScope.hide = true;
		}
		//Shortcuts Size
		String s = sharedPrefs.getString("sizes", "2");
		if(s.equals("1")){
			MainScope.size = 24;
		}
		else if(s.equals("2")){
			MainScope.size = 36;
		}
		else if(s.equals("3")){
			MainScope.size = 48;
		}
		MainScope.HW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MainScope.size, context.getResources().getDisplayMetrics());

		imgIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PackageName = navDrawerItems.get(position).getTitle();
				runService(PackageName);
				saveServiceFile(PackageName);
			}
		});

		return convertView;
	}

	/***********************Functions**************************/
	public static void runService(String packName){
		try {
			/*Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);*/
			if(MainScope.one == false){
				Intent s = new Intent(context, Floating_one.class);
				s.putExtra("pack", packName);
				context.startService(s);

				MainScope.one = true;
			}
			else if(MainScope.one = true){
				//ONE is Busy!
				if (MainScope.two == false){
					Intent s = new Intent(context, Floating_two.class);
					s.putExtra("pack", packName);
					context.startService(s);

					MainScope.two = true;
				}
				else if(MainScope.two == true){
					//TWO is Busy!
					if (MainScope.three == false){
						Intent s = new Intent(context, Floating_three.class);
						s.putExtra("pack", packName);
						context.startService(s);

						MainScope.three = true;
					}
					else if(MainScope.three == true){
						//THREE is Busy!
						if (MainScope.four == false){
							Intent s = new Intent(context, Floating_four.class);
							s.putExtra("pack", packName);
							context.startService(s);

							MainScope.four = true;
						}
						else if(MainScope.four == true){
							//FOUR is Busy!
							if (MainScope.five == false){
								Intent s = new Intent(context, Floating_five.class);
								s.putExtra("pack", packName);
								context.startService(s);

								MainScope.five = true;
							}
							else if(MainScope.five == true){
								//FIVE is Busy!
								if (MainScope.six == false){
									Intent s = new Intent(context, Floating_six.class);
									s.putExtra("pack", packName);
									context.startService(s);

									MainScope.six = true;
								}
								else if(MainScope.six == true){
									//SIX is Busy!
									if (MainScope.seven == false){
										Intent s = new Intent(context, Floating_seven.class);
										s.putExtra("pack", packName);
										context.startService(s);

										MainScope.seven = true;
									}
									else if(MainScope.seven == true){
										//SEVEN is Busy!
										if (MainScope.eight == false){
											Intent s = new Intent(context, Floating_eight.class);
											s.putExtra("pack", packName);
											context.startService(s);

											MainScope.eight = true;
										}
										else if(MainScope.eight == true){
											//EIGHT is Busy!
											if (MainScope.nine == false){
												Intent s = new Intent(context, Floating_nine.class);
												s.putExtra("pack", packName);
												context.startService(s);

												MainScope.nine = true;
											}
											else if(MainScope.nine == true){
												//NINE is Busy!
												if (MainScope.ten == false){
													Intent s = new Intent(context, Floating_ten.class);
													s.putExtra("pack", packName);
													context.startService(s);

													MainScope.ten = true;
												}
												else if(MainScope.ten == true){
													//TEN is Busy!
													Toast.makeText(context, "Long Press On Icon To Release A Service", Toast.LENGTH_SHORT).show();
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void saveServiceFile(String packName){
		try {
			//Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
			if(MainScope.oneS == false){
				savePackName(packName, 1);

				MainScope.oneS = true;
			}
			else if(MainScope.oneS = true){
				//ONE is Busy!
				if (MainScope.twoS == false){
					savePackName(packName, 2);

					MainScope.twoS = true;
				}
				else if(MainScope.twoS == true){
					//TWO is Busy!
					if (MainScope.threeS == false){
						savePackName(packName, 3);

						MainScope.threeS = true;
					}
					else if(MainScope.threeS == true){
						//THREE is Busy!
						if (MainScope.fourS == false){
							savePackName(packName, 4);

							MainScope.fourS = true;
						}
						else if(MainScope.fourS == true){
							//FOUR is Busy!
							if (MainScope.fiveS == false){
								savePackName(packName, 5);

								MainScope.fiveS = true;
							}
							else if(MainScope.fiveS == true){
								//FIVE is Busy!
								if (MainScope.sixS == false){
									savePackName(packName, 6);

									MainScope.sixS = true;
								}
								else if(MainScope.sixS == true){
									//SIX is Busy!
									if (MainScope.sevenS == false){
										savePackName(packName, 7);

										MainScope.sevenS = true;
									}
									else if(MainScope.sevenS == true){
										//SEVEN is Busy!
										if (MainScope.eightS == false){
											savePackName(packName, 8);

											MainScope.eightS = true;
										}
										else if(MainScope.eightS == true){
											//EIGHT is Busy!
											if (MainScope.nineS == false){
												savePackName(packName, 9);

												MainScope.nineS = true;
											}
											else if(MainScope.nineS == true){
												//NINE is Busy!
												if (MainScope.tenS == false){
													savePackName(packName, 10);

													MainScope.tenS = true;
												}
												else if(MainScope.tenS == true){
													//TEN is Busy!
													//Return
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	//Save packName to Separate Files
	public void savePackName(String pack, int i){
		try {
			String fileName = ".File" + i;
			FileOutputStream fOut = context.openFileOutput(fileName, context.MODE_PRIVATE);
			fOut.write((pack).getBytes());

			System.out.println("DONE " + i + " >> " + pack);

			fOut.close();
			fOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
}
