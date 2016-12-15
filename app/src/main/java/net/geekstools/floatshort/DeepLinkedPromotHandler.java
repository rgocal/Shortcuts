package net.geekstools.floatshort;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;

public class DeepLinkedPromotHandler extends Activity {

    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linked_promot);
        System.out.println(this.getClass().getName());

        startActivity(new Intent(getApplicationContext(), DetailHelper.class));
        finish();
    }
}
