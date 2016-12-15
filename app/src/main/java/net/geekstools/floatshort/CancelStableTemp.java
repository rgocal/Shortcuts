package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CancelStableTemp extends Activity{

    @Override
    protected void onCreate(Bundle Saved){
        super.onCreate(Saved);

        stopService(new Intent(getApplicationContext(), BindServices.class));

        finish();
    }
}
