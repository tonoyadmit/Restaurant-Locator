package in.digitechlab.restaurantlocator;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.robohorse.gpversionchecker.GPVersionChecker;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double latitude;
    private double longitude;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView addressTV;
    private Button getRest;
    private boolean isConnected;
    private boolean isGPSOn;
    private Boolean isBackPressed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GPVersionChecker.Builder(this).create();

        geocoder = new Geocoder(this);

        addressTV = (TextView) findViewById(R.id.addressTV);
        getRest = (Button) findViewById(R.id.btnNext);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for(Location location : locationResult.getLocations()){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                        try {
                            addresses = geocoder.getFromLocation(latitude,longitude,1);
                            String subarea = addresses.get(0).getSubLocality();
                            String division = addresses.get(0).getAdminArea();
                            String city = addresses.get(0).getLocality();
                            String postalCode = addresses.get(0).getPostalCode();
                            String country = addresses.get(0).getCountryName();


                        addressTV.setText(subarea+", "+city+" - "+postalCode+",\n"+
                                division+", "+country);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        getRest.setOnClickListener(this);

    }


    private void checkPermission(){

        isGPSOn=true;

        //Code for Menifest permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },101);

            Toast.makeText(MainActivity.this, "Failed to get device location", Toast.LENGTH_SHORT).show();

            //return;

            //Code for App Restart
            Intent mStartActivity = new Intent(this, MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            this.finish();
        }

        //code for GPS check
        //if(addressTV.getText()==getResources().getString(R.string.gps_notification)){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(MainActivity.this, "Please Switch on GPS", Toast.LENGTH_SHORT).show();
            isGPSOn=false;
        }

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        checkPermission();

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onClick(View view) {

        checkPermission();

        //Code to check Internet Connection
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Intent requestLink = new Intent(MainActivity.this, LocatorActivity.class);
        Bundle b1=new Bundle();
        b1.putDouble("lat1", latitude);
        b1.putDouble("lng1", longitude);
        requestLink.putExtras(b1);


        if(isConnected==true&&isGPSOn==true&&addressTV.getText()!=getResources().getString(R.string.gps_notification))
        {
            startActivity(requestLink);
        }
        else if(isConnected==true&&isGPSOn==false&&addressTV.getText()!=getResources().getString(R.string.gps_notification))
        {
            startActivity(requestLink);
        }
        else if (isConnected==true&&isGPSOn==false&&addressTV.getText()==getResources().getString(R.string.gps_notification))
        {
            Toast.makeText(MainActivity.this, "GPS is Switched Off", Toast.LENGTH_SHORT).show();
        }
        else if (isConnected==false&&isGPSOn==true)
        {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else if (isConnected==false&&isGPSOn==false)
        {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else  {
            Toast.makeText(MainActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
        }

    }
}
