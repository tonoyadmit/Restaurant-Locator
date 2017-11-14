
package in.digitechlab.restaurantlocator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Double.valueOf;
import static java.lang.Math.round;

public class LocatorActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double latitude;
    private double longitude;
    private Geocoder geocoder;
    private RecyclerView rRecyclerView;
    private List<Address> addresses;
    private NearbyPlaceApiService nearbyPlaceApiService;
    private TextView restaurant_result, lonTV, addressTV;
    private ImageView image1;
    private String placeType = "restaurant";
    private RestaurantAdapter rAdapter;
    private ArrayList<NearbyPlaceResponse> RestList;
    private Boolean isBackPressed=false;
    private int distance = 3000;
    private String range = "3.0 KM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);


        Bundle b2 = getIntent().getExtras();
        if (b2!=null) {
        latitude = b2.getDouble("lat1");
        longitude = b2.getDouble("lng1");
        //Toast.makeText(this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();
    }

        restaurant_result = (TextView) findViewById(R.id.restaurant_details);
        rRecyclerView = (RecyclerView) findViewById(R.id.recycler_rest);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rRecyclerView.setLayoutManager(llm);
        rAdapter = new RestaurantAdapter(this);
        rRecyclerView.setAdapter(rAdapter);
        RestList = new ArrayList<NearbyPlaceResponse>();

        initializeNetworkLibrary();

        getNearbyPlaceResponse();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.dist1k:
                this.distance = 500;
                this.range = "500 METER";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist2k:
                this.distance = 1000;
                this.range = "1.0 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist3k:
                this.distance = 1500;
                this.range = "1.5 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist4k:
                this.distance = 2000;
                this.range = "2.0 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist5k:
                this.distance = 3000;
                this.range = "3.0 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist6k:
                this.distance = 5000;
                this.range = "5.0 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist7k:
                this.distance = 10000;
                this.range = "10.0 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
            case R.id.dist8k:
                this.distance = 25000;
                this.range = "25.0 KM";
                initializeNetworkLibrary();
                getNearbyPlaceResponse();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private void getNearbyPlaceResponse() {

        String urlString = String.format("json?location=%f,%f&radius=%d&type=%s&key=%s",latitude,longitude,distance,placeType,BuildConfig.NEARBY_API_KEY);

        Call<NearbyPlaceResponse.WeatherResult>nearbyPlaceResponseCall = nearbyPlaceApiService.getNearbyResponse(urlString);

        nearbyPlaceResponseCall.enqueue(new Callback<NearbyPlaceResponse.WeatherResult>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse.WeatherResult> call, Response<NearbyPlaceResponse.WeatherResult> response) {
                if(response.code() == 200){
                    NearbyPlaceResponse.WeatherResult nearbyPlaceResponse = response.body();

                    Toast.makeText(LocatorActivity.this, "Query Successful", Toast.LENGTH_SHORT).show();

                    if(nearbyPlaceResponse.getResults().size()==0)
                    {
                        Toast.makeText(LocatorActivity.this, "No Restaurent Found\nTry Increasing Range", Toast.LENGTH_SHORT).show();
                    }

                    rAdapter.rest_lat1 = latitude;
                    rAdapter.rest_long1 = longitude;

                    rAdapter.setRestList(nearbyPlaceResponse.getResults());

                    restaurant_result.setText("NEARBY RESTAURANTS IN "+range);


/*                    Location loc1 = new Location("");
                    loc1.setLatitude(nearbyPlaceResponse.getResults().get(1).getGeometry().getLocation().getLat());
                    loc1.setLongitude(nearbyPlaceResponse.getResults().get(1).getGeometry().getLocation().getLng());

                    Location loc2 = new Location("");
                    loc2.setLatitude(valueOf(latitude).doubleValue());
                    loc2.setLongitude(valueOf(longitude).doubleValue());

                    float distanceInMeters = loc1.distanceTo(loc2);*/


                }

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse.WeatherResult> call, Throwable t) {

                restaurant_result.setText(t.getMessage());

                Toast.makeText(LocatorActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initializeNetworkLibrary() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nearbyPlaceApiService = retrofit.create(NearbyPlaceApiService.class);

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(50000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },101);

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}