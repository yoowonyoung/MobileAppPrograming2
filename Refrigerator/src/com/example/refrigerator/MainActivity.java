package com.example.refrigerator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gcm.GCMRegistrar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //// 일반적인 UI형식(activity class)와 java 코드를 연결시켜주는 부분
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.coolBtn:
            	Intent intent = new Intent(this, CoolActivity.class);
            	startActivity(intent);
            	break;

            case R.id.ColdBtn:
                Intent intent1 = new Intent(this, ColdActivity.class);
                startActivity(intent1);
                break;
            
            case R.id.RecipeBtn:
                Intent intent2 = new Intent(this, IngredientActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public void registerGcm() {

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);

        if (regId.equals("")) {
            GCMRegistrar.register(this, "356070819593" );
        } else {
            Log.e("id", regId);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
