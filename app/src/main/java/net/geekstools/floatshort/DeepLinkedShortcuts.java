package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinkedShortcuts extends Activity{

    Context context;
    String appName, packageName;
    String[] data = new String[1000];
    String[] dataNew;
    String[] FavDrawer;

    private final static String regexPack = "\\*(.*?)\\*";
    private final static String deleteStar = "*";

    @Override
    protected void onCreate(Bundle Saved){
        super.onCreate(Saved);
        context = getApplicationContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!sharedPreferences.getBoolean("stable", true)){
            Toast.makeText(getApplicationContext(), getString(R.string.app_index_error), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String incomingURI = getIntent().getDataString();
        appName = incomingURI.substring(incomingURI.lastIndexOf("/") + 1);  System.out.println("AppName >> " + appName);

        File f = getApplicationContext().getFileStreamPath(".AppInfo");
        if(!f.exists()){
            finish();
            return;
        }

        //load content
        try{
            FileInputStream fin = new FileInputStream(getApplicationContext().getFileStreamPath(".AppInfo"));
            DataInputStream myDIS = new DataInputStream(fin);

            String line = "";
            int i = 0;
            while ( (line = myDIS.readLine()) != null) {
                data[i] = line;
                i++;
            }

            dataNew = new String[i];
            System.arraycopy(data, 0, dataNew, 0, i);
            data = dataNew;

            /*************************************************************************************************/
            FavDrawer = new String[i];
            System.arraycopy(data, 0, FavDrawer, 0, i);
            data = FavDrawer;

            Pattern p = Pattern.compile(regexPack);
            int count = countLine();
            for(int navItem = 0; navItem < count; navItem++){
                if(FavDrawer[navItem].contains(appName)){
                    Matcher m = p.matcher(FavDrawer[navItem]);
                    if(m.find()){
                        packageName = m.group().replace(deleteStar, "");    System.out.println("Package >> " + packageName);
                        runService(packageName);
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Error: No Favorite\n" + e);
            finish();
        }

        finish();
    }

    /**************************************/
    public int countLine(){
        int nLines = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(getApplicationContext().getFileStreamPath(".AppInfo")));

            while (reader.readLine() != null){
                nLines++;
            }
            System.out.println("Count of Line: " + nLines);
            reader.close();
        }
        catch(Exception e){
            System.out.println("ERROR Line Counting\n" + e);
        }

        return nLines;
    }
    public void runService(String packName){
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
}
