package in.digitechlab.restaurantlocator;

/**
 * Created by DELL on 10/15/2017.
 */

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.robohorse.gpversionchecker.GPVersionChecker;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

/*        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },101);

            Toast.makeText(SplashActivity.this, "Failed to get device location", Toast.LENGTH_SHORT).show();

            //return;

            //Code for App Restart
            Intent mStartActivity = new Intent(this, MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }*/

        // new GPVersionChecker.Builder(this).create();

        Thread t = new Thread(){

            public void run(){

                try{
                    sleep(2000);
                }catch (InterruptedException e){
                }
                finally{
                    Intent i = new Intent ("android.intent.action.MAINRESTACTIVITY");
                    startActivity(i);
                    finish();
                }
            }
        };
        t.start();

    }

}